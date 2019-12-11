package com.xiaohei.sp09.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.xiaohei.sp09.pojo.CountSummery;
import com.xiaohei.sp09.pojo.Summery;


@Mapper
public interface UpdateSummeryMapper {
	//取出总和	
	CountSummery countTotal();
	//存入
	void saveSummery(@Param("vistorNums")Long vistorNums,@Param("particiNums")Long particiNums,@Param("plantsNums")Long plantsNums);
    //取出最新5天
	List<Summery> getSmmery();

	
}
