package com.xiaohei.sp09.pojo;
public class Users {
	private String headImgUrl;
	//private String openId;
	private String nickname;
	private String payTime;
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	public String getHeadImgUrl() {
		return headImgUrl;
	}
	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}
	
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	@Override
	public String toString() {
		return "Users [headImgUrl=" + headImgUrl + ", nickname=" + nickname + ", payTime=" + payTime + "]";
	}

}