package com.xiaohei.sp09.pojo;

public class CountSummery {
private Long vistorSum;
private Long particiSum;
private Long plantsSum;
public Long getVistorSum() {
	return vistorSum;
}
public void setVistorSum(Long vistorSum) {
	this.vistorSum = vistorSum;
}
public Long getParticiSum() {
	return particiSum;
}
public void setParticiSum(Long particiSum) {
	this.particiSum = particiSum;
}
public Long getPlantsSum() {
	return plantsSum;
}
public void setPlantsSum(Long plantsSum) {
	this.plantsSum = plantsSum;
}
@Override
public String toString() {
	return "CoutSummery [vistorSum=" + vistorSum + ", particiSum=" + particiSum + ", plantsSum=" + plantsSum + "]";
}

}
