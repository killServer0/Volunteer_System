package com.entity.view;

import org.apache.tools.ant.util.DateUtils;
import com.annotation.ColumnInfo;
import com.entity.FeizhiyuanEntity;
import com.baomidou.mybatisplus.annotations.TableName;
import org.apache.commons.beanutils.BeanUtils;
import java.lang.reflect.InvocationTargetException;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import com.utils.DateUtil;

/**
* 游客
* 后端返回视图实体辅助类
* （通常后端关联的表或者自定义的字段需要返回使用）
*/
@TableName("feizhiyuan")
public class FeizhiyuanView extends FeizhiyuanEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	//当前表
	/**
	* 性别的值
	*/
	@ColumnInfo(comment="性别的字典表值",type="varchar(200)")
	private String sexValue;




	public FeizhiyuanView() {

	}

	public FeizhiyuanView(FeizhiyuanEntity feizhiyuanEntity) {
		try {
			BeanUtils.copyProperties(this, feizhiyuanEntity);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	//当前表的
	/**
	* 获取： 性别的值
	*/
	public String getSexValue() {
		return sexValue;
	}
	/**
	* 设置： 性别的值
	*/
	public void setSexValue(String sexValue) {
		this.sexValue = sexValue;
	}




	@Override
	public String toString() {
		return "FeizhiyuanView{" +
			", sexValue=" + sexValue +
			"} " + super.toString();
	}
}
