package com.xiaohei.sp09.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.xiaohei.sp09.mapper.UpdateSummeryMapper;
import com.xiaohei.sp09.pojo.CountSummery;
import com.xiaohei.sp09.pojo.Summery;
import com.xiaohei.sp09.pojo.Users;
import com.xiaohei.sp09.util.RedisUtil;
import com.xiaohei.sp09.vo.DynamicMapVO;
import com.xiaohei.sp09.vo.MapVO;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;

@Service
public class RedisUtilServiceImpl implements RedisUtilService {
	public static final String ORDER_RANK ="pay_order";//已支付订单
	public static long particiNums;//参与人数
	public static long vistorNums ;//访问人数
	public static long plantsNums;//认种统计
	//public static List<Users> user2;//最新用户头像和昵称集合
	//public static int time;
	//public static long i;


	@Autowired
	private UpdateSummeryMapper updateSummeryMapper;
	
	@Autowired
	private  StringRedisTemplate redisTemplate;
	DynamicMapVO dynamicMapVO = new DynamicMapVO();
	
	@Override
	public MapVO query() {
		//1.从list订单表中取出最新10条订单的openID
		MapVO mapVO = new MapVO();
		Collection<String> list1 = new ArrayList();
		ListOperations<String, String> listOperations = redisTemplate.opsForList();
		
		Long size1 = redisTemplate.opsForList().size("wxpay:order:top");
		if(size1<10) {
			list1 = redisTemplate.opsForList().range("wxpay:order:top",0,size1-1);
		}
		else list1 = redisTemplate.opsForList().range("wxpay:order:top",size1-10,size1-1);
		
		Long size = redisTemplate.opsForList().size("wxpay:order:top");
		if(size<11) {
			listOperations.trim("wxpay:order:top", 0, size-size1-1);
		}
		else listOperations.trim("wxpay:order:top", 0, size-11);
		//1.0json串转换成json对象
		JSONArray jsonArray = JSONArray.fromObject(list1);
		List paytimeList = new ArrayList();
		Collection<Object> openid = new ArrayList();

		for(int i=0;i<jsonArray.size();i++){
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			String str =(String) jsonObject.getString("wechatOpenId");
			String payTime =(String) jsonObject.getString("payTime");
			String payTimestr = payTime.substring(5,10);//截取支付时间
			openid.add(str);
			paytimeList.add(payTimestr);
		}

		System.out.println("支付时间"+paytimeList);
		//1.1根据订单的openid 对应的查询最新用户头像和
		System.out.println("Openid"+openid);

		//从实体对象中单独取出头像和昵称
		List<Object> multiGet = this.redisTemplate.opsForHash().multiGet("wxpay:wechatUser", openid);
		System.out.println("获取头像和昵称"+multiGet);
		JSONArray Array=JSONArray.fromObject(multiGet);

		List<Users> user2 = new ArrayList<Users>();
		for(int i=0; i<Array.size();i++) {
			Users user = new Users();
			System.out.println(Array.get(i));
			JSONObject obj = new JSONObject().fromObject(Array.get(i));
			String img = obj.getString("avatar");
			System.out.println(img);
			String nick = obj.getString("nickname");
			System.out.println(nick);
			user.setHeadImgUrl(img);
			user.setNickname(nick);
			user.setPayTime((String) paytimeList.get(i));
			user2.add(user);

		}

		//封装用户头像和昵称
		mapVO.setUser2(user2);

		return mapVO;

	} 


	//返回每天的静态和当天动态
	@Override
	@Scheduled(fixedRate=5000)
	public DynamicMapVO queryall(){
		System.out.println("---------------每2秒执行一次");

		//2.获取参与人数--------原订单去重数量
		//wxpay:wechatUser:paid
		particiNums= redisTemplate.opsForSet().size("wxpay:wechatUser:paid");
		System.out.println("参与人数:___" +particiNums);

		//3.获取访问人数
		//用户表:wxpay:wechartUser
		Set<Object> keys1 = redisTemplate.opsForHash().keys("wxpay:wechatUser");
		long count =0;
		for (Object string : keys1) {
			++count;
		}
		System.out.println("访问总人数:"+count);
		vistorNums=count;

		//4.认种统计---------认证统计就是原订单数
		//wxpay:order:count
		String trees=(String) redisTemplate.opsForValue().get("wxpay:order:count");
		try {
			if(trees.isEmpty()) plantsNums=0;
			else plantsNums=Long.valueOf(trees);
		} catch (Exception e) {
			throw new IllegalArgumentException("缓存捐种数据为空!");
		}
		System.out.println("认证多少颗:"+plantsNums);






		//缓存取出最新列表
		List<String> VistorNums=redisTemplate.opsForList().range("VistorNumkey",0,-1);
		List<String> ParticiNums=redisTemplate.opsForList().range("ParticiNumkey",0,-1);
		List<String> PlantsNums=redisTemplate.opsForList().range("PlantsNumkey",0,-1);
		List<String> Dates=redisTemplate.opsForList().range("Datekey",0,-1);
		List<Long> VistorNumsvo = StringChangeLong(VistorNums);
		List<Long> ParticiNumsvo = StringChangeLong(ParticiNums);
		List<Long> PlantsNumsvo = StringChangeLong(PlantsNums);

		System.out.println("最新5位VistorNums"+VistorNums);
		System.out.println("ParticiNums"+ParticiNums);
		System.out.println("PlantsNums"+PlantsNums);
		System.out.println("Dates"+Dates);



		String T = (String) redisTemplate.opsForValue().get("Total1");
		CountSummery Total = JSON.parseObject(T,CountSummery.class);
		//实时-之前总和=当天实时数据
		//ArrayList todayData = new ArrayList();
		if(Total==null||Total.getParticiSum()==null) {
			ParticiNumsvo.add(particiNums);
		}
		else {
			long lparticiNums = particiNums-Total.getParticiSum();
			ParticiNumsvo.add(lparticiNums);
			if((lparticiNums)<0)
				throw new IllegalArgumentException("非法数据");

		}
		if(Total==null||Total.getVistorSum()==null) {
			VistorNumsvo.add(vistorNums);
		}
		else {
			long lvistorNums = vistorNums-Total.getVistorSum();
			VistorNumsvo.add(lvistorNums);
			if((lvistorNums)<0)
				throw new IllegalArgumentException("非法数据");

		}




		//当天时间
		Calendar now = Calendar.getInstance(); 
		int month = (now.get(Calendar.MONTH) + 1);
		int day = (now.get(Calendar.DAY_OF_MONTH));
		String dateStr;
		String dateString;
		if(day<10) dateString = month+"-0"+ day;
		else  dateString = month+"-"+ day;
		
		
		System.out.println("dateString"+dateString);
		Dates.add(dateString);

		PlantsNumsvo.add(plantsNums);





		//封装最新5天数据
		dynamicMapVO.setVistorNum(VistorNumsvo);
		dynamicMapVO.setParticiNum(ParticiNumsvo);
		dynamicMapVO.setPlantsNum(PlantsNumsvo);
		dynamicMapVO.setCurrentplantNums(plantsNums);
		dynamicMapVO.setDate(Dates);
		System.out.println(dynamicMapVO);
		return dynamicMapVO;
	}








	//定时每天晚24点保存入库并取出每天静态数据
	@Scheduled(cron = "00 55 23 * * ?") 
	@Override
	public  synchronized void saveAndget() {

		System.out.println("每天执行一次");
		System.out.println(particiNums);
		System.out.println(vistorNums);
		System.out.println(plantsNums);



		//取出之前的各数据分别的总和
		CountSummery Total = updateSummeryMapper.countTotal();
		System.out.println("Total:"+Total);



		Long a = Total.getVistorSum();
		Long b = Total.getParticiSum();
		Long c = Total.getPlantsSum();
		//System.out.println("------"+a);
		long VistorNums = 0;
		long ParticiNums = 0;
		long PlantsNums = 0;
	


		VistorNums = vistorNums-(long)Total.getVistorSum();
		ParticiNums = particiNums-(long)Total.getParticiSum();
		PlantsNums = plantsNums;
		if((VistorNums* ParticiNums*PlantsNums)<0) {
			
			throw new IllegalArgumentException("非法数据----");}



		//存入当天总数

		updateSummeryMapper.saveSummery(VistorNums, ParticiNums, PlantsNums);

		//取出之前的各数据分别的总和
		CountSummery Total1 = updateSummeryMapper.countTotal();
		//总和存入缓存
		String str = JSON.toJSONString(Total1);
		redisTemplate.opsForValue().set("Total1",str);

		//获取最新6天综合数据
		List<Summery>  recent5Day = updateSummeryMapper.getSmmery();
		System.out.println("=========="+recent5Day);
		List<Long>VistorNum = recent5Day.stream().map(Summery::getVistorNums).collect(Collectors.toList());
		List<Long>ParticiNum  = recent5Day.stream().map(Summery::getParticiNums).collect(Collectors.toList());
		List<Long>PlantsNum  = recent5Day.stream().map(Summery::getPlantsNums).collect(Collectors.toList());
		List<String>Date  = recent5Day.stream().map(Summery::getDate).collect(Collectors.toList());
		System.out.println("DATE---------1"+Date);


		Long sizev = redisTemplate.opsForList().leftPushAll("VistorNumkey",LongChangeString(VistorNum));
		Long sizep = redisTemplate.opsForList().leftPushAll("ParticiNumkey",LongChangeString(ParticiNum));
		Long sizet = redisTemplate.opsForList().leftPushAll("PlantsNumkey",LongChangeString(PlantsNum));
		Long sized = redisTemplate.opsForList().leftPushAll("Datekey", Date);

		//移除旧的,保留最新6位
		while (redisTemplate.opsForList().size("VistorNumkey") >=6){
			redisTemplate.opsForList().rightPop("VistorNumkey");
		}
		//移除旧的,保留最新6位
		while (redisTemplate.opsForList().size("ParticiNumkey") >=6){
			redisTemplate.opsForList().rightPop("ParticiNumkey");
		}

		//移除旧的,保留最新6位
		while (redisTemplate.opsForList().size("PlantsNumkey") >=6){
			redisTemplate.opsForList().rightPop("PlantsNumkey");

		}
		//移除旧的,保留最新6位
		while (redisTemplate.opsForList().size("Datekey") >=6){
			redisTemplate.opsForList().rightPop("Datekey");

		}



	}
	//数据结构转换
	static List<String> LongChangeString(List<Long> T){
		List<String> list = new ArrayList();
		for(Long t : T) {
			list.add(String.valueOf(t));
		}
		return list;

	}
	static List<Long> StringChangeLong(List<String> T){
		List<Long> list = new ArrayList();
		for(String t : T) {
			list.add(Long.valueOf(t));
		}
		return list;

	}



}


