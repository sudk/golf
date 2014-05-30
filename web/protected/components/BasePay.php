<?php
/**
 * @author sudk
 */
 
class BasePay
{

    //订单推送请求
    public function Purchase($orderAmount,$orderDescription,$orderNumber){

    }


    //消费撤销接口
    public function Void($orderNumber,$orderAmount,$qn=""){


    }

    //退货接口
    public function Refund($orderNumber,$orderAmount,$qn=""){

    }

    public function OrderInfo($orderNumber){
        return Yii::app()->db->createCommand()
            ->select("*")
            ->from("g_order")
            ->where("order_id=:order_id",array("order_id"=>$orderNumber))
            ->queryRow();
    }

}
