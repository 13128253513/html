package com.vcode.ticket.methods;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultListModel;

import com.vcode.ticket.events.EventUtils;
import com.vcode.ticket.model.MyThreadLocal;
import com.vcode.ticket.model.TrainInfo;
import com.vcode.ticket.utils.ConfigUtils;
import com.vcode.ticket.utils.LoggerUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.vcode.http.client.VHttpResponse;
import com.vcode.http.client.methods.VHttpGet;
import com.vcode.http.client.methods.VHttpPost;
import com.vcode.http.client.parames.VParames;
import com.vcode.http.utils.VBrowser;
import com.vcode.http.utils.VHttpUtils;
import com.vcode.ticket.ui.HomePage;
import com.vcode.ticket.utils.HttpUtils;

/**
 * 提交订单类
 * @author Administrator
 *
 */
public class HomeMethods extends Thread {
	

	public JSONObject obj2;
	
	private HomeMethods(){};
	
	private String passengerTicketStr;
	

	public HomeMethods(JSONObject obj){
		if (this.obj2==null) {
			this.obj2 = obj;
		}
	}

	@Override
	public void run() {
		SubmitOrder();
	}

	/**
	 * 提交订单
	 */
	private void SubmitOrder() {
		//TODO 目前只使用第一个乘客订票 我账号用不了麻烦修复下,......
		String username = MyThreadLocal.get().getSearchPassengerName().get(0);
		JSONObject userObj = MyThreadLocal.get().getPassenger().get(username);
		String[] seatTypes = new String[TrainInfo.seatLevel.getSize()];
		for (int i=0;i<TrainInfo.seatLevel.getSize();i++) {
			seatTypes[i] = TrainInfo.seatLevel.get(i).toString();
		}
		String station_train_code = "";
		try {
			station_train_code = obj2.get("station_train_code").toString();
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		passengerTicketStr = HttpUtils.getPassengerTicketStr(userObj, seatTypes, station_train_code);
		try {
			// 预订车票
			LoggerUtils.printLog("订单线程已启动，开始提交订票信息");
			VHttpPost post = new VHttpPost(URLUtils.getInitSubmitOrder().currentURL());
			VParames parames = new VParames();
			parames.clear();
			parames.put("secretStr", obj2.get("secretStr").toString());
			parames.put("train_date", MyThreadLocal.get().getTrainDate());
			parames.put("back_train_date",MyThreadLocal.get().getBackTrainDate());
			parames.put("tour_flag", "dc");
			parames.put("purpose_codes", "ADULT");
			parames.put("query_from_station_name", obj2.get("from_station_name")
					.toString());
			parames.put("query_to_station_name", obj2.get("to_station_name")
					.toString());
			parames.put("undefined", "");
			post.setParames(parames);
			VHttpResponse res = VBrowser.execute(post);
			String body = VHttpUtils.outHtml(res.getBody());
			JSONObject res_obj = new JSONObject(body);
			if ("true".equals(res_obj.get("status").toString())) {
				LoggerUtils.printLog("订票信息提交成功");
				res.getEntity().disconnect();
				initDc();
			} else {
				LoggerUtils.printLog(res_obj.get("messages").toString());
				res.getEntity().disconnect();
			}
		} catch (JSONException e) {
			LoggerUtils.printLog("提交订单失败，请联系作者QQ：372939956");
		}
	}

	/**
	 * 预定界面
	 */
	private void initDc() {
		VHttpPost post = new VHttpPost(URLUtils.getInitHtml().currentURL());
		VParames parames = new VParames();
		parames.clear();
		parames.put("_json_att", "");
		post.setParames(parames);
		VHttpResponse res = VBrowser.execute(post);
		String body = VHttpUtils.outHtml(res.getBody());

		Pattern pattern = Pattern
				.compile("var globalRepeatSubmitToken = '[0-9 | a-z]{32}'");
		Pattern pattern2 = Pattern
				.compile("'key_check_isChange':'[0-9 | A-Z]{56}'");
		Matcher matcher = pattern.matcher(body);
		Matcher matcher2 = pattern2.matcher(body);
		while (matcher.find()) {
			MyThreadLocal.get().setToken(matcher.group(0)
					.replace("var globalRepeatSubmitToken = '", "")
					.replace("'", ""));
		}
		while (matcher2.find()) {
			MyThreadLocal.get().setKeyCheckisChange(matcher2.group(0)
					.replace("'key_check_isChange':'", "").replace("'", ""));
		}
		res.getEntity().disconnect();
		if (MyThreadLocal.get().getToken() !=null && MyThreadLocal.get().getKeyCheckisChange() !=null){
			URLUtils.getInitHtml().OK();
		}else {
			URLUtils.getInitHtml().isFail();
		}
		LoggerUtils.printLog("开始拉取验证......");
		//2019年不需要验证码
		checkOrderInfo();
	}

	/**
	 * 拉取提交订单验证码及校验，返回true表示校验成功，反之否
	 * 
	 * @return 校验是否成功
	 */
	public void getSubmitCode() {
		// 拉取验证码
		MyThreadLocal.get().setValidCode("");
		String url = "https://kyfw.12306.cn/otn/passcodeNew/getPassCodeNew?module=passenger&rand=randp&"
				+ Math.random();
		VHttpGet get = new VHttpGet(url);
		VHttpResponse res = VBrowser.execute(get); // 获取验证码
		HttpUtils.getSubmitCodeBy12306(res.getBody(), this);
		res.getEntity().disconnect(); // 耗尽资源
	}

	/**
	 * 
	 * 校验验证码是否正确
	 * 
	 */
	public void checkSubmitCode(String code) {
		MyThreadLocal.get().setValidCode(code);
		LoggerUtils.printLog("当前验证码：" + code);
		VHttpPost post = new VHttpPost(
				"https://kyfw.12306.cn/otn/passcodeNew/checkRandCodeAnsyn");
		VParames parames5 = new VParames();
		parames5.put("randCode", code);
		parames5.put("rand", "randp");
		parames5.put("_json_att", "");
		parames5.put("REPEAT_SUBMIT_TOKEN", MyThreadLocal.get().getToken());

		post.setParames(parames5);
		VHttpResponse res = VBrowser.execute(post);
		String body = VHttpUtils.outHtml(res.getBody());
		try {
			JSONObject res_obj = new JSONObject(body);
			JSONObject dataObj = (JSONObject) res_obj.get("data");
			if ("1".equals(dataObj.get("result").toString())) {
				LoggerUtils.printLog("验证码正确，开始确认用户是否可以提交订单");
				checkOrderInfo();
			} else {
				LoggerUtils.printLog("验证码错误，请重新验证");
				getSubmitCode();
			}
		} catch (JSONException e) {
			LoggerUtils.printLog("解析验证码错误，请联系作者QQ：3094759846");
		}
	}

	/**
	 * 确认用户是否可以提交订单
	 */
	private void checkOrderInfo() {
		String username = MyThreadLocal.get().getSearchPassengerName().get(0);
		JSONObject userObj = MyThreadLocal.get().getPassenger().get(username);
		try {
			VHttpPost post = new VHttpPost(URLUtils.getCheckOrderInfo().currentURL());
			VParames parames = new VParames();
			parames.clear();
			parames.put("cancel_flag", "2");
			parames.put("bed_level_order_num", "000000000000000000000000000000");
			parames.put("passengerTicketStr",passengerTicketStr);
			parames.put("oldPassengerStr", userObj.getString("passenger_name")
					+ ",1," + userObj.getString("passenger_id_no") + ",1_");
			parames.put("tour_flag", "dc");
			parames.put("randCode","");//submitCode
			parames.put("whatsSelect","1");
			parames.put("_json_att", "");
			parames.put("REPEAT_SUBMIT_TOKEN", MyThreadLocal.get().getToken());
			post.setParames(parames);
			VHttpResponse res = VBrowser.execute(post);
			String body = VHttpUtils.outHtml(res.getBody());
			JSONObject res_obj = new JSONObject(body);
			JSONObject dataobj = new JSONObject(res_obj.get("data").toString());
			if ("true".equals(dataobj.get("submitStatus").toString())) {
				LoggerUtils.printLog("当前用户可以提交订单");
				URLUtils.getCheckOrderInfo().OK();
				getQueueCount();
			} else {
				LoggerUtils.printLog(dataobj.get("errMsg").toString());
				return;
			}
			res.getEntity().disconnect();
		} catch (JSONException e) {
			e.printStackTrace();
			URLUtils.getCheckOrderInfo().isFail();
		}
	}

	/**
	 * 获取余票数量
	 */
	private void getQueueCount() {
		VHttpPost post = new VHttpPost(URLUtils.getCheckQueueCount().currentURL());
		VParames parames4 = new VParames();
		try {
			parames4.put("train_date", MyThreadLocal.get().getTrainDate());
			parames4.put("train_no", obj2.getString("train_no"));
			parames4.put("stationTrainCode", obj2.getString("station_train_code"));
			parames4.put("seatType", passengerTicketStr.substring(0, 1));
			parames4.put("fromStationTelecode", obj2.getString("from_station_telecode"));
			parames4.put("toStationTelecode", obj2.getString("to_station_telecode"));
			parames4.put("leftTicket", obj2.getString("yp_info"));
			parames4.put("purpose_codes", "00");
			parames4.put("train_location", obj2.getString("location_code"));
			parames4.put("_json_att", "");
			parames4.put("REPEAT_SUBMIT_TOKEN", MyThreadLocal.get().getToken());
			post.setParames(parames4);
			VHttpResponse res = VBrowser.execute(post);
			String body = VHttpUtils.outHtml(res.getBody());
			JSONObject jsonBody = new JSONObject(body);
			if ("true".equals(jsonBody.get("status").toString())) {
				JSONObject dataObj = (JSONObject) jsonBody.get("data");
				/*String[] counts = HttpUtils.getCountByJs(
						dataObj.get("ticket").toString(), passengerTicketStr.substring(0, 1)).split(",");*/
				if (dataObj.getInt("ticket") > 0) {
					LoggerUtils.printLog(obj2.get("station_train_code") + "："+HttpUtils.seatNumToseatType(passengerTicketStr.substring(0, 1))+"剩余:"
							+ dataObj.getInt("ticket") + "张");
				}
				LoggerUtils.printLog("开始提交订单");
				URLUtils.getCheckQueueCount().OK();
			} else {
				LoggerUtils.printLog(jsonBody.get("messages").toString());
			}
			res.getEntity().disconnect();
		} catch (Exception e) {
			LoggerUtils.printLog("解析余票数量失败，请联系作者QQ：372939956");
			URLUtils.getCheckQueueCount().isFail();
		}
		confirmSingleForQueue();
	}

	/**
	 * 确认提交订单
	 */
	private void confirmSingleForQueue() {
		String username = MyThreadLocal.get().getSearchPassengerName().get(0);
		JSONObject userObj = MyThreadLocal.get().getPassenger().get(username);

		try {
			VHttpPost post = new VHttpPost(URLUtils.getSubmitOrder().currentURL());
			VParames parames = new VParames();
			parames.clear();
			parames.put("passengerTicketStr",passengerTicketStr);
			parames.put("oldPassengerStr", userObj.getString("passenger_name")
					+ ",1," + userObj.getString("passenger_id_no") + ",1_");
			parames.put(
					"randCode","");//submitCode
			parames.put("purpose_codes", "00");
			parames.put("key_check_isChange", MyThreadLocal.get().getKeyCheckisChange());
			parames.put("leftTicketStr", obj2.getString("yp_info"));
			parames.put("train_location", obj2.getString("location_code"));
			parames.put("roomType", "00");
			parames.put("dwAll", "N");
			parames.put("_json_att", "");
			parames.put("REPEAT_SUBMIT_TOKEN", MyThreadLocal.get().getToken());
			post.setParames(parames);
			VHttpResponse res = VBrowser.execute(post);
			String body = VHttpUtils.outHtml(res.getBody());
			JSONObject res_obj = new JSONObject(body);
			JSONObject dataobj = new JSONObject(res_obj.get("data").toString());
			if ("true".equals(dataobj.get("submitStatus").toString())) {
				LoggerUtils.printLog("订单提交成功，正在查询订票结果");
				URLUtils.getSubmitOrder().OK();
				TrainInfo.isPoolRun = false;		//结束运行判断
				//记录url下标到文件

				queryOrderWaitTime();
			} else {
				LoggerUtils.printLog(body);
			}
			res.getEntity().disconnect();
		} catch (JSONException e) {
			e.printStackTrace();
			URLUtils.getSubmitOrder().isFail();
		}
	}

	/**
	 * 开始查询订单
	 */
	private void queryOrderWaitTime() {
		boolean order = true;
		String orderId = "";
		try {
			while (order) {
				Random ne = new Random();
				int x = ne.nextInt(9999 - 1000 + 1) + 1000;
				String query_url = URLUtils.getQueryOrderWaitTime().currentURL()
						+ "random=14772940" + x
						+ "&tourFlag=dc&_json_att=&REPEAT_SUBMIT_TOKEN="
						+ MyThreadLocal.get().getToken();
				VHttpGet get = new VHttpGet(query_url);
				VHttpResponse res = VBrowser.execute(get);
				String body = VHttpUtils.outHtml(res.getBody());
				JSONObject res_obj = new JSONObject(body);
				JSONObject dataobj = new JSONObject(res_obj.get("data")
						.toString());
				if (!"null".equals(dataobj.get("orderId").toString())) {
					order = false;
					orderId = dataobj.get("orderId").toString();
				}
			}
			URLUtils.getQueryOrderWaitTime().OK();
			try {
				EventUtils.newInstance().unregister(ConfigUtils.getInstace());
				EventUtils.newInstance().noticeAll("成功订票保存url");
			} catch (Exception e) {

			}
			LoggerUtils.printLog("恭喜你，成功订到一张"
					+ obj2.getString("from_station_name") + "至"
					+ obj2.getString("to_station_name") + "的"+HttpUtils.seatNumToseatType(passengerTicketStr.substring(0, 1))+"，订单号为：" + orderId
					+ "，请尽快付款，以免耽误行程");

		} catch (JSONException e) {
			LoggerUtils.printLog("：解析订票结果失败，请联系作者QQ：372939956");
			URLUtils.getQueryOrderWaitTime().isFail();
		}
	}

}
