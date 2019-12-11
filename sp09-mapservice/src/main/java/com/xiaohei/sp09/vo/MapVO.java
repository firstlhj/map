package com.xiaohei.sp09.vo;

import java.util.List;

import com.xiaohei.sp09.pojo.Users;



public class MapVO {
	private Long vistor;//访问人数
	 private Long participator;//参与人数
	// private List<CountUserInfo> newData;//更新的种树列表
	public Long getVistor() {
		return vistor;
	}
	public void setVistor(Long vistor) {
		this.vistor = vistor;
	}
	public Long getParticipator() {
		return participator;
	}
	public void setParticipator(Long participator) {
		this.participator = participator;
	}
	private List<Users> user2;
	public List<Users> getUser2() {
		return user2;
	}
	public void setUser2(List<Users> user2) {
		this.user2 = user2;
	}
	
	
	
}

