package com.xiaohei.sp09.pojo;

import java.io.Serializable;

public class ProvinceSort implements Serializable {
  private String province;
  private Long nums;
public String getProvince() {
	return province;
}
public void setProvince(String province) {
	this.province = province;
}
public Long getNums() {
	return nums;
}
public void setNums(Long nums) {
	this.nums = nums;
}
@Override
public String toString() {
	return "ProvinceSort [province=" + province + ", nums=" + nums + "]";
}
  
}
