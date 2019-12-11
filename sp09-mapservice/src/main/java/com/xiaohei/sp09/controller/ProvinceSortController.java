package com.xiaohei.sp09.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xiaohei.sp09.pojo.ProvinceSort;
import com.xiaohei.sp09.service.ProvinceSortService;
import com.xiaohei.sp09.util.JsonResult;

@CrossOrigin(maxAge = 3600,origins = "*")
@RestController
@RequestMapping("/map")
public class ProvinceSortController {
@Autowired
private ProvinceSortService provinceSortService;

@GetMapping("/sort")
public JsonResult<List<ProvinceSort>> querySort() {
	List<ProvinceSort> sort =provinceSortService.queryProvinceSort();
	System.out.println(sort);
	//return sort;
	return JsonResult.ok(sort); 
	
}
}
