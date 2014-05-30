package com.unionpay.upmp.sdk.examples;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.unionpay.upmp.sdk.conf.UpmpConfig;
import com.unionpay.upmp.sdk.service.UpmpService;

/**
 * 类名：订单推送请求接口实例类文件
 * 功能：订单推送请求接口实例
 * 版本：1.0
 * 日期：2012-10-11
 * 作者：中国银联UPMP团队
 * 版权：中国银联
 * 说明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己的需要，按照技术文档编写,并非一定要使用该代码。该代码仅供参考。
 * */
public class PurchaseExample{
    
    public static void main(String[] args){
        // 请求要素
        Map<String, String> req = new HashMap<String, String>();
        req.put("backEndUrl", UpmpConfig.MER_BACK_END_URL);// 通知URL
        req.put("charset", UpmpConfig.CHARSET);// 字符编码
        req.put("transType", "01");// 交易类型
        req.put("frontEndUrl", UpmpConfig.MER_FRONT_END_URL);// 前台通知URL
        req.put("merId", UpmpConfig.MER_ID);// 商户代码
        req.put("merReserved", "");// 商户保留域
        req.put("orderAmount", "1");// 订单金额
        req.put("orderCurrency", "156");// 交易币种
        req.put("orderDescription", "订单描述");// 订单描述
        req.put("orderNumber", generateOrdrNo());// 订单号
        req.put("orderTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));// 交易时间
        req.put("orderTimeout", "20141201100000");// 订单超时时间
        req.put("sysReseverd", "");// 系统保留域
        req.put("version", UpmpConfig.VERSION);// 协议版本
        
        Map<String, String> resp = new HashMap<String, String>();
        boolean success = UpmpService.trade(req, resp);
        // 商户的业务逻辑
        if (success){
            System.out.println(resp);
        }else {
            // 错误应答码
            String respCode = resp.get(UpmpConfig.RESPONSE_CODE);
            // 错误提示信息
            String msg = resp.get(UpmpConfig.RESPONSE_MSG);
            System.out.println("respCode:" + respCode);
            System.out.println("msg:" + msg);
        }
    }
    
    /**
     * 订单号生成，该生产订单号仅用于测试，商户根据自己需要生成订单号
     * @return
     */
    public static String generateOrdrNo(){
        DateFormat formater = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        StringBuilder sb = new StringBuilder(formater.format(new Date()));
        return sb.toString();
    }
    
}
    