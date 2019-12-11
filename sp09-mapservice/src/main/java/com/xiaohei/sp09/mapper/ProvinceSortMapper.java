package com.xiaohei.sp09.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.xiaohei.sp09.pojo.ProvinceSort;



@Mapper
public interface ProvinceSortMapper {
     List<ProvinceSort>selectProvinceSort();
     ProvinceSort selectOther();
}
