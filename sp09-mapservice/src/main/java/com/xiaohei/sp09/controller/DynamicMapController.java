package com.xiaohei.sp09.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.xiaohei.sp09.service.RedisUtilService;
import com.xiaohei.sp09.util.JsonResult;
import com.xiaohei.sp09.vo.DynamicMapVO;
import com.xiaohei.sp09.vo.MapVO;

@CrossOrigin(maxAge = 3600,origins = "*")
@RestController
@RequestMapping("/map")
public class DynamicMapController {
   @Autowired
   RedisUtilService redisService;
   @RequestMapping("/mapDynamic")
   public JsonResult<DynamicMapVO> queryDynamic() {
   DynamicMapVO u = redisService.queryall();
   MapVO query = redisService.query();
   u.setUser2(query.getUser2());
   return JsonResult.ok(u);  
   }
   
}
