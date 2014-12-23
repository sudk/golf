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
    public function Void($orderNumber,$orderAmount,$sn){


    }

    //退货接口
    public function Refund($orderNumber,$orderAmount,$sn){

    }
}
