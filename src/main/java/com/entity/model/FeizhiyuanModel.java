package com.entity.model;

import com.entity.FeizhiyuanEntity;

import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;


/**
 * 游客
 * 接收传参的实体类
 *（实际开发中配合移动端接口开发手动去掉些没用的字段， 后端一般用entity就够用了）
 * 取自ModelAndView 的model名称
 */
public class FeizhiyuanModel implements Serializable {
    private static final long serialVersionUID = 1L;




    /**
     * 主键
     */
    private Integer id;


    /**
     * 账户
     */
    private String username;


    /**
     * 密码
     */
    private String password;


    /**
     * 游客姓名
     */
    private String feizhiyuanName;


    /**
     * 游客手机号
     */
    private String feizhiyuanPhone;


    /**
     * 游客身份证号
     */
    private String feizhiyuanIdNumber;


    /**
     * 游客头像
     */
    private String feizhiyuanPhoto;


    /**
     * 性别
     */
    private Integer sexTypes;


    /**
     * 游客邮箱
     */
    private String feizhiyuanEmail;


    /**
     * 创建时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
    private Date createTime;


    /**
	 * 获取：主键
	 */
    public Integer getId() {
        return id;
    }


    /**
	 * 设置：主键
	 */
    public void setId(Integer id) {
        this.id = id;
    }
    /**
	 * 获取：账户
	 */
    public String getUsername() {
        return username;
    }


    /**
	 * 设置：账户
	 */
    public void setUsername(String username) {
        this.username = username;
    }
    /**
	 * 获取：密码
	 */
    public String getPassword() {
        return password;
    }


    /**
	 * 设置：密码
	 */
    public void setPassword(String password) {
        this.password = password;
    }
    /**
	 * 获取：游客姓名
	 */
    public String getFeizhiyuanName() {
        return feizhiyuanName;
    }


    /**
	 * 设置：游客姓名
	 */
    public void setFeizhiyuanName(String feizhiyuanName) {
        this.feizhiyuanName = feizhiyuanName;
    }
    /**
	 * 获取：游客手机号
	 */
    public String getFeizhiyuanPhone() {
        return feizhiyuanPhone;
    }


    /**
	 * 设置：游客手机号
	 */
    public void setFeizhiyuanPhone(String feizhiyuanPhone) {
        this.feizhiyuanPhone = feizhiyuanPhone;
    }
    /**
	 * 获取：游客身份证号
	 */
    public String getFeizhiyuanIdNumber() {
        return feizhiyuanIdNumber;
    }


    /**
	 * 设置：游客身份证号
	 */
    public void setFeizhiyuanIdNumber(String feizhiyuanIdNumber) {
        this.feizhiyuanIdNumber = feizhiyuanIdNumber;
    }
    /**
	 * 获取：游客头像
	 */
    public String getFeizhiyuanPhoto() {
        return feizhiyuanPhoto;
    }


    /**
	 * 设置：游客头像
	 */
    public void setFeizhiyuanPhoto(String feizhiyuanPhoto) {
        this.feizhiyuanPhoto = feizhiyuanPhoto;
    }
    /**
	 * 获取：性别
	 */
    public Integer getSexTypes() {
        return sexTypes;
    }


    /**
	 * 设置：性别
	 */
    public void setSexTypes(Integer sexTypes) {
        this.sexTypes = sexTypes;
    }
    /**
	 * 获取：游客邮箱
	 */
    public String getFeizhiyuanEmail() {
        return feizhiyuanEmail;
    }


    /**
	 * 设置：游客邮箱
	 */
    public void setFeizhiyuanEmail(String feizhiyuanEmail) {
        this.feizhiyuanEmail = feizhiyuanEmail;
    }
    /**
	 * 获取：创建时间
	 */
    public Date getCreateTime() {
        return createTime;
    }


    /**
	 * 设置：创建时间
	 */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    }
