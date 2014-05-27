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

            $transaction->commit();

            OrderLog::Add($orderNumber,$serial_number);
            return array('status'=>0,'desc'=>'成功');

        }catch (Exception $e){

            $transaction->rollBack();
            return array('status'=>3,'desc'=>'失败');

        }
    }


    //消费撤销接口
    public function Void($orderNumber,$orderAmount,$qn){


    }

    //退货接口
    public function Refund($orderNumber,$orderAmount,$qn){
        $conn=Yii::app()->db;
        $transaction = $conn->beginTransaction();
        $serial_number=Utils::GenerateSerialNumber();

        $status=Order::STATUS_TOBE_CANCEL;

        $row=Order::OrderInfo($orderNumber);

        try{
            Order::ChangeStatus($conn,$status,$orderNumber,$orderAmount);
            //返还余额
            $rs=User::Recharge($conn,$orderAmount,$row['user_id']);
            if($rs['status']!=0){
                $transaction->rollBack();
                return $rs;
            }

            $trans_type=TransRecord::GetCancelTypeByOrderType($row['type']);

            TransRecord::Add($conn,$orderNumber,$trans_type,$orderAmount,$serial_number,TransRecord::STATUS_SUCCESS,$re_serial_number="",$out_serial_number="",$user_id="",$operator_id="");

            $transaction->commit();

            //OrderLog::Add($orderNumber,$serial_number);
            return array('status'=>0,'desc'=>'成功');

        }catch (Exception $e){

            $transaction->rollBack();
            return array('status'=>3,'desc'=>'失败');

        }
    }

}
