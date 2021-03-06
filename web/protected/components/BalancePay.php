<?php
/**
 * @author sudk
 */
 
class BalancePay extends BasePay
{

    //订单支付
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
            $desc="余额支付成功，支付金额：".$orderAmount/100;
            TransRecord::Add($conn,$orderNumber,$trans_type,-$orderAmount,$serial_number,TransRecord::STATUS_SUCCESS,$re_serial_number="",$out_serial_number="",$user_id=$row['user_id'],$operator_id="");


            //如果是购买VIP则给用户生成VIP卡号
            if($row['type']==Order::TYPE_VIP){
                //$number=SysSetting::GetNewVipNumber();
                //User::AddVipNumber($conn,$number,$row['user_id']);
                User::AddVipNumber($conn,$row['user_id'],$orderAmount);
                $desc="购买VIP,支付成功，支付金额：".$orderAmount/100;
            }

            Order::ChangeDesc($conn,$orderNumber,$desc);

            $transaction->commit();

            //OrderLog::Add($orderNumber,$serial_number);
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

        $conn=Yii::app()->db;
        $transaction = $conn->beginTransaction();
        $serial_number=Utils::GenerateSerialNumber();

        $status=Order::STATUS_TOBE_CANCEL;


        try{
            Order::ChangeStatus($conn,$status,$orderNumber,$orderAmount);
            //返还金额
            $rs=User::Recharge($conn,$orderAmount,$order['user_id']);
            if($rs['status']!=0){
                $transaction->rollBack();
                return $rs;
            }

            $trans_type=TransRecord::GetCancelTypeByOrderType($order['type']);

            TransRecord::Add($conn,$orderNumber,$trans_type,$orderAmount,$serial_number,TransRecord::STATUS_SUCCESS,$re_serial_number="",$out_serial_number="",$user_id=$order['user_id'],$operator_id="");

            Order::ChangeDesc($conn,$orderNumber,"余额退款成功，退款金额：".$orderAmount/100);

            $transaction->commit();

            //OrderLog::Add($orderNumber,$serial_number);
            return array('status'=>0,'desc'=>'余额退款成功');

        }catch (Exception $e){

            $transaction->rollBack();
            return array('status'=>3,'desc'=>'失败');

        }
    }

}
