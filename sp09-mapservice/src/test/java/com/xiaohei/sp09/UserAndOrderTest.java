package com.xiaohei.sp09;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.alibaba.fastjson.JSON;
import com.xiaohei.sp09.pojo.Order;
import com.xiaohei.sp09.pojo.WechatUser;




@SpringBootTest
public class UserAndOrderTest {
	@Autowired
	private static  StringRedisTemplate redisTemplate;
	private static int count;
	public static void main(String[] args) {
		for(long k =0;k <20;k+=2) {
		//动态产生用户关注
		for(long i=0;i<(k*100);i++) {
			String openId =i+"openId";
			WechatUser User = new WechatUser();
			User.setAvatar(i+"http://img");
			User.setNickname(i+"nickname");
			User.setOpenId(openId);
			String users = JSON.toJSONString(User);

			//put方法,存入redis
			redisTemplate.opsForHash().put("wxpay:wechatUser", openId, users);
			System.out.println("用户关注成功");
		}



		//动态产生订单
		List<Order> list1=new ArrayList<Order>();
		//	int count=0;
		for(long j =0;j<(k*100);j++) {
			Order order = new Order();
			order.setWechatOpenId(j+"openId");
			order.setPayTime(LocalDateTime.now());
			//String orders = JSON.toJSONString(order);
			++count;
			list1.add(order);
			addTopList( order, 10L);
			//订单去重存入set
			redisTemplate.opsForSet().add("wxpay:wechatUser:paid",order.getWechatOpenId());

		}
		//存入认证总数
		//String counts = String.valueOf(count+=count);
		//redisTemplate.opsForValue().set("wxpay:order:count",counts);
		redisTemplate.opsForValue().increment("wxpay:order:count",count);

		}

	}
	public static  void addTopList(Order order, Long limit) {
		ListOperations<String, String> listOperations = redisTemplate.opsForList();

		Long size = listOperations.leftPush("wxpay:order:top", JSON.toJSONString(order));

		if (size != null && size >= limit) {
			listOperations.trim("wxpay:order:top", 0, limit - 1);
		}
	}

}
