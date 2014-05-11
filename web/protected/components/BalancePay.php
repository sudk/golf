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

        $row=$this->OrderInfo($orderNumber);

        try{
            $sql = "update g_order set status=:status,had_pay=:had_pay,pay_method=:pay_method where order_id=:order_id";
            $command = $conn->createCommand($sql);
            $command->bindParam(":status",$status, PDO::PARAM_STR);
            $command->bindParam(":had_pay",$orderAmount, PDO::PARAM_STR);
            $command->bindParam(":pay_method",$pay_method, PDO::PARAM_STR);
            $command->bindParam(":order_id",$orderNumber, PDO::PARAM_STR);
            $command->execute();

            //扣除余额
            $rs=User::Deduct($conn,$orderAmount);
            if($rs['status']!=0){
                $transaction->rollBack();
                return $rs;
            }

            $trans_type=TransRecord::TYPE_COURT_PAY;
            switch($row['type']){
                case Order::TYPE_COURT: $trans_type=TransRecord::TYPE_COURT_PAY;break;
                case Order::TYPE_COMPETITION: $trans_type=TransRecord::TYPE_COMPETITION_PAY;break;
                case Order::TYPE_TRIP: $trans_type=TransRecord::TYPE_TRIP_PAY;break;
            }

            TransRecord::Add($conn,$orderNumber,$trans_type,-$orderAmount,$serial_number,TransRecord::STATUS_SUCCESS,$re_serial_number="",$out_serial_number="",$user_id=Yii::app()->user->id,$operator_id="");

            $transaction->commit();

            OrderLog::Add($orderNumber,$serial_number);

            return array('status'=>0,'desc'=>'成功');
        }catch (Exception $e){
            $transaction->rollBack();
            return array('status'=>3,'desc'=>'失败');;
        }
    }


    //消费撤销接口
    public function Void($orderNumber,$orderAmount,$qn=""){


    }

    //退货接口
    public function Refund($orderNumber,$orderAmount,$qn=""){

    }

}
