<?php
/**
 * @author sudk
 */
 
class UpmPay extends BasePay
{

    //订单推送请求
    public function Purchase($orderAmount,$orderDescription,$orderNumber){

        //$serial_number=Utils::GenerateSerialNumber();
        $pay_method=Order::PAY_METHOD_UPMP;
        //$status=Order::STATUS_TOBE_SUCCESS;
        //$row=Order::OrderInfo($orderNumber);

        $req['version']     		= upmp_config::$version; // 版本号
        $req['charset']     		= upmp_config::$charset; // 字符编码
        $req['transType']   		= "01"; // 交易类型
        $req['merId']       		= upmp_config::$mer_id; // 商户代码
        $req['backEndUrl']      	= upmp_config::$mer_back_end_url; // 通知URL
        $req['frontEndUrl']     	= upmp_config::$mer_front_end_url; // 前台通知URL(可选)
        $req['orderDescription']	= $orderDescription;// 订单描述(可选)
        $req['orderTime']   		= date("YmdHis"); // 交易开始日期时间yyyyMMddHHmmss
        $req['orderNumber'] 		= $orderNumber; //订单号(商户根据自己需要生成订单号)
        $req['orderAmount'] 		= $orderAmount; // 订单金额
        $req['orderCurrency'] 		= "156"; // 交易币种(可选)

        $resp = array ();
        $validResp = UpmpService::trade($req, $resp);

        if($validResp){
            $respCode=$resp[upmp_config::RESPONSE_CODE];
            if("00"==$respCode){
                $conn=Yii::app()->db;
                try{
                    $transaction = $conn->beginTransaction();
                    //Order::ChangeStatus($conn,$status,$orderNumber);
                    Order::ChangePayMethod($conn,$pay_method,$orderNumber);
                    //$trans_type=TransRecord::GetPayTypeByOrderType($row['type']);
                    $tn=$resp['tn'];
                    //TransRecord::Add($conn,$orderNumber,$trans_type,-$orderAmount,$serial_number,TransRecord::STATUS_PROCESS,$re_serial_number="",$tn,$user_id=Yii::app()->user->id,$operator_id="");
                    $transaction->commit();
                    $data[]=array('tn'=>$tn);
                    return array('status'=>0,'desc'=>'成功','data'=>$data);
                }catch (Exception $e){
                    $transaction->rollBack();
                    return array('status'=>3,'desc'=>'失败');;
                }

            }else{
                $msg=array('status'=>$respCode,'msg'=>$resp[upmp_config::RESPONSE_MSG]);
            }
        }else{
            $msg=array('status'=>'1','msg'=>"签名认证失败");
        }
        return $msg;
    }

    //交易信息查询
    public function QueryInfo($type,$orderNumber){
        //需要填入的部分
        $req['version']     	= upmp_config::$version; // 版本号
        $req['charset']     	= upmp_config::$charset; // 字符编码
        $req['transType']   	= $type; // 交易类型
        $req['merId']       	= upmp_config::$mer_id; // 商户代码
        $req['orderTime']   	= date("Ymd"); // 交易开始日期时间yyyyMMddHHmmss或yyyyMMdd
        $req['orderNumber'] 	= $orderNumber; // 订单号

//        // 保留域填充方法
//        $merReserved['test']   	= "test";
//        $req['merReserved']   	= UpmpService::buildReserved($merReserved); // 商户保留域(可选)

        $resp = array ();
        $validResp = UpmpService::query($req, $resp);
        if($validResp){
            $this->Notice($resp);
        }
        print_r($resp);
    }

    //消费撤销接口
    public function Void($orderNumber,$orderAmount,$qn){
        //需要填入的部分
        $req['version']     	= upmp_config::$version; // 版本号
        $req['charset']     	= upmp_config::$charset; // 字符编码
        $req['transType']   	= upmp_config::TRANS_TYPE_VOID; // 交易类型
        $req['merId']       	= upmp_config::$mer_id; // 商户代码
        $req['backEndUrl']      = upmp_config::$mer_back_end_url; // 通知URL
        $req['orderTime']   	= date("YmdHis"); // 交易开始日期时间yyyyMMddHHmmss（撤销交易新交易日期，非原交易日期）
        $req['orderNumber'] 	= date("YmdHiss"); // 订单号（撤销交易新订单号，非原订单号）
        $req['orderAmount'] 	= $orderAmount; // 订单金额
        $req['orderCurrency'] 	= "156"; // 交易币种(可选)
        $req['qn'] 				= $qn; // 查询流水号（原订单支付成功后获取的流水号）
        $req['reqReserved'] 	= $orderNumber; // 请求方保留域(可选，用于透传商户信息)

//// 保留域填充方法
//        $merReserved['test']   	= "test";
//        $req['merReserved']   	= UpmpService::buildReserved($merReserved); // 商户保留域(可选)

        $resp = array ();
        $validResp = UpmpService::trade($req, $resp);
        if($validResp){
            return array('status'=>0,'desc'=>'成功');
        }else{
            $respCode=$resp[upmp_config::RESPONSE_CODE];
            return array('status'=>$respCode,'msg'=>$resp[upmp_config::RESPONSE_MSG]);
        }

    }

    //退货接口
    public function Refund($orderNumber,$orderAmount,$qn){
        //需要填入的部分
        $req['version']     	= upmp_config::$version; // 版本号
        $req['charset']     	= upmp_config::$charset; // 字符编码
        $req['transType']   	= "04"; // 交易类型
        $req['merId']       	= upmp_config::$mer_id; // 商户代码
        $req['backEndUrl']      = upmp_config::$mer_back_end_url; // 通知URL
        $req['orderTime']   	= date("YmdHis"); // 交易开始日期时间yyyyMMddHHmmss（退货交易新交易日期，非原交易日期）
        $req['orderNumber'] 	= date("YmdHiss"); // 订单号（退货交易新订单号，非原交易订单号）
        $req['orderAmount'] 	= $orderAmount; // 订单金额
        $req['orderCurrency'] 	= "156"; // 交易币种(可选)
        $req['qn'] 				= $qn; // 查询流水号（原订单支付成功后获取的流水号）
        $req['reqReserved'] 	= $orderNumber; // 请求方保留域(可选，用于透传商户信息)
//
//// 保留域填充方法
//        $merReserved['test']   	= "test";
//        $req['merReserved']   	= UpmpService::buildReserved($merReserved); // 商户保留域(可选)

        $resp = array ();
        $validResp = UpmpService::trade($req, $resp);

// 商户的业务逻辑
        if($validResp){
            return array('status'=>0,'desc'=>'成功');
        }else{
            $respCode=$resp[upmp_config::RESPONSE_CODE];
            return array('status'=>$respCode,'msg'=>$resp[upmp_config::RESPONSE_MSG]);
        }

    }

    public function Notice($params){
        Yii::log(json_encode($params),"debug",'application');
        if (UpmpService::verifySignature($params)){// 服务器签名验证成功
            if($params[upmp_config::RESPONSE_CODE]!=upmp_config::RESPONSE_CODE_SUCCESS){ echo "fail"; return false;}

            $conn=Yii::app()->db;
            try{
                //请在这里加上商户的业务逻辑程序代码
                $transStatus = $params['transStatus'];// 交易状态
                $orderNumber = $params['orderNumber'];
                $settleAmount = $params['settleAmount'];
                $serial_number=Utils::GenerateSerialNumber();
                $qn=$params['qn'];
                $transaction = $conn->beginTransaction();
                if (""!=$transStatus && upmp_config::TRANS_STATUS_SUCCESS==$transStatus){
                    // 交易处理成功
                    if($params['transType']==upmp_config::TRANS_TYPE_PURCHASE){
                        //消费
                        $rs=Order::ChangeStatus($conn,Order::STATUS_TOBE_SUCCESS,$orderNumber,$settleAmount);
                        if($rs){
                            $trans_type=TransRecord::GetPayTypeByOrderType($rs['type']);
                            TransRecord::Add($conn,$orderNumber,$trans_type,-$settleAmount,$serial_number,TransRecord::STATUS_SUCCESS,$re_serial_number="",$qn,$user_id="",$operator_id="");
                        }
                    }
                    if($params['transType']==upmp_config::TRANS_TYPE_VOID){
                        $orderNumber = $params['reqReserved'];
                        //消费撤销
                        $rs=Order::ChangeStatus($conn,Order::STATUS_TOBE_CANCEL,$orderNumber);
                        if($rs){
                            $trans_type=TransRecord::GetCancelTypeByOrderType($rs['type']);
                            TransRecord::Add($conn,$orderNumber,$trans_type,$settleAmount,$serial_number,TransRecord::STATUS_SUCCESS,$re_serial_number="",$qn,$user_id="",$operator_id="");
                        }
                    }
                    if($params['transType']==upmp_config::TRANS_TYPE_REFUND){
                        $orderNumber = $params['reqReserved'];
                        //退货
                        $rs=Order::ChangeStatus($conn,Order::STATUS_TOBE_CANCEL,$orderNumber);
                        if($rs){
                            $trans_type=TransRecord::GetCancelTypeByOrderType($rs['type']);
                            TransRecord::Add($conn,$orderNumber,$trans_type,$settleAmount,$serial_number,TransRecord::STATUS_SUCCESS,$re_serial_number="",$qn,$user_id="",$operator_id="");
                        }

                    }

                }else if(""!=$transStatus && upmp_config::TRANS_STATUS_FALSE==$transStatus) {

                    //$order=Order::OrderInfo($orderNumber);
                    // 交易处理失败
                    if($params['transType']==upmp_config::TRANS_TYPE_PURCHASE){
                        $model=Order::OrderInfo($orderNumber);
                        $trans_type=TransRecord::GetPayTypeByOrderType($model['type']);
                        TransRecord::Add($conn,$orderNumber,$trans_type,-$settleAmount,$serial_number,TransRecord::STATUS_FALSE,$re_serial_number="",$qn,$user_id="",$operator_id="");
                    }
                    if($params['transType']==upmp_config::TRANS_TYPE_VOID){
                        $orderNumber = $params['reqReserved'];
                        //消费撤销失败
                        $model=Order::OrderInfo($orderNumber);
                        $trans_type=TransRecord::GetCancelTypeByOrderType($model['type']);
                        TransRecord::Add($conn,$orderNumber,$trans_type,$settleAmount,$serial_number,TransRecord::STATUS_FALSE,$re_serial_number="",$qn,$user_id="",$operator_id="");
                    }
                    if($params['transType']==upmp_config::TRANS_TYPE_REFUND){
                        $orderNumber = $params['reqReserved'];
                        //退货失败
                        $model=Order::OrderInfo($orderNumber);
                        $trans_type=TransRecord::GetCancelTypeByOrderType($model['type']);
                        TransRecord::Add($conn,$orderNumber,$trans_type,$settleAmount,$serial_number,TransRecord::STATUS_FALSE,$re_serial_number="",$qn,$user_id="",$operator_id="");
                    }
                }
                $transaction->commit();
            }catch (Exception $e){
                $transaction->rollBack();
            }
            echo "success";
        } else {
            // 服务器签名验证失败
            echo "fail";
        }
    }

}
