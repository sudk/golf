<?php
/**
 * @author sudk
 */
 
class UpmPay extends BasePay
{

    //订单推送请求
    public function Purchase($orderAmount,$orderDescription,$order_id){

        $pay_method=Order::PAY_METHOD_UPMP;
        //支付时内部订单和外部订单一样可以防止重复支付
        $out_order_number=Utils::GenerateOrderId();
        $req['version']     		= upmp_config::$version; // 版本号
        $req['charset']     		= upmp_config::$charset; // 字符编码
        $req['transType']   		= "01"; // 交易类型
        $req['merId']       		= upmp_config::$mer_id; // 商户代码
        $req['backEndUrl']      	= upmp_config::$mer_back_end_url; // 通知URL
        $req['frontEndUrl']     	= upmp_config::$mer_front_end_url; // 前台通知URL(可选)
        $req['orderDescription']	= $orderDescription;// 订单描述(可选)
        $req['orderTime']   		= date("YmdHis"); // 交易开始日期时间yyyyMMddHHmmss
        $req['orderNumber'] 		= $out_order_number; //订单号(商户根据自己需要生成订单号)
        $req['orderAmount'] 		= $orderAmount; // 订单金额
        $req['orderCurrency'] 		= "156"; // 交易币种(可选)
        $req['reqReserved']   	= $order_id;
        //$req['reqReserved']   	= UpmpService::buildReserved($merReserved); // 商户保留域(可选)

        $resp = array ();
        $validResp = UpmpService::trade($req, $resp);

        if($validResp){
            $respCode=$resp[upmp_config::RESPONSE_CODE];
            if("00"==$respCode){
                $conn=Yii::app()->db;
                $transaction = $conn->beginTransaction();
                try{

                    $rs=Order::ChangePayMethod($conn,$pay_method,$order_id);
                    $tn=$resp['tn'];
                    if($tn){
                        $trans_type=TransRecord::GetPayTypeByOrderType($rs['type']);
                        TransRecord::Add($conn,$order_id,$trans_type,-$orderAmount,$out_order_number,TransRecord::STATUS_PROCESS,$re_serial_number="",$out_serial_number="",$user_id="",$operator_id="",$out_order_number,$req['orderTime']);
                    }
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
    public function Void($order_id,$orderAmount,$qn){
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
        $req['reqReserved'] 	= $order_id; // 请求方保留域(可选，用于透传商户信息)

        // 保留域填充方法
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
    public function Refund($order_id,$orderAmount,$sn){
        $out_order_number=Utils::GenerateSerialNumber();
        //需要填入的部分
        $req['version']     	= upmp_config::$version; // 版本号
        $req['charset']     	= upmp_config::$charset; // 字符编码
        $req['transType']   	= "04"; // 交易类型
        $req['merId']       	= upmp_config::$mer_id; // 商户代码
        $req['backEndUrl']      = upmp_config::$mer_back_end_url; // 通知URL
        $req['orderTime']   	= date("YmdHis"); // 交易开始日期时间yyyyMMddHHmmss（退货交易新交易日期，非原交易日期）
        $req['orderNumber'] 	= $out_order_number; // 订单号（退货交易新订单号，非原交易订单号）
        $req['orderAmount'] 	= $orderAmount; // 订单金额
        $req['orderCurrency'] 	= "156"; // 交易币种(可选)
        $req['qn'] 				= $sn; // 查询流水号（原订单支付成功后获取的流水号）
        $req['reqReserved']   	= $order_id;
        // 保留域填充方法
        //$merReserved['order_id']   	= $order_id;
        //$req['reqReserved']   	= UpmpService::buildReserved($merReserved);

        $resp = array ();
        $validResp = UpmpService::trade($req, $resp);
        // 商户的业务逻辑
        if($validResp){
            if($resp[upmp_config::RESPONSE_CODE]==upmp_config::RESPONSE_CODE_SUCCESS){
                try{
                    $conn=Yii::app()->db;
                    $transaction = $conn->beginTransaction();
                    $desc="银联退款申请中，退款金额：".$orderAmount/100;
                    $rs=Order::ChangeStatus($conn,Order::STATUS_REFUND,$order_id);

                    $trans_type=TransRecord::GetCancelTypeByOrderType($rs['type']);
                    TransRecord::Add($conn,$order_id,$trans_type,$orderAmount,$out_order_number,TransRecord::STATUS_PROCESS,$re_serial_number="",$out_serial_number="",$user_id="",$operator_id="",$out_order_number,$req['orderTime']);
                    if($desc){
                        Order::ChangeDesc($conn,$order_id,$desc);
                    }
                    $transaction->commit();
                    return array('status'=>0,'desc'=>'已经成功向银行支付发起退款申请');

                }catch (Exception $e){
                    $transaction->rollBack();
                }
            }else{
                //print_r($resp);
                $respCode=$resp[upmp_config::RESPONSE_CODE];
                return array('status'=>$respCode,'msg'=>$resp[upmp_config::RESPONSE_MSG]);
            }
        }else{
            return array('status'=>20,'desc'=>'银行请求退款申请失败');
        }

    }

    public function Notice($params){
        Yii::log(json_encode($params),"info",'application.un_pay_log');
        if (UpmpService::verifySignature($params)){// 服务器签名验证成功
            if($params[upmp_config::RESPONSE_CODE]!=upmp_config::RESPONSE_CODE_SUCCESS){ echo "fail"; return false;}
            try{
                //请在这里加上商户的业务逻辑程序代码
                $transStatus = $params['transStatus'];// 交易状态
                $out_order_number = $params['orderNumber'];
                $order_id = $params['reqReserved'];
                $settleAmount = $params['settleAmount'];
                //$serial_number=Utils::GenerateSerialNumber();
                $qn=$params['qn'];
                $desc=false;
                $conn=Yii::app()->db;
                $transaction = $conn->beginTransaction();
                if (""!=$transStatus && upmp_config::TRANS_STATUS_SUCCESS==$transStatus){
                    // 交易处理成功
                    if($params['transType']==upmp_config::TRANS_TYPE_PURCHASE){
                        $desc="银联支付成功，支付金额：".$settleAmount/100;
                        $order=Order::ChangeStatus($conn,Order::STATUS_TOBE_SUCCESS,$order_id,$settleAmount);

                        //如果是购买VIP则给用户生成VIP卡号
                        if($order['type']==Order::TYPE_VIP){
                            //$number=SysSetting::GetNewVipNumber();
                            User::AddVipNumber($conn,$order['user_id'],$settleAmount);
                            $desc="银联支付,购买VIP,支付成功，支付金额：".$settleAmount/100;
                        }

                        //如果是充值则给用户余额账户充上金额
                        if($order['type']==Order::TYPE_RECHARGE){
                            User::Recharge($conn,$settleAmount,$order['user_id']);
                            $desc="银联支付，为账户余额充值，支付成功，支付金额：".$settleAmount/100;
                            $d=date("YmdHis");
                            TransRecord::Add($conn,$order_id,TransRecord::TRANS_TYPE_RECHARGE,$settleAmount,$out_order_number,TransRecord::STATUS_SUCCESS,$re_serial_number="",$out_serial_number=$qn,$user_id="",$operator_id="",$out_order_number,$d);
                        }

                    }
                    if($params['transType']==upmp_config::TRANS_TYPE_VOID){
                        $desc="银联消费撤销成功，金额：".$settleAmount/100;
                        Order::ChangeStatus($conn,Order::STATUS_TOBE_CANCEL,$order_id);

                    }
                    if($params['transType']==upmp_config::TRANS_TYPE_REFUND){
                        $desc="银联退款成功，金额：".$settleAmount/100;
                        Order::ChangeStatus($conn,Order::STATUS_TOBE_CANCEL,$order_id);
                    }
                    TransRecord::ChangeStatusByOutOrderNumber($conn,$order_id,$out_order_number,$qn,TransRecord::STATUS_SUCCESS);

                }else if(""!=$transStatus && upmp_config::TRANS_STATUS_FALSE==$transStatus) {

                    if($params['transType']==upmp_config::TRANS_TYPE_PURCHASE){
                        $desc="银联支付失败，支付金额：".$settleAmount/100;
                    }

                    if($params['transType']==upmp_config::TRANS_TYPE_VOID){
                        $desc="银联消费撤销失败，支付金额：".$settleAmount/100;
                    }

                    if($params['transType']==upmp_config::TRANS_TYPE_REFUND){

                        $desc="银联退款失败，支付金额：".$settleAmount/100;
                        Order::ChangeStatus($conn,Order::STATUS_WAIT_REFUND,$order_id);
                    }

                    TransRecord::ChangeStatusByOutOrderNumber($conn,$order_id,$out_order_number,$qn,TransRecord::STATUS_FALSE);
                }

                if($desc){
                    Order::ChangeDesc($conn,$order_id,$desc);
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
