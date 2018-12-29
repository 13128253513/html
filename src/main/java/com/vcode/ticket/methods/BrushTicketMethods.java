package com.vcode.ticket.methods;

import javax.swing.table.DefaultTableModel;

import com.vcode.http.client.VHttpResponse;
import com.vcode.http.client.methods.VHttpGet;
import com.vcode.http.utils.VBrowser;
import com.vcode.http.utils.VHttpUtils;
import com.vcode.ticket.model.MyThreadLocal;
import com.vcode.ticket.model.TrainInfo;
import com.vcode.ticket.ui.HomePage;
import com.vcode.ticket.utils.LoggerUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BrushTicketMethods extends Thread{
	

	public BrushTicketMethods() {
	}

	@Override
	public void run() {
		int num = 0;
		do {
			num+=1;
			if (TrainInfo.isBrushTicket) {
				LoggerUtils.printLog("线程"+Thread.currentThread().getName()+"正在开始第"+num+"次查询");
			}
			brushVotes();
			/*try {
				Thread.sleep(Integer.parseInt(home_page.spinner.getValue().toString()));
			} catch (InterruptedException e) {
				Thread.interrupted();
				e.printStackTrace();
			};*/
		}while (TrainInfo.isPoolRun);//不管有没有票只要还在刷票就继续刷
	}
	
	/**
	 * 刷票，点击查票按钮后，开始查询车票信息，并填入到表格中
	 * 验证出发地、目的地、时间等是否填写，没有则不刷票
	 * 
	 */
	private void brushVotes() {
		// 开始刷票
		VHttpGet get = new VHttpGet(URLUtils.getInitTrainTicketInfo().currentURL() + MyThreadLocal.get().createQueryRequestParam());
		VHttpResponse res = VBrowser.execute(get);
		String body = VHttpUtils.outHtml(res.getBody());
		try {
			disposeTicketInfo(body,null);
		} catch (JSONException e) {
			URLUtils.getInitTrainTicketInfo().isFail();
			e.printStackTrace();
		}
	}
	
	/**
	 * 处理刷票返回信息
	 */
	private boolean disposeTicketInfo(String body,int[] seatOther) throws JSONException {
		boolean Brush = true;
		JSONObject json = new JSONObject(body);
		JSONObject data = new JSONObject(json.get("data").toString());
		String result = new JSONObject(json.get("data").toString()).get("result").toString();
		String rsMap = new JSONObject(json.get("data").toString()).get("map").toString();
		URLUtils.getInitTrainTicketInfo().OK();
		JSONObject jsMap = new JSONObject(rsMap);
		TrainInfo.stationCode=jsMap;
		JSONArray jsonArr = new JSONArray(result);

		for (int i = 0; i < jsonArr.length(); i++) {
			String[] tiketStr = jsonArr.get(i).toString().split("\\|");
			TrainInfo.trainInfoList.add(tiketStr);
			
			flag:
			if (TrainInfo.isBrushTicket && Brush) {
				//是否是当前线程需要查询的车次
				if (tiketStr[3].trim().equalsIgnoreCase(MyThreadLocal.get().getCurrentThreadTrain().trim())) {
					//判断选择的座席是否有票
					for (int seat : MyThreadLocal.get().getSearchSeat()) {
						if (!"--".equals(tiketStr[seat].trim()) &&
								!"无".equals(tiketStr[seat].trim()) &&
								!"".equals(tiketStr[seat].trim())) {
							//Brush = false;		//结束刷票结果判定

							//home_page.btnNewButton.setText("自动刷票");
							data.put("station_train_code",tiketStr[3]);//车次
							data.put("secretStr",tiketStr[0]);//加密的窜
							data.put("from_station_name",jsMap.getString(tiketStr[6]));//始发站
							data.put("to_station_name",jsMap.getString(tiketStr[7]));//到达站
							data.put("yp_info",tiketStr[12]);
							data.put("location_code",tiketStr[15]);
							data.put("train_no",tiketStr[2]);
							data.put("from_station_telecode",tiketStr[6]);
							data.put("to_station_telecode",tiketStr[7]);

							new HomeMethods(data).start();
							break flag;
						}
					}
				}
			}

		}
		if (!TrainInfo.isBrushTicket) {
			TrainInfo.isPoolRun = false;
		}
		//home_page.setTableSize();
		return TrainInfo.isPoolRun;
//		for (int i = 0; i < jsonArr.length(); i++) {
//			JSONObject obj = (JSONObject) jsonArr.get(i);
//			JSONObject obj2 = new JSONObject(obj.get("queryLeftNewDTO")
//					.toString());
//			obj2.put("secretStr", obj.get("secretStr").toString());
//			
//			flag:
//			if (home_page.ticket_type==1 && Brush) {
//				for (int j=0;j<home_page.model_train.getSize();j++) {
//					String train_no = home_page.model_train.get(j).toString();
//					if (obj2.get("station_train_code").toString().trim().equalsIgnoreCase(train_no.trim())) {
//						for (String seat : seatOther) {
//							if (!"--".equals(obj2.get(seat).toString().trim().toUpperCase())) {
//								Brush = false;		//结束刷票结果判定
//								home_page.result = false;		//结束循环刷票判定
//								home_page.isRun = false;		//结束运行判断
//								home_page.btnNewButton.setText("自动刷票");
//								new HomeMethods(home_page.window,obj2).start();
//								break flag;
//							}
//						}
//					}
//				}
//			}
	}
}
