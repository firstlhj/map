package com.xiaohei.sp09.service;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.xiaohei.sp09.mapper.ProvinceSortMapper;
import com.xiaohei.sp09.pojo.ProvinceSort;



@Service
public class ProvinceSortImpl implements ProvinceSortService {
 @Autowired
 private ProvinceSortMapper provinceSortmapper;
@Autowired
RedisTemplate redisTemplate;

@Override
public List<ProvinceSort> queryProvinceSort() {
	
	 String key = "sort";  // redis中存储所有用户名的key
     List<ProvinceSort> provinceSort = null;    // 存储所有省份排名
     // 如果不为空，读取redis中用户名，否则 到数据库取所有用户名存储到redis
     if(redisTemplate.opsForList().size(key) > 0){
    	 provinceSort = redisTemplate.opsForList().range(key, 0, -1);
    	 redisTemplate.expire(key, 1, TimeUnit.DAYS);//过期时间是1天
    	// redisTemplate.expireat(key,)
     }
    else{
    	ProvinceSort provinceOther = provinceSortmapper.selectOther();
    	provinceOther.setProvince("其他");
    	 provinceSort = provinceSortmapper.selectProvinceSort();
    	 
    	 provinceSort.add(provinceOther);
    	 //list 降序排序
    	 provinceSort.sort(Comparator.comparing(ProvinceSort::getNums).reversed());
        
    	 redisTemplate.opsForList().rightPushAll(key,provinceSort);
     }
     System.out.println(provinceSort);
	return provinceSort;
}
 
}
