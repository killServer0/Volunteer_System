package com.controller;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.annotation.IgnoreAuth;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.entity.ConfigEntity;
import com.entity.EIException;
import com.service.ConfigService;
import com.utils.R;

/**
 * 上传文件映射表
 */
@RestController
@RequestMapping("file")
@SuppressWarnings({"unchecked","rawtypes"})
public class FileController{
	@Autowired
    private ConfigService configService;
	/**
	 * 上传文件
	 */
	@RequestMapping("/upload")
	public R upload(@RequestParam("file") MultipartFile file, String type) throws Exception {
		try {
			// 检查文件是否为空
			if (file.isEmpty()) {
				throw new EIException("上传文件不能为空");
			}

			// 检查文件类型
			String originalFilename = file.getOriginalFilename();
			String fileExt = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
			if (!Arrays.asList("jpg", "jpeg", "png", "gif").contains(fileExt)) {
				throw new EIException("只支持上传jpg、jpeg、png、gif格式的图片");
			}

			// 检查文件大小（2MB）
			if (file.getSize() > 2 * 1024 * 1024) {
				throw new EIException("图片大小不能超过2MB");
			}

			// 获取上传目录
			File path = new File(ResourceUtils.getURL("classpath:static").getPath());
			if (!path.exists()) {
				path = new File("");
			}
			File upload = new File(path.getAbsolutePath(), "/upload/");
			if (!upload.exists()) {
				upload.mkdirs();
			}

			// 生成唯一文件名
			String fileName = new Date().getTime() + "." + fileExt;
			File dest = new File(upload.getAbsolutePath() + "/" + fileName);

			// 保存文件
			try {
				file.transferTo(dest);
				System.out.println("文件已保存到: " + dest.getAbsolutePath());
			} catch (IOException e) {
				System.out.println("保存文件失败: " + e.getMessage());
				throw new EIException("保存文件失败，请重试");
			}

			// 如果是人脸识别文件，更新配置
			if (StringUtils.isNotBlank(type) && type.equals("1")) {
				ConfigEntity configEntity = configService.selectOne(new EntityWrapper<ConfigEntity>().eq("name", "faceFile"));
				if (configEntity == null) {
					configEntity = new ConfigEntity();
					configEntity.setName("faceFile");
					configEntity.setValue(fileName);
				} else {
					configEntity.setValue(fileName);
				}
				configService.insertOrUpdate(configEntity);
			}

			return R.ok().put("file", "upload/" + fileName);
		} catch (EIException e) {
			return R.error(e.getMessage());
		} catch (Exception e) {
			System.out.println("上传文件时发生错误: " + e.getMessage());
			e.printStackTrace();
			return R.error("上传文件失败，请重试");
		}
	}
	
	/**
	 * 下载文件
	 */
	@IgnoreAuth
	@RequestMapping("/download")
	public ResponseEntity<byte[]> download(@RequestParam String fileName) {
		try {
			System.out.println("开始下载文件，文件名: " + fileName);
			
			// 安全检查：防止目录遍历攻击
			if (fileName.contains("..") || fileName.contains("/") || fileName.contains("\\")) {
				System.out.println("文件名包含非法字符");
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

			File path = new File(ResourceUtils.getURL("classpath:static").getPath());
			if (!path.exists()) {
				path = new File("");
			}
			File upload = new File(path.getAbsolutePath(), "/upload/");
			if (!upload.exists()) {
				upload.mkdirs();
			}

			File file = new File(upload.getAbsolutePath() + "/" + fileName);
			System.out.println("文件完整路径: " + file.getAbsolutePath());

			if (file.exists()) {
				System.out.println("文件存在，开始读取");
				HttpHeaders headers = new HttpHeaders();
				String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
				switch (extension) {
					case "jpg":
					case "jpeg":
						headers.setContentType(MediaType.IMAGE_JPEG);
						break;
					case "png":
						headers.setContentType(MediaType.IMAGE_PNG);
						break;
					case "gif":
						headers.setContentType(MediaType.IMAGE_GIF);
						break;
					default:
						headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
				}
				return new ResponseEntity<>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
			} else {
				System.out.println("文件不存在: " + file.getAbsolutePath());
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (IOException e) {
			System.out.println("下载文件时发生错误: " + e.getMessage());
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
