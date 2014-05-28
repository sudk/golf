<?php

/**
 * 赛事
 * @author sudk
 */
class OrderController extends CMDBaseController
{


    public function accessRules() {
        return array(
            array('allow',
                'users' => array('@'),
            ),
            array('allow',
                'users' => array('*'),
            ),
            array('deny',
                'actions' => array(),
            ),
        );
    }
    public function beforeAction($action)
    {
        if(Yii::app()->user->isGuest){
            $msg['status']=-1;
            $msg['desc']="用户未登陆！";
            echo json_encode($msg);
            return false;
        }else{
            return true;
        }
    }

    public function actionList(){
        if(Yii::app()->command->cmdObj->_pg_==null||Yii::app()->command->cmdObj->_pg_==""){
            $msg['status']=1;
            $msg['desc']="分页参数不能为空！";
            echo json_encode($msg);
            return;
        }
        //echo Yii::app()->command->cmdObj->_pg_[1];
        $rs=Order::Order_list(Yii::app()->command->cmdObj->_pg_,$this->pageSize,Yii::app()->command->cmdObj);
        if($rs['rows']){
            $msg['status']=0;
            $msg['desc']="成功";
            $msg['_pg_']=Yii::app()->command->cmdObj->_pg_;
            $msg['data']=$rs['rows'];
        }else{
            $msg['status']=4;
            $msg['desc']="没有数据";
        }
        echo json_encode($msg);
        return;
    }

    public function actionInfo(){
        if(!Yii::app()->command->cmdObj->order_id){
            $msg['status']=1;
            $msg['desc']="ID不能为空！";
            echo json_encode($msg);
            return;
        }
        $row=Order::Info(Yii::app()->command->cmdObj->order_id);
        if($row){
            $msg['status']=0;
            $msg['desc']="成功";
            $msg['data']=$row;
        }else{
            $msg['status']=4;
            $msg['desc']="没有数据";
        }
        echo json_encode($msg);
        return;
    }

    public function actionCreate(){
        if(!isset(Yii::app()->command->cmdObj->type)||Yii::app()->command->cmdObj->type==''){
            $msg['status']=1;
            $msg['desc']="订单类型不能为空！";
            echo json_encode($msg);
            return;
        }
        if(!isset(Yii::app()->command->cmdObj->relation_id)||Yii::app()->command->cmdObj->relation_id==''){
            $msg['status']=2;
            $msg['desc']="关联ID不能为空！";
            echo json_encode($msg);
            return;
        }
        if(!Yii::app()->command->cmdObj->relation_name){
            $msg['status']=3;
            $msg['desc']="项目名称不能为空！";
            echo json_encode($msg);
            return;
        }
        if(!Yii::app()->command->cmdObj->tee_time){
            $msg['status']=5;
            $msg['desc']="打球时间不能为空！";
            echo json_encode($msg);
            return;
        }
        if(!Yii::app()->command->cmdObj->count){
            $msg['status']=6;
            $msg['desc']="数量不能为空！";
            echo json_encode($msg);
            return;
        }
        if(!Yii::app()->command->cmdObj->unitprice){
            $msg['status']=7;
            $msg['desc']="单价不能为空！";
            echo json_encode($msg);
            return;
        }
        if(!Yii::app()->command->cmdObj->amount){
            $msg['status']=8;
            $msg['desc']="购买数量不能为空！";
            echo json_encode($msg);
            return;
        }
        if(!isset(Yii::app()->command->cmdObj->pay_type)||Yii::app()->command->cmdObj->pay_type==''){
            $msg['status']=9;
            $msg['desc']="支付类型不能为空！";
            echo json_encode($msg);
            return;
        }
        if(!Yii::app()->command->cmdObj->contact){
            $msg['status']=10;
            $msg['desc']="联系人不能为空！";
            echo json_encode($msg);
            return;
        }
        if(!Yii::app()->command->cmdObj->phone){
            $msg['status']=11;
            $msg['desc']="电话不能为空！";
            echo json_encode($msg);
            return;
        }
        if(!Yii::app()->command->cmdObj->agent_id){
            $msg['status']=12;
            $msg['desc']="代理商ID不能为空！";
            echo json_encode($msg);
            return;
        }
        $row=Order::Create(Yii::app()->command->cmdObj);
        if($row){
            $msg['status']=0;
            $msg['desc']="成功";
            $msg['data']=array(array('order_id'=>$row));
        }else{
            $msg['status']=4;
            $msg['desc']="没有数据";
        }
        echo json_encode($msg);
        return;
    }

    public function actionCancel(){
        if(!Yii::app()->command->cmdObj->order_id){
            $msg['status']=1;
            $msg['desc']="ID不能为空！";
            echo json_encode($msg);
            return;
        }
        $row=Order::Cancel(Yii::app()->command->cmdObj->order_id);
        if($row){
            $msg['status']=0;
            $msg['desc']="成功";
            $msg['data']=$row;
        }else{
            $msg['status']=4;
            $msg['desc']="没有数据";
        }
        echo json_encode($msg);
        return;
    }

    public function actionPay(){
        if(!isset(Yii::app()->command->cmdObj->type)||Yii::app()->command->cmdObj->type==''){
            $msg['status']=1;
            $msg['desc']="支付类型不能为空！";
            echo json_encode($msg);
            return;
        }
        if(!isset(Yii::app()->command->cmdObj->order_id)||Yii::app()->command->cmdObj->order_id==''){
            $msg['status']=2;
            $msg['desc']="订单ID不能为空！";
            echo json_encode($msg);
            return;
        }
        if(!isset(Yii::app()->command->cmdObj->amount)||Yii::app()->command->cmdObj->amount==''){
            $msg['status']=3;
            $msg['desc']="支付金额不能为空！";
            echo json_encode($msg);

        }

        $rs=Order::Pay(Yii::app()->command->cmdObj->order_id,Yii::app()->command->cmdObj->type,Yii::app()->command->cmdObj->amount);
        echo json_encode($rs);
        return;

    }

    public function actionApplyrefund(){
        if(!isset(Yii::app()->command->cmdObj->order_id)||Yii::app()->command->cmdObj->order_id==''){
            $msg['status']=2;
            $msg['desc']="订单ID不能为空！";
            echo json_encode($msg);
            return;
        }
        if(!isset(Yii::app()->command->cmdObj->desc)||Yii::app()->command->cmdObj->desc==''){
            $msg['status']=4;
            $msg['desc']="请填写退款理由！";
            echo json_encode($msg);
            return;
        }
        $order=Order::Info(Yii::app()->command->cmdObj->order_id);
        $status=$order['status'];
        if($status!=Order::STATUS_TOBE_SUCCESS){
            $msg['status']=5;
            $msg['desc']="当前状态不允许申请退款！";
            echo json_encode($msg);
            return;
        }
        $conn=Yii::app()->db;
        $transaction = $conn->beginTransaction();
        $status=Order::STATUS_WAIT_REFUND;
        try{
            Order::ChangeStatus($conn,$status,Yii::app()->command->cmdObj->order_id);
            Order::ChangeDesc($conn,Yii::app()->command->cmdObj->order_id,Yii::app()->command->cmdObj->desc);
            $transaction->commit();
            //OrderLog::Add($orderNumber,$serial_number);
            //return array('status'=>0,'desc'=>'成功');
            $msg['status']=0;
            $msg['desc']="退款申请成功！";
            echo json_encode($msg);

        }catch (Exception $e){

            $transaction->rollBack();
            $msg['status']=3;
            $msg['desc']="失败！";
            echo json_encode($msg);

        }
    }

}