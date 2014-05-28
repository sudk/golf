<?php
/**
 * @author sudk
 */
 
class BalancePay extends BasePay
{

    //订单推送请求
    public function Purchase($orderAmount,$orderDescription,$orderNumber){

        $conn=Yii::app()->db;
        $transaction = $conn->beginTransaction();
        $serial_number=Utils::GenerateSerialNumber();
        $pay_method=Order::PAY_METHOD_BALANCE;

        $status=Order::STATUS_TOBE_SUCCESS;

        $row=Order::OrderInfo($orderNumber);

        try{
            Order::ChangeStatus($conn,$status,$orderNumber,$orderAmount);
            Order::ChangePayMethod($conn,$pay_method,$orderNumber);
            //扣除余额
            $rs=User::Deduct($conn,$orderAmount,$row['user_id']);
            if($rs['status']!=0){
                $transaction->rollBack();
                return $rs;
            }

            $trans_type=TransRecord::GetPayTypeByOrderType($row['type']);

            TransRecord::Add($conn,$orderNumber,$trans_type,-$orderAmount,$serial_number,TransRecord::STATUS_SUCCESS,$re_serial_number="",$out_serial_number="",$user_id=Yii::app()->user->id,$operator_id="");
            Order::ChangeDesc($conn,$orderNumber,"余额支付成功，支付金额：".$orderAmount);
            $transaction->commit();

            OrderLog::Add($orderNumber,$serial_number);
            return array('status'=>0,'desc'=>'成功');

        }catch (Exception $e){

            $transaction->rollBack();
            return array('status'=>3,'desc'=>'失败');

        }
    }


    //消费撤销接口
    public function Void($orderNumber,$orderAmount,$sn){


    }

    //退货接口
    public function Refund($orderNumber,$orderAmount,$sn){
        $order=Order::OrderInfo($orderNumber);
//        if(!$order){
//            return array('status'=>5,'desc'=>'订单不存在！');
//        }
//        if($order['status']<Order::STATUS_TOBE_SUCCESS){
//            return array('status'=>6,'desc'=>'订单没有过支付不能退款！');
//        }
//        $trans_type=TransRecord::GetPayTypeByOrderType($order['type']);
//
//        $transRecord=TransRecord::GetBySerialNumber($qn,$trans_type);
//
//        if(!$transRecord){
//            return array('status'=>4,'desc'=>'没有任何的支付成功交易记录所以不能退款！');
//        }
//        $record_time=strtotime($transRecord['record_time']);
//        $now=time();
//        if(($now-$record_time)<86400){
//            return array('status'=>7,'desc'=>'支付成功后需要过24小时才能办理退款操作！');
//        }
//        if($orderAmount<0||$orderAmount>abs($transRecord['amount'])){
//            return array('status'=>7,'desc'=>'退款金额有误！');
//        }

        $conn=Yii::app()->db;
        $transaction = $conn->beginTransaction();
        $serial_number=Utils::GenerateSerialNumber();

        $status=Order::STATUS_TOBE_CANCEL;


        try{
            Order::ChangeStatus($conn,$status,$orderNumber,$orderAmount);
            //返还余额
            $rs=User::Recharge($conn,$orderAmount,$order['user_id']);
            if($rs['status']!=0){
                $transaction->rollBack();
                return $rs;
            }

            $trans_type=TransRecord::GetCancelTypeByOrderType($order['type']);

            TransRecord::Add($conn,$orderNumber,$trans_type,$orderAmount,$serial_number,TransRecord::STATUS_SUCCESS,$re_serial_number="",$out_serial_number="",$user_id="",$operator_id="");

            Order::ChangeDesc($conn,$orderNumber,"余额退款成功，退款金额：".$orderAmount);

            $transaction->commit();

            //OrderLog::Add($orderNumber,$serial_number);
            return array('status'=>0,'desc'=>'成功');

        }catch (Exception $e){

            $transaction->rollBack();
            return array('status'=>3,'desc'=>'失败');

        }
    }

}
