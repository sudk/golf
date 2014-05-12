<?php
/**
 * @author sudk
 */
 
class UpmPay extends BasePay
{
    // 版本号
    const VERSION="1.0.0";

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
    const SIGN_TYPE="MD5";

    // 商城密匙，需要和银联商户网站上配置的一样
    const SECURITY_KEY="";

    const TRANS_STATUS_SUCCESS="00";//交易成功
    const TRANS_STATUS_PROCESS="01";//处理中
    const TRANS_STATUS_FALSE="02";//交易失败

    // 成功应答码
    const RESPONSE_CODE_SUCCESS = "00";

    // 签名
    const SIGNATURE = "signature";

    // 签名方法
    const SIGN_METHOD = "signMethod";

    // 应答码
    const RESPONSE_CODE = "respCode";

    const RESPONSE_SUCCESS="00";

    // 应答信息
    const RESPONSE_MSG = "respMsg";


    //解悉同步应答字符串
    public function parseQString($str){
        $str_ar=explode("&",$str);
        $params=array();
        foreach($str_ar as $kv_str){
            list($k,$v)=explode("=",$kv_str);
            $params[$k]=$v;
        }
        if(self::verifySignature($params)){
            return $params;
        }else{
            return false;
        }
    }

    //验证收到的消息
    public function verifySignature($params){
        $params=self::removeEmpty($params);
        $signature=$params[self::SIGNATURE];
        $local_signature=self::buildSignature($params);
        return $signature==$local_signature;
    }

    //构造签名
    public function buildSignature($params){
        ksort($params);
        $str="";
        foreach($params as $k=>$v){
            if($k==self::SIGNATURE||$k==self::SIGN_METHOD){
                continue;
            }
            $str.="{$k}=".urldecode($v)."&";
        }
        $str.=md5(self::SECURITY_KEY);
        return md5($str);
    }


    public function buildReq($params){
        $params=self::removeEmpty($params);
        $signature=self::buildSignature($params);
        $params[self::SIGNATURE]=$signature;
        $params[self::SIGN_METHOD]=self::SIGN_TYPE;
        $str="";
        foreach($params as $k=>$v){
            $str.="{$k}=".urlencode($v)."&";
        }
        return substr($str,0,strlen($str)-1);
    }

    //去掉空值项
    public function removeEmpty($params){
        $tmp_ar=array();
        foreach($params as $k=>$v){
            if(!$v==""){
                $tmp_ar[$k]=$v;
            }
        }
        return $tmp_ar;
    }

    //交易请求
    public function track($params){
        $str=self::buildReq($params);
        $resp_str=self::HttpPos($str,self::TRADE_URL);
        return self::parseQString($resp_str);
    }

    //查询请求
    public function query($params){
        $str=self::buildReq($params);
        $resp_str=self::HttpPos($str,self::QUERY_URL);
        return self::parseQString($resp_str);
    }

    //订单推送请求
    public function Purchase($orderAmount,$orderDescription,$orderNumber){

        $serial_number=Utils::GenerateSerialNumber();
        $pay_method=Order::PAY_METHOD_UPMP;
        //$status=Order::STATUS_TOBE_SUCCESS;
        $row=Order::OrderInfo($orderNumber);

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
        $rs=self::track($req);
        if($rs){
            $respCode=$rs[self::RESPONSE_CODE];
            if($respCode==self::RESPONSE_CODE_SUCCESS){
                $conn=Yii::app()->db;
                try{
                    $transaction = $conn->beginTransaction();
                    //Order::ChangeStatus($conn,$status,$orderNumber);
                    Order::ChangePayMethod($conn,$pay_method,$orderNumber);
                    $trans_type=TransRecord::GetPayTypeByOrderType($row['type']);
                    $tn=$rs['tn'];
                    TransRecord::Add($conn,$orderNumber,$trans_type,-$orderAmount,$serial_number,TransRecord::STATUS_PROCESS,$re_serial_number="",$tn,$user_id=Yii::app()->user->id,$operator_id="");
                    $transaction->commit();
                    $data[]=array('tn'=>$tn);
                    return array('status'=>0,'desc'=>'成功','data'=>$data);
                }catch (Exception $e){
                    $transaction->rollBack();
                    return array('status'=>3,'desc'=>'失败');;
                }

            }else{
                $msg=array('status'=>$respCode,'msg'=>$rs[self::RESPONSE_MSG]);
            }
        }else{
            $msg=array('status'=>'1','msg'=>"请求银联接口失败");
        }
        return $msg;
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
        return self::query($req);

    }

    //消费撤销接口
    public function Void($orderNumber,$orderAmount,$qn){
        $req["backEndUrl"]=self::MER_BACK_END_URL;// 通知URL
        $req["charset"]=self::CHARSET;// 字符编码
        $req["transType"]="31";// 交易类型
        $req["merId"]=self::MER_ID;// 商户代码
        $req["merReserved"]="";// 商户保留域
        $req["orderAmount"]=$orderAmount;// 订单金额
        $req["orderCurrency"]="156";// 交易币种
        $req["orderNumber"]=$orderNumber;// 订单号
        $req["orderTime"]=date("YmdHis");// 交易时间
        $req["qn"]=$qn;// 查询流水号
        $req["sysReseverd"]="";// 系统保留域
        $req["version"]=self::VERSION;// 协议版本
        return self::track($req);

    }

    //退货接口
    public function Refund($orderNumber,$orderAmount,$qn){
        $req["backEndUrl"]=self::MER_BACK_END_URL;// 通知URL
        $req["charset"]=self::CHARSET;// 字符编码
        $req["transType"]="04";// 交易类型
        $req["merId"]=self::MER_ID;// 商户代码
        $req["merReserved"]="";// 商户保留域
        $req["orderAmount"]=$orderAmount;// 订单金额
        $req["orderCurrency"]="156";// 交易币种
        $req["orderNumber"]=$orderNumber;// 订单号
        $req["orderTime"]=date("YmdHis");// 交易时间
        $req["qn"]=$qn;// 查询流水号
        $req["sysReseverd"]="";// 系统保留域
        $req["version"]=self::VERSION;// 协议版本
        return self::track($req);

    }

    public function Notice($str){
        $params=self::parseQString($str);
        if($params){

        }else{
            return false;
        }
    }

    public function HttpPos($str,$url){
        $tuCurl = curl_init();
        curl_setopt($tuCurl, CURLOPT_URL,$url);
        //curl_setopt($tuCurl, CURLOPT_VERBOSE, 0);
        curl_setopt($tuCurl, CURLOPT_TIMEOUT,5);
        curl_setopt($tuCurl, CURLOPT_HEADER,false);
        curl_setopt($tuCurl, CURLOPT_RETURNTRANSFER,true);
        curl_setopt($tuCurl, CURLOPT_POST,true);
        curl_setopt($tuCurl, CURLOPT_POSTFIELDS,$str);
        //curl_setopt($tuCurl, CURLOPT_HTTPHEADER,array("Cookie: n8lca91gg1gdtk0n000uhkaq97"));
        //curl_setopt($tuCurl, CURLOPT_COOKIEFILE, '/Applications/XAMPP/htdocs/golf/web/assets/cookie.txt');
        //curl_setopt($tuCurl, CURLOPT_COOKIEJAR, '/Applications/XAMPP/htdocs/golf/web/assets/cookie.txt');
        $rs=curl_exec($tuCurl);
        //list($header, $data) = explode("\n\n",$rs,2);
        curl_close($tuCurl);

        return $rs;
    }


}
