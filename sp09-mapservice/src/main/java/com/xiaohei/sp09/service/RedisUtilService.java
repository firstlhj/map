package com.xiaohei.sp09.service;

import com.xiaohei.sp09.vo.DynamicMapVO;
import com.xiaohei.sp09.vo.MapVO;

public interface RedisUtilService {
  
    void saveAndget();
	DynamicMapVO queryall();
	MapVO query();
}
