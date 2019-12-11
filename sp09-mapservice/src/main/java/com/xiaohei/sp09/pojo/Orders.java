package com.xiaohei.sp09.pojo;

import java.io.Serializable;
import java.util.Date;

public class Orders implements Serializable {
	private String openid;
	private Date paytime;
	private Integer status;//1,表示支付成功过
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public Date getPaytime() {
		return paytime;
	}
	public void setPaytime(Date paytime) {
		this.paytime = paytime;
	}
	public Integer getStatus() {
		return status;
	}
	@Override
	public String toString() {
		return "Orders [openid=" + openid + ", paytime=" + paytime + ", status=" + status + "]";
	}

}
