package com.entity.view;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;

import com.annotation.ColumnInfo;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.entity.FeihuodongEntity;

/**
* 活动
* 后端返回视图实体辅助类
* （通常后端关联的表或者自定义的字段需要返回使用）
*/
@TableName("feihuodong")
public class FeihuodongView extends FeihuodongEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	//当前表
	/**
	* 活动类型的值
	*/
	@ColumnInfo(comment="活动类型的字典表值",type="varchar(200)")
	private String feihuodongValue;

	//级联表 游客
		/**
		* 游客姓名
		*/

		@ColumnInfo(comment="游客姓名",type="varchar(200)")
		private String feizhiyuanName;
		/**
		* 游客手机号
		*/

		@ColumnInfo(comment="游客手机号",type="varchar(200)")
		private String feizhiyuanPhone;
		/**
		* 游客身份证号
		*/

		@ColumnInfo(comment="游客身份证号",type="varchar(200)")
		private String feizhiyuanIdNumber;
		/**
		* 游客头像
		*/

		@ColumnInfo(comment="游客头像",type="varchar(200)")
		private String feizhiyuanPhoto;
		/**
		* 游客邮箱
		*/

		@ColumnInfo(comment="游客邮箱",type="varchar(200)")
		private String feizhiyuanEmail;

	@ColumnInfo(comment="活动状态：1-待审核，2-未开始，3-进行中，4-已结束",type="int(11)")
	@TableField(value = "huodong_status")
	private Integer huodongStatus;

	@ColumnInfo(comment="活动状态的值",type="varchar(200)")
	private String huodongStatusValue;

	public FeihuodongView() {

	}

	public FeihuodongView(FeihuodongEntity feihuodongEntity) {
		try {
			BeanUtils.copyProperties(this, feihuodongEntity);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	//当前表的
	/**
	* 获取： 活动类型的值
	*/
	public String getFeihuodongValue() {
		return feihuodongValue;
	}
	/**
	* 设置： 活动类型的值
	*/
	public void setFeihuodongValue(String feihuodongValue) {
		this.feihuodongValue = feihuodongValue;
	}


	//级联表的get和set 游客

		/**
		* 获取： 游客姓名
		*/
		public String getFeizhiyuanName() {
			return feizhiyuanName;
		}
		/**
		* 设置： 游客姓名
		*/
		public void setFeizhiyuanName(String feizhiyuanName) {
			this.feizhiyuanName = feizhiyuanName;
		}

		/**
		* 获取： 游客手机号
		*/
		public String getFeizhiyuanPhone() {
			return feizhiyuanPhone;
		}
		/**
		* 设置： 游客手机号
		*/
		public void setFeizhiyuanPhone(String feizhiyuanPhone) {
			this.feizhiyuanPhone = feizhiyuanPhone;
		}

		/**
		* 获取： 游客身份证号
		*/
		public String getFeizhiyuanIdNumber() {
			return feizhiyuanIdNumber;
		}
		/**
		* 设置： 游客身份证号
		*/
		public void setFeizhiyuanIdNumber(String feizhiyuanIdNumber) {
			this.feizhiyuanIdNumber = feizhiyuanIdNumber;
		}

		/**
		* 获取： 游客头像
		*/
		public String getFeizhiyuanPhoto() {
			return feizhiyuanPhoto;
		}
		/**
		* 设置： 游客头像
		*/
		public void setFeizhiyuanPhoto(String feizhiyuanPhoto) {
			this.feizhiyuanPhoto = feizhiyuanPhoto;
		}

		/**
		* 获取： 游客邮箱
		*/
		public String getFeizhiyuanEmail() {
			return feizhiyuanEmail;
		}
		/**
		* 设置： 游客邮箱
		*/
		public void setFeizhiyuanEmail(String feizhiyuanEmail) {
			this.feizhiyuanEmail = feizhiyuanEmail;
		}


	@Override
	public String toString() {
		return "FeihuodongView{" +
			", feihuodongValue=" + feihuodongValue +
			", feizhiyuanName=" + feizhiyuanName +
			", feizhiyuanPhone=" + feizhiyuanPhone +
			", feizhiyuanIdNumber=" + feizhiyuanIdNumber +
			", feizhiyuanPhoto=" + feizhiyuanPhoto +
			", feizhiyuanEmail=" + feizhiyuanEmail +
			", huodongStatus=" + huodongStatus +
			", huodongStatusValue=" + huodongStatusValue +
			"} " + super.toString();
	}
}
