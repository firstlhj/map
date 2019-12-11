package com.xiaohei.sp09.pojo;

import java.io.Serializable;
import java.util.Date;

public class Summery implements Serializable {
	private Integer id;
	private String date;
	private Long vistorNums;
	private Long particiNums;
	private  Long plantsNums;
	//private int t;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Long getVistorNums() {
		return vistorNums;
	}
	public void setVistorNums(Long vistorNums) {
		this.vistorNums = vistorNums;
	}
	public Long getParticiNums() {
		return particiNums;
	}
	public void setParticiNums(Long particiNums) {
		this.particiNums = particiNums;
	}
	public Long getPlantsNums() {
		return plantsNums;
	}
	public void setPlantsNums(Long plantsNums) {
		this.plantsNums = plantsNums;
	}
	@Override
	public String toString() {
		return "Summery [id=" + id + ", date=" + date + ", vistorNums=" + vistorNums + ", particiNums=" + particiNums
				+ ", plantsNums=" + plantsNums + "]";
	}
	
}
