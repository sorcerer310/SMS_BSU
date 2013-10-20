package com.bsu.system.tool;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
//import org.json.JSONException;
import org.json.JSONObject;

public class JSONMsg {
	private static Map<Integer,String> infome = new HashMap<Integer,String>();
	static{
		infome.put(1001, "登陆信息验证失败!");
		infome.put(1002, "注册失败");
		infome.put(1003, "未取出功能数据");
		infome.put(1004, "未提供state参数给general_update");
		infome.put(1005, "state参数不合法");
		infome.put(1006, "未提供table参数给general_update");
		infome.put(1007, "未提供id参数给general_update");
		infome.put(1011,"上传文件servlet的type参数不正确");
		infome.put(1012,"上传文件serlvet的id不正确或为空");
		infome.put(1013, "上传文件servlet 未获得ContentType数据");
		infome.put(1014, "未获得boundary分隔符，请确认是否指定了上传文件");
		infome.put(1015, "未获得正确父id");
		infome.put(1016, "用户不属于分公司机关,不能上传文件");
		infome.put(1017, "插入数据失败");
		infome.put(1020, "数据权限检查未通过");
		infome.put(1111, "发生异常");
		infome.put(2222, "请求缺少参数");
		
		
		
		infome.put(3001, "登陆成功");
		infome.put(3002, "注册成功");
		infome.put(3003, "更新操作成功");
		infome.put(3004, "删除操作成功");
		infome.put(3005,"保存数据成功");
		infome.put(3006, "用户第一次登陆,未查到玩家数据");
		infome.put(3020, "数据权限检查通过");
		infome.put(3021, "获得unitid数据");
	}

	/**
	 * 根据信息编号返回对应信息的json格式数据
	 * @param no		信息编号详见infome内容
	 * @return				转换为json数据的信息
	 */
	public static String info(Integer no) {
		JSONObject jo = new JSONObject();
		try {
			jo.put("_no", no);
			jo.put("_msg", infome.get(no));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jo.toString();
	}
	/**
	 * 根据信息编号返回对应信息的json格式数据,并附带额外信息
	 * @param no		信息编号见informe内容
	 * @param em		带入的额外信息.
	 * @return				转换为json数据信息
	 */
	public static String info(Integer no,String em)
	{
		JSONObject jo = new JSONObject();
		try{
			jo.put("_no", no);
			jo.put("_msg", infome.get(no));
			jo.put("_extramsg", em);
		}catch(JSONException e){
			e.printStackTrace();
		}
		return jo.toString();
	}
}
