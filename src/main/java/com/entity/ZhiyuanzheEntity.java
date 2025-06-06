package com.entity;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import com.annotation.ColumnInfo;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.utils.DateUtil;


/**
 * 志愿者
 *
 * @author 
 * @email
 */
@TableName("zhiyuanzhe")
public class ZhiyuanzheEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;


	public ZhiyuanzheEntity() {

	}

	public ZhiyuanzheEntity(T t) {
		try {
			BeanUtils.copyProperties(this, t);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    @ColumnInfo(comment="主键",type="int(11)")
    @TableField(value = "id")

    private Integer id;


    /**
     * 账户
     */
    @ColumnInfo(comment="账户",type="varchar(200)")
    @TableField(value = "username")

    private String username;


    /**
     * 密码
     */
    @ColumnInfo(comment="密码",type="varchar(200)")
    @TableField(value = "password")

    private String password;


    /**
     * 志愿者姓名
     */
    @ColumnInfo(comment="志愿者姓名",type="varchar(200)")
    @TableField(value = "zhiyuanzhe_name")

    private String zhiyuanzheName;


    /**
     * 志愿者手机号
     */
    @ColumnInfo(comment="志愿者手机号",type="varchar(200)")
    @TableField(value = "zhiyuanzhe_phone")

    private String zhiyuanzhePhone;


    /**
     * 志愿者身份证号
     */
    @ColumnInfo(comment="志愿者身份证号",type="varchar(200)")
    @TableField(value = "zhiyuanzhe_id_number")

    private String zhiyuanzheIdNumber;


    /**
     * 志愿者头像
     */
    @ColumnInfo(comment="志愿者头像",type="varchar(200)")
    @TableField(value = "zhiyuanzhe_photo")

    private String zhiyuanzhePhoto;


    /**
     * 性别
     */
    @ColumnInfo(comment="性别",type="int(11)")
    @TableField(value = "sex_types")

    private Integer sexTypes;


    /**
     * 志愿者邮箱
     */
    @ColumnInfo(comment="志愿者邮箱",type="varchar(200)")
    @TableField(value = "zhiyuanzhe_email")

    private String zhiyuanzheEmail;


    /**
     * 创建时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
    @ColumnInfo(comment="创建时间",type="timestamp")
    @TableField(value = "create_time",fill = FieldFill.INSERT)

    private Date createTime;


    /**
     * 状态
     */
    @ColumnInfo(comment="状态",type="int(11)")
    @TableField(value = "status")

    private Integer status;


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
	 * 获取：志愿者姓名
	 */
    public String getZhiyuanzheName() {
        return zhiyuanzheName;
    }
    /**
	 * 设置：志愿者姓名
	 */

    public void setZhiyuanzheName(String zhiyuanzheName) {
        this.zhiyuanzheName = zhiyuanzheName;
    }
    /**
	 * 获取：志愿者手机号
	 */
    public String getZhiyuanzhePhone() {
        return zhiyuanzhePhone;
    }
    /**
	 * 设置：志愿者手机号
	 */

    public void setZhiyuanzhePhone(String zhiyuanzhePhone) {
        this.zhiyuanzhePhone = zhiyuanzhePhone;
    }
    /**
	 * 获取：志愿者身份证号
	 */
    public String getZhiyuanzheIdNumber() {
        return zhiyuanzheIdNumber;
    }
    /**
	 * 设置：志愿者身份证号
	 */

    public void setZhiyuanzheIdNumber(String zhiyuanzheIdNumber) {
        this.zhiyuanzheIdNumber = zhiyuanzheIdNumber;
    }
    /**
	 * 获取：志愿者头像
	 */
    public String getZhiyuanzhePhoto() {
        return zhiyuanzhePhoto;
    }
    /**
	 * 设置：志愿者头像
	 */

    public void setZhiyuanzhePhoto(String zhiyuanzhePhoto) {
        this.zhiyuanzhePhoto = zhiyuanzhePhoto;
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
	 * 获取：志愿者邮箱
	 */
    public String getZhiyuanzheEmail() {
        return zhiyuanzheEmail;
    }
    /**
	 * 设置：志愿者邮箱
	 */

    public void setZhiyuanzheEmail(String zhiyuanzheEmail) {
        this.zhiyuanzheEmail = zhiyuanzheEmail;
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

    /**
	 * 获取：状态
	 */
    public Integer getStatus() {
        return status;
    }
    /**
	 * 设置：状态
	 */

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Zhiyuanzhe{" +
            ", id=" + id +
            ", username=" + username +
            ", password=" + password +
            ", zhiyuanzheName=" + zhiyuanzheName +
            ", zhiyuanzhePhone=" + zhiyuanzhePhone +
            ", zhiyuanzheIdNumber=" + zhiyuanzheIdNumber +
            ", zhiyuanzhePhoto=" + zhiyuanzhePhoto +
            ", sexTypes=" + sexTypes +
            ", zhiyuanzheEmail=" + zhiyuanzheEmail +
            ", createTime=" + DateUtil.convertString(createTime,"yyyy-MM-dd") +
            ", status=" + status +
        "}";
    }
}
