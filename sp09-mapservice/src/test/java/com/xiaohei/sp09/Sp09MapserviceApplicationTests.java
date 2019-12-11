package com.xiaohei.sp09;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.xiaohei.sp09.pojo.Order;
import com.xiaohei.sp09.pojo.Users;
import com.xiaohei.sp09.pojo.WechatUser;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Sp09MapserviceApplicationTests {
	@Autowired
	private  StringRedisTemplate redisTemplate;
	private static int count;
	@Test
	public void contextLoads() {
	}



	/*
	 * 用户关注---redis-----hash结构
	 */
	@Test
	public void createUserInfo() {
		//动态产生用户关注
		for(long i = 0;i<100;i++) {
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



	}




	/*
	 * 1,订单产生------------------------------注意订单对应的openid数量不能超过关注用户的openid
	 * 2,最新10单存入redis----list结构
	 */
	@Test
	public void createOrder() {
		//动态产生订单
		List<Order> list1=new ArrayList<Order>();
		//	int count=0;
		for(long j =0;j<100;j++) {
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
	public  void addTopList(Order order, Long limit) {
		ListOperations<String, String> listOperations = redisTemplate.opsForList();

		Long size = listOperations.leftPush("wxpay:order:top", JSON.toJSONString(order));

		if (size != null && size >= limit) {
			listOperations.trim("wxpay:order:top", 0, limit - 1);
		}
	}


	/*
	 * 
	 */
	@Test
	public void getavatarandnick() {

		//1.从list订单表中取出最新10条订单的openID
		Collection<String> list1 = redisTemplate.opsForList().range("wxpay:order:top", 0, -1);
		System.out.println("list1:"+list1);
		//1.0json串转换成json对象
		JSONArray jsonArray = JSONArray.fromObject(list1);
		System.out.println(jsonArray);
		Collection<Object> openid = new ArrayList();
		for(int i=0;i<jsonArray.size();i++){
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			String str =(String) jsonObject.getString("wechatOpenId");
			openid.add(str);
		}

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
			user2.add(user);

		}
		System.out.println("头像"+user2);
	}

	/*
	 * 获取参与人数--------原订单去重数量
	 */
	@Test
	public void getParticiNums() {
		//wxpay:wechatUser:paid
		Long particiNums = redisTemplate.opsForSet().size("wxpay:wechatUser:paid");
		System.out.println("参与人数:___" +particiNums);
	}
	/*
	 * 获取访问人数
	 */
	//用户表:wxpay:wechartUser
	@Test
	public void getVistorNums() {
		Set<Object> keys1 = redisTemplate.opsForHash().keys("wxpay:wechatUser");
		long count =0;
		for (Object string : keys1) {
			++count;
		}
		System.out.println("访问总人数:"+count);
		long vistorNums = count;
	}


	/*
	 * 获取认证统计总数
	 */

	@Test
	public void getPlantsNums() {
		//4.认种统计---------认证统计就是原订单数
		//wxpay:order:count
		String trees=(String) redisTemplate.opsForValue().get("wxpay:order:count");
		long plantsNums;
		if(trees.isEmpty()) plantsNums=0;
		else plantsNums=Long.valueOf(trees);
		System.out.println("认证多少颗:"+plantsNums);
	}


	/*
	 * list多属性分组
	 * 
	 * 
	 */

	@Test
	public void rightPopAndLeftPush() {
		List<String>VistorNum = new ArrayList();
		VistorNum.add("h4");
		VistorNum.add("k4");
		VistorNum.add("j4");
		VistorNum.add("zb4");
		VistorNum.add("cv4");

		//Long popValue = redisTemplate.opsForList().rightPushAll("VistorNumkey",VistorNum);  
		Long popValue = redisTemplate.opsForList().leftPushAll("VistorNumkey",VistorNum);  
		System.out.print("通过rightPopAndLeftPush(K sourceKey, K destinationKey)方法移除的元素是:" + popValue);  

		List<String> list = redisTemplate.opsForList().range("VistorNumkey",0,-1); 
		System.out.println(",剩余的元素是list:" + list);
		//移除旧的,保留最新6位
		while (redisTemplate.opsForList().size("VistorNumkey") >=6){
			//redisTemplate.opsForList().leftPop("VistorNumkey");
			redisTemplate.opsForList().rightPop("VistorNumkey");
		}
		List<String> list2 = redisTemplate.opsForList().range("VistorNumkey",0,-1);  
		System.out.println(",剩余的元素是list2:" + list2);  
	}


	/*
	 * 自动化联合测试产生用户和订单
	 */
	@Test
	public void testuserAndorder() throws Exception {
		for(long k =0;k <30;k+=2) {
			//动态产生用户关注
			for(long i=0;i<(k*10);i++) {
				String openId =i+"openId";
				WechatUser User = new WechatUser();
				User.setAvatar("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1575004203931&di=5ac49bd76a4478baae03edf8d8cbbd43&imgtype=0&src=http%3A%2F%2Fpic.qqtn.com%2Fup%2F2019-4%2F2019041207552576624.jpg");
				User.setNickname(i+"nickname");
				User.setOpenId(openId);
				String users = JSON.toJSONString(User);
				Thread.sleep(500);
				//put方法,存入redis
				redisTemplate.opsForHash().put("wxpay:wechatUser", openId, users);
				System.out.println("用户关注成功");
			}



			//动态产生订单
			List<Order> list1=new ArrayList<Order>();
			//	int count=0;
			for(long j =0;j<(k*10);j++) {
				Order order = new Order();
				order.setWechatOpenId(j+"openId");
				order.setPayTime(LocalDateTime.now());
				//String orders = JSON.toJSONString(order);
				++count;
				list1.add(order);
				addTopList(order, 10L);
				//订单去重存入set
				redisTemplate.opsForSet().add("wxpay:wechatUser:paid",order.getWechatOpenId());
				Thread.sleep(500);
			}
			//存入认证总数
			//String counts = String.valueOf(count+=count);
			//redisTemplate.opsForValue().set("wxpay:order:count",counts);
			redisTemplate.opsForValue().increment("wxpay:order:count",count);

		// Thread.sleep(5000);//间隔1秒
		}


	}
	
	
	
}
