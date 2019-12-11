package com.xiaohei.sp09.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.alibaba.fastjson.JSON;
import com.xiaohei.sp09.pojo.Orders;
import com.xiaohei.sp09.pojo.Users;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;

public class RedisUtil {
	public static final String SCORE_RANK = "score_rank";
	public static final String ORDER_RANK ="pay_order";//已支付订单
	public static final String WAIT_RANK ="wait_pay_order";//等待支付订单
	public static final String USER_INFO ="users_infos";//等待支付订单
	@Autowired
	private static StringRedisTemplate redisTemplate;
	@Autowired
	private Orders order;
	
	/*
	 * 动态存入list
	 */
	public void addlist(Orders order) {
		ValueOperations<String, String> operations = redisTemplate.opsForValue();

//		Orders order0 = new Orders();
//		Orders order1 = new Orders();
//		Orders order2 = new Orders();
//		Orders order3 = new Orders();
//		order1.setOpenid("123");
//		order1.setPaytime(new Date());
//
//		order2.setOpenid("456");
//		order2.setPaytime(new Date());
//
//		order3.setOpenid("789");
//		order3.setPaytime(new Date());

		ArrayList<Orders> list = new ArrayList();
		list.add(order);
//		list.add(order1);
//		list.add(order2);
//		list.add(order3);
		Jedis jedis = new Jedis();
		for(Orders o : list) {
			//对象转json
			String str = JSON.toJSONString(o);
			System.out.println(str);
			//把orders这个对象存放到redis中
			jedis.lpush("wxpay:order:top",str);
		}
	}
	
	/*
	 * 取出List
	 */
	public static Collection<Object> getlist(){

	Jedis jedis = new Jedis();
	    //wxpay:order:top
		Collection<String> list = jedis.lrange("wxpay:order:top", 0, -1 );
		
		JSONArray list1=JSONArray.fromObject(list);
		//JSONObject.toBean(list1,Orders.class);
		System.out.println("订单集合:"+list);
		return list1;
	}
	
	/*
	 * 将对象存入hash
	 */
	public void addHash(Users user) {
//		Users user1 = new Users();
//		Users user2= new Users();
//		Users user3 = new Users();

//		user1.setOpenId("123");
//		user1.setHeadImgUrl("http:hello");
//		user1.setNickname("赵子龙");
//		String str1 = JSON.toJSONString(user1);
//		user2.setOpenId("456");
//		user2.setHeadImgUrl("http:feichengwurao");
//		user2.setNickname("张飞");
//		String str2 = JSON.toJSONString(user2);
//		user3.setOpenId("789");
//		user3.setHeadImgUrl("http:sanguo");
//		user3.setNickname("孔明");
//		String str3 = JSON.toJSONString(user3);
		ArrayList<Users> list = new ArrayList();
		for(Users u : list) {
			//String openid = u.getOpenId();
			//String str = JSON.toJSONString(u);
			//redisTemplate.opsForHash().put("k1", openid, str);
		}
		//put方法
//		redisTemplate.opsForHash().put("k1", "123", str1);
//		redisTemplate.opsForHash().put("k1", "456", str2);
//		redisTemplate.opsForHash().put("k1", "789", str3);

		//hashKey不存在时，才设值
		//redisTemplate.opsForHash().putIfAbsent(key, hashKey, value)
	}
	
	/*
	 *根据list 从hash中取出对应的对象list 
	 * 
	 */
	public static List<Object> getHash(Collection<String> openid) {
		//multiGet方法，根据key和多个hashkey找出对应的多个值
		//Collection<String> list = new ArrayList<>();
		Collection<Object> keys = new ArrayList<>();
//        for(Object k : openid) {
//        	keys.add(k);
//        }
		keys.add("789");
		keys.add("456");
		keys.add("123");
		List<Object> user_info = redisTemplate.opsForHash().multiGet("k1", keys);
		System.out.println(user_info);
		return user_info;
	}

	
}
