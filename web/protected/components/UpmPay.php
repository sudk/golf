<?php
/**
 * @author sudk
 */
 
class UpmPay
{
    // 版本号
    const VERSION="";

    // 编码方式
    const CHARSET="UTF-8";

    // 交易网址
    const TRADE_URL="";

    // 查询网址
    const QUERY_URL="";

    // 商户代码
    const MER_ID="";

    // 通知URL
    const MER_BACK_END_URL="";

    // 前台通知URL
    const MER_FRONT_END_URL="";

    // 返回URL
    const MER_FRONT_RETURN_URL="";

    // 加密方式
    const SIGN_TYPE="";

    // 商城密匙，需要和银联商户网站上配置的一样
    const SECURITY_KEY="";

    // 成功应答码
    const RESPONSE_CODE_SUCCESS = "00";

    // 签名
    const SIGNATURE = "signature";

    // 签名方法
    const SIGN_METHOD = "signMethod";

    // 应答码
    const RESPONSE_CODE = "respCode";

    // 应答信息
    const RESPONSE_MSG = "respMsg";


    //解悉同步应答字符串
    public function parseQString($str){

    }

    //验证收到的消息
    public function verifySignature($params){

    }

    //构造签名
    public function buildSignature($params){

    }

    /**
     * 拼接请求字符串
     * @param req 请求要素
     * @param resp 应答要素
     * @return 请求字符串
     */
    public function buildReq($params){

    }

    public function track($params){

    }

    public function query($params){

    }

    //订单推送请求
    public function Purchase($orderAmount,$orderDescription,$orderNumber){
        $req['backEndUrl']=self::MER_BACK_END_URL;// 通知URL
        $req['charset']=self::CHARSET;// 字符编码
        $req["transType"]="01";// 交易类型
        $req["frontEndUrl"]=self::MER_FRONT_END_URL;// 前台通知URL
        $req['merId']=self::MER_ID;// 商户代码
        $req["merReserved"]="";// 商户保留域
        $req["orderAmount"]=$orderAmount;// 订单金额
        $req["orderCurrency"]="156";// 交易币种
        $req["orderDescription"]=$orderDescription;// 订单描述
        $req["orderNumber"]=$orderNumber;// 订单号
        $req["orderTime"]=date("YmdHis");// 交易时间
        $req["orderTimeout"]=date("YmdHis",strtotime("+1 hours"));// 订单超时时间
        $req["sysReseverd"]="";// 系统保留域
        $req["version"]=self::VERSION;// 协议版本

    }

    //交易信息查询
    public function QueryInfo($type,$orderNumber){
        $req["charset"]=self::CHARSET;// 字符编码
        $req["transType"]=$type;// 交易类型
        $req["merId"]=self::MER_ID;// 商户代码
        $req["merReserved"]="";// 商户保留域
        $req["orderNumber"]=$orderNumber;// 订单号
        $req["orderTime"]=date("YmdHis");// 交易时间
        $req["sysReseverd"]="";// 系统保留域
        $req["version"]=self::VERSION;// 协议版本
    }

    //消费撤销接口
    public function Void($orderNumber){
        $req["backEndUrl"]=self::MER_BACK_END_URL;// 通知URL
        $req["charset"]=self::CHARSET;// 字符编码
        $req["transType"]=04;// 交易类型
        $req["merId"]=self::MER_ID;// 商户代码
        $req["merReserved"]="";// 商户保留域
        $req["orderAmount"]="1";// 订单金额
        $req["orderCurrency"]="156";// 交易币种
        $req["orderNumber"]=$orderNumber;// 订单号
        $req["orderTime"]=date("YmdHis");// 交易时间
        $req["qn"]="";// 查询流水号
        $req["sysReseverd"]="";// 系统保留域
        $req["version"]=self::VERSION;// 协议版本
    }

    //退货接口
    public function Refund($orderNumber){
        $req["backEndUrl"]=self::MER_BACK_END_URL;// 通知URL
        $req["charset"]=self::CHARSET;// 字符编码
        $req["transType"]=04;// 交易类型
        $req["merId"]=self::MER_ID;// 商户代码
        $req["merReserved"]="";// 商户保留域
        $req["orderAmount"]="1";// 订单金额
        $req["orderCurrency"]="156";// 交易币种
        $req["orderNumber"]=$orderNumber;// 订单号
        $req["orderTime"]=date("YmdHis");// 交易时间
        $req["qn"]="";// 查询流水号
        $req["sysReseverd"]="";// 系统保留域
        $req["version"]=self::VERSION;// 协议版本
    }

}
