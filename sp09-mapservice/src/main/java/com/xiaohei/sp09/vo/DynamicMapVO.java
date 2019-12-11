package com.xiaohei.sp09.vo;

import java.util.List;

import com.xiaohei.sp09.pojo.Users;



/*
 * 3秒内动态实时数据封装
 */
public class DynamicMapVO {
private List<Users> user2;//用户头像和昵称
private List<Long> VistorNum ;//最新5天访问数据
private List<Long>ParticiNum;//最新5天参与数据
private List<Long>PlantsNum;//最新5天种植累积数据
private Long CurrentplantNums;//当前认种数
private List<String>Date;
//private List<Object> todayData;//当天实时数据

public List<String> getDate() {
	return Date;
}
public void setDate(List<String> date) {
	Date = date;
}
//public List<Object> getTodayData() {
//	return todayData;
//}
//public void setTodayData(List<Object> todayData) {
//	this.todayData = todayData;
//}
public List<Long> getVistorNum() {
	return VistorNum;
}
public void setVistorNum(List<Long> vistorNum) {
	VistorNum = vistorNum;
}
public List<Long> getParticiNum() {
	return ParticiNum;
}
public void setParticiNum(List<Long> particiNum) {
	ParticiNum = particiNum;
}
public List<Long> getPlantsNum() {
	return PlantsNum;
}
public void setPlantsNum(List<Long> plantsNum) {
	PlantsNum = plantsNum;
}
public List<Users> getUser2() {
	return user2;
}
public void setUser2(List<Users> user2) {
	this.user2 = user2;
}

public Long getCurrentplantNums() {
	return CurrentplantNums;
}
public void setCurrentplantNums(Long currentplantNums) {
	CurrentplantNums = currentplantNums;
}
@Override
public String toString() {
	return "DynamicMapVO [user2=" + user2 + ", VistorNum=" + VistorNum + ", ParticiNum=" + ParticiNum + ", PlantsNum="
			+ PlantsNum + ", CurrentplantNums=" + CurrentplantNums + ", Date=" + Date + "]";
}



}
