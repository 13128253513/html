package com.vcode.ticket.utils;

import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.vcode.ticket.events.CustomEvent;
import com.vcode.ticket.methods.URLManageEnum;
import com.vcode.ticket.methods.URLUtils;
import javax.xml.bind.JAXBException;
import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Copyright (C), 2018-2018, 友和道通实业有限公司
 * FileName: XmlUtils
 * Author:   wangpeng.sy
 * Date:     2018/12/25 17:07
 * Description: ${DESCRIPTION}
 * History:
 */
public class FileHelper implements CustomEvent{


    public static <T> T readJsonData(String fileName,TypeReference<T> type) {
        // 读取文件数据
        //System.out.println("读取文件数据util");

        StringBuffer strbuffer = new StringBuffer();
        File myFile = new File(fileName);//"D:"+File.separatorChar+"DStores.json"
        if (!myFile.exists()) {
            System.err.println("Can't Find " + fileName);
        }
        try {
            FileInputStream fis = new FileInputStream(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(fis, "UTF-8");
            BufferedReader in = new BufferedReader(inputStreamReader);

            String str;
            while ((str = in.readLine()) != null) {
                strbuffer.append(str);  //new String(str,"UTF-8")
            }
            in.close();
        } catch (IOException e) {
            e.getStackTrace();
        }
        //System.out.println("读取文件结束util");
        return com.alibaba.fastjson.JSONObject.parseObject(strbuffer.toString(), type);
    }

    // 把json格式的字符串写到文件
    public static boolean writeFile(String filePath, Object obj) {
        FileWriter fw;
        try {
            fw = new FileWriter(filePath);
            PrintWriter out = new PrintWriter(fw);
            out.write(com.alibaba.fastjson.JSONObject.toJSONString(obj, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat));
            out.println();
            fw.close();
            out.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public void onChange(String message) {
        System.out.println(message);
        //缓存数据保存到文件
        FileHelper.writeFile(URLUtils.class.getResource("/urlConfig.txt").getFile(),URLUtils.getURLCache());
    }

    public static void main(String[] args) throws JAXBException, IOException {
        Map<String,URLIndex> initData=new LinkedHashMap<>();
        initData.put("verificationCode",new URLIndex(URLManageEnum.verification_code));
        initData.put("checkCode",new URLIndex(URLManageEnum.check_code));
        initData.put("login",new URLIndex(URLManageEnum.login));
        initData.put("checkLogin",new URLIndex(URLManageEnum.check_login));
        initData.put("initHtml",new URLIndex(URLManageEnum.init_html));
        initData.put("initBusPeople",new URLIndex(URLManageEnum.init_bus_people));
        initData.put("checkOrderInfo",new URLIndex(URLManageEnum.check_order_info));
        initData.put("checkQueueCount",new URLIndex(URLManageEnum.check_queue_count));
        initData.put("submitOrder",new URLIndex(URLManageEnum.submit_order));
        initData.put("queryOrderWaitTime",new URLIndex(URLManageEnum.query_order_wait_time));
        initData.put("queryOrder",new URLIndex(URLManageEnum.query_order));
        //FileHelper.writeFile("E:\\V12306-master\\http\\src\\main\\resources\\urlConfig.txt",initData);
        FileHelper.readJsonData(URLUtils.class.getResource("/urlConfig.txt").getFile(), new TypeReference<HashMap<String,URLIndex>>(){});
    }

}
