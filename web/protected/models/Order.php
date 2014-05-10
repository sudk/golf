<?php
/**
 * --请填写模块名称--
 *
 * @author #guohao 
 * @copyright Copyright &copy; 2003-2009 TrunkBow Co., Inc
 */
class Order extends CActiveRecord {

   
    const TYPE_COURT = '0';
    const TYPE_TRIP = '1';
    const TYPE_COMPETITION = '2';
    
    
    /**
     * 状态流程
     * 0、等待确认；1、等待付款；2、付款完成；3、交易关闭 ，4-未到场  5-交易成功，6-等待退款 7-拒绝退款 8-退款中
     * 1）需要预付款的订单，需要完成上面的流程；无需预付款的，“等待确认”  到  “交易成功”
     * 2）在“等待确认”，“等待付款”时，客户可以随时 关闭交易。
     * 3）“付款完成”，客户想取消订单，只能【申请退款】，填写退款原因。
     * 4）客户未到场，则有 代理商 修改状态 “未到场”，可能需要执行一个返还部分款项的操作。订单结束
     * 5）客户打球完，代理商 修改状态 “交易成功”。订单结束
     * 6）客户申请退款，状态为“等待退款”。代理商审核通过，则执行退款流程，状态为“退款中”。退款完成，状态为“交易成功”。订单结束
     * 7）客户申请退款，代理商审核不通过，状态为“拒绝退款”。 订单结束。
     * 8)代理商 可以在任何时候 关闭订单。“交易关闭”，已经付款的，执行退款流程
     */
    const STATUS_TOBE_CONFIRM = '0';
    const STATUS_TOBE_PAID = '1';
    const STATUS_TOBE_SUCCESS = '2';
    const STATUS_TOBE_CANCEL = '3';
    const STATUS_NOT_PRESENT = '4';
    const STATUS_ORDER_OVER = '5';
    
    const STATUS_WAIT_REFUND = '6';
    CONST STATUS_REFUND = '8';
    CONST STATUS_REFUSE_REFUND = '7';
    
    public $court_phone;

    public static function model($className=__CLASS__){
        return parent::model($className);
    }

    public function tableName(){
        return 'g_order';
    }

    public function rules(){
        return array();
    }
    
    /**
     * 流程。
     * 根据pay_type
     * 先付的， 待确认，交易成功
     * 预付和押金的  
     * 0、等待确认；1、等待付款；2、付款完成；3、交易关闭 ，4-未到场  5-交易成功，6-等待退款 7-拒绝退款 8-退款中
     * @param type $cur_status
     * @param type $pay_type
     * @return boolean
     */
    public static function getNextStatus($cur_status='0',$pay_type='0')
    {
        if($cur_status==null)
        {
            return false;
        }
       
        $close_next = array(
            'now_status'=>'',
            'status'=>self::STATUS_TOBE_CANCEL,
            'desc'=>'交易关闭'
        );
        $next = array();
        if($pay_type == '0')
        {
         
            $tmp_next = array(
                'now_status'=>self::STATUS_TOBE_CONFIRM,
                'status'=>self::STATUS_ORDER_OVER,
                'desc'=>"交易成功"
            );
            array_push($next, $tmp_next);
            array_push($next,$close_next);
            return $next;
        }
        
        switch ($cur_status) {
            case self::STATUS_TOBE_CONFIRM:
                $tmp_next = array(
                    'now_status'=>self::STATUS_TOBE_CONFIRM,
                    'status'=>self::STATUS_TOBE_PAID,
                    'desc'=>"订单确认"
                );
                array_push($next, $tmp_next);
                $close_next['now_status'] = self::STATUS_TOBE_CONFIRM;
                array_push($next,$close_next);
                break;
            case self::STATUS_TOBE_PAID:
                
                $close_next['now_status'] = self::STATUS_TOBE_PAID;
                array_push($next,$close_next);
                break;
            case self::STATUS_TOBE_SUCCESS:
                //可以有三中情况。撤销，未到场，订单完成。
                $tmp_next = array(
                    'now_status'=>self::STATUS_TOBE_SUCCESS,
                    'status'=>self::STATUS_ORDER_OVER,
                    'desc'=>"交易成功"
                );
                array_push($next, $tmp_next);
                $tmp_next2 = array(
                    'now_status'=>self::STATUS_TOBE_SUCCESS,
                    'status'=>self::STATUS_NOT_PRESENT,
                    'desc'=>"未到场"
                );
                array_push($next, $tmp_next2);
                $close_next['now_status'] = self::STATUS_TOBE_SUCCESS;
                array_push($next,$close_next);
                break;
            case self::STATUS_WAIT_REFUND:
                $tmp_next = array(
                    'now_status'=>self::STATUS_WAIT_REFUND,
                    'status'=>self::STATUS_REFUND,
                    'desc'=>"退款"
                );
                array_push($next, $tmp_next);
                $tmp_next = array(
                    'now_status'=>self::STATUS_WAIT_REFUND,
                    'status'=>self::STATUS_REFUSE_REFUND,
                    'desc'=>"拒绝退款"
                );
                array_push($next, $tmp_next);
                //array_push($next,$close_next);
                break;
            case self::STATUS_REFUND:
                $tmp_next = array(
                    'now_status'=>self::STATUS_REFUND,
                    'status'=>self::STATUS_ORDER_OVER,
                    'desc'=>"交易成功"
                );
                array_push($next, $tmp_next);
                break;
            default:
                $next = array();
                break;
        }
        
        return $next;
    }
    
    /**
     * 订单类型，0、订场；1、行程；3、赛事报名；
     */
    public static function getOrderType($type=null)
    {
        $rs = array(
            self::TYPE_COURT=>'预约场地',
            self::TYPE_TRIP=>'预约行程',
            self::TYPE_COMPETITION=>'赛事报名',
        );
        
        return $type?$rs[$type]:$rs;
    }
    
    
    public static function getStatus($status=null)
    {
        $rs = array(
            self::STATUS_TOBE_CONFIRM=>'等待确认',
            self::STATUS_TOBE_PAID=>'等待支付',
            self::STATUS_TOBE_SUCCESS=>'付款成功',
            self::STATUS_TOBE_CANCEL=>'交易关闭',
            self::STATUS_NOT_PRESENT=>'未到场',
            self::STATUS_ORDER_OVER=>'交易成功',
            self::STATUS_WAIT_REFUND=>'等待退款',
            self::STATUS_REFUND=>'退款中',
            self::STATUS_REFUSE_REFUND=>'拒绝退款',
                
        );
        
        return $status? $rs[$status]:$rs;
    }
    
    /**
     * 支付方式：0为现付，1为全额预付，2为押金
     * @return type
     */
    public static function getPayType()
    {
        return array(
            '0'=>'现付',
            '1'=>'全额预付',
            '2'=>'押金'
        );
    }


    /**
     * 查询
     * @param int $page
     * @param int $pageSize
     * @param array $args
     * @return array
     */
    public static function queryList($page, $pageSize, $args = array()) {

        $condition = ' 1=1 ';
        $params = array();

        if ($args['order_id'] != ''){
            $condition.= ' AND order_id=:order_id';
            $params['order_id'] = $args['order_id'];
        }
        
        if ($args['court_id'] != ''){
            $condition.= ' AND court_id=:court_id';
            $params['court_id'] = $args['court_id'];
        }
        
        if ($args['agent_id'] != ''){
            $condition.= ' AND  agent_id=:agent_id';
            $params['agent_id'] = $args['agent_id'];
        }
        
        if($args['status'] != "")
        {
            $condition .= ' AND status=:status';
            $params['status'] = $args['status'];
        }
        
        if($args['pre_deal'] != "")
        {
            $condition .= ' AND status in (0,1)';
            
        }
        
        if($args['begin_date']!="" && $args['end_date']!="")
        {
            $condition .=" AND record_time >= '".$args['begin_date']." 00:00:00' AND record_time <= '".$args['end_date']." 23:59:59'";
            
        }
        
        
        
        $total_num = Order::model()->count($condition, $params); //总记录数

        $criteria = new CDbCriteria();
        $criteria->order = 'record_time  DESC';
    	

        $criteria->condition = $condition;
        $criteria->params = $params;

        $pages = new CPagination($total_num);
        $pages->pageSize = $pageSize;
        $pages->setCurrentPage($page);
        $pages->applyLimit($criteria);

        $rows = Order::model()->findAll($criteria);

        $rs['status'] = 0;
        $rs['desc'] = '成功';
        $rs['page_num'] = ($pages->currentPage + 1);
        $rs['total_num'] = $total_num;
        $rs['total_page'] = ceil($rs['total_num'] / $rs['page_num']);
        $rs['num_of_page'] = $pages->pageSize;
        $rs['rows'] = $rows;

        return $rs;
    }

    public static function Create($args){
        $conn=Yii::app()->db;
        $transaction = $conn->beginTransaction();
        $r=rand(100000,999999);
        $d=date("YmdHis");
        $order_id=$d.$r;
        $record_time=date("Y-m-d H:i:s");
        if(Yii::app()->user->isGuest){
            $user_id="guest";
        }else{
            $user_id=Yii::app()->user->id;
        }
        try{
            $had_pay=0;
            $status=Order::STATUS_TOBE_CONFIRM;
            $sql = "
                    insert into g_order
                    (order_id,user_id,`type`,relation_id,relation_name,tee_time,`count`,unitprice,amount,had_pay,pay_type,status,record_time,agent_id,contact,phone)
                     values
                    (:order_id,:user_id,:type,:relation_id,:relation_name,:tee_time,:count,:unitprice,:amount,:had_pay,:pay_type,:status,:record_time,:agent_id,:contact,:phone)
            ";
            $command = $conn->createCommand($sql);
            $command->bindParam(":order_id", $order_id,PDO::PARAM_STR);
            $command->bindParam(":user_id", $user_id,PDO::PARAM_STR);
            $command->bindParam(":type", $args->type,PDO::PARAM_STR);
            $command->bindParam(":relation_id", $args->relation_id, PDO::PARAM_STR);
            $command->bindParam(":relation_name", $args->relation_name, PDO::PARAM_STR);
            $command->bindParam(":tee_time",$args->tee_time,PDO::PARAM_STR);
            $command->bindParam(":count",$args->count,PDO::PARAM_STR);
            $command->bindParam(":unitprice", $args->unitprice, PDO::PARAM_STR);
            $command->bindParam(":amount",$args->amount,PDO::PARAM_STR);
            $command->bindParam(":had_pay",$had_pay,PDO::PARAM_STR);
            $command->bindParam(":pay_type",$args->pay_type,PDO::PARAM_STR);
            $command->bindParam(":status",$status,PDO::PARAM_STR);
            $command->bindParam(":record_time",$record_time,PDO::PARAM_STR);
            //$command->bindParam(":desc", $args->desc, PDO::PARAM_STR);
            $command->bindParam(":agent_id",$args->agent_id,PDO::PARAM_STR);
            $command->bindParam(":contact",$args->contact,PDO::PARAM_STR);
            $command->bindParam(":phone",$args->phone,PDO::PARAM_STR);
            $command->execute();
            $transaction->commit();

            OrderLog::Add($order_id,$order_id);

            return $order_id;

        }catch (Exception $e){
            Yii::log($e->getMessage(),'debug','application.firebuglog');
            $transaction->rollBack();
            return false;
        }
    }

    public static function Order_list($page,$pageSize,$args){
        $condition = ' 1=1 ';
        $params = array();

        if ($args->relation_name != ''){
            $condition.=' AND relation_name like :relation_name';
            $params['relation_name'] = "%".$args->relation_name."%";
        }

        if ($args->start_time != ''){
            $condition.=' AND record_time >= :start_time';
            $params['start_time'] = $args->start_time;
        }

        if ($args->end_time != ''){
            $condition.=' AND record_time <= :end_time';
            $params['end_time'] = $args->end_time;
        }

        $condition.=' AND user_id = :user_id';
        $params['user_id'] = Yii::app()->user->id;


        $total_num = Yii::app()->db->createCommand()
            ->select("count(1)")
            ->from("g_order")
            ->where($condition,$params)
            ->queryScalar();


        $order = 'record_time  DESC';

        $rows=Yii::app()->db->createCommand()
            ->select("*")
            ->from("g_order")
            ->where($condition,$params)
            ->order($order)
            ->limit($pageSize)
            ->offset($page * $pageSize)
            ->queryAll();

        $rs['status'] = 0;
        $rs['desc'] = '成功';
        $rs['page_num'] = $page;
        $rs['total_num'] = $total_num;
        $rs['total_page'] = ceil($total_num/$pageSize);
        $rs['num_of_page'] = $pageSize;
        $rs['rows'] = $rows;

        return $rs;
    }

    public static function Info($id) {

        $condition = ' 1=1 ';
        $params = array();


        if ($id != ''){
            $condition.=' AND order_id = :order_id';
            $params['order_id'] =$id;
        }else{
            return false;
        }

        $row=Yii::app()->db->createCommand()
            ->select("*")
            ->from("g_order")
            ->where($condition,$params)
            ->queryRow();
        return $row;

    }

    public static function Cancel($id){

        $conn=Yii::app()->db;
        $transaction = $conn->beginTransaction();
        $r=rand(100000,999999);
        $d=date("YmdHis");
        $serial_number=$d.$r;

        $status=Order::STATUS_TOBE_CANCEL;
        try{
            $sql = "update g_order set status=:status where order_id=:order_id";
            $command = $conn->createCommand($sql);
            $command->bindParam(":status",$status, PDO::PARAM_STR);
            $command->bindParam(":order_id",$id, PDO::PARAM_STR);
            $command->execute();
            $transaction->commit();

            OrderLog::Add($id,$serial_number);

            return true;
        }catch (Exception $e){
            $transaction->rollBack();
            return false;
        }
    }

    public static function Pay($order_id,$type,$amount){

        $conn=Yii::app()->db;
        $transaction = $conn->beginTransaction();
        $r=rand(100000,999999);
        $d=date("YmdHis");
        $serial_number=$d.$r;
        $status=Order::STATUS_TOBE_SUCCESS;

        $row=Yii::app()->db->createCommand()
            ->select("*")
            ->from("g_order")
            ->where("order_id=:order_id",array("order_id"=>$order_id))
            ->queryRow();

        try{
            $sql = "update g_order set status=:status,had_pay=:had_pay where order_id=:order_id";
            $command = $conn->createCommand($sql);
            $command->bindParam(":status",$status, PDO::PARAM_STR);
            $command->bindParam(":had_pay",$amount, PDO::PARAM_STR);
            $command->bindParam(":order_id",$order_id, PDO::PARAM_STR);
            $command->execute();

            if($type==1){
                $rs=User::Deduct($conn,$amount);
                if($rs['status']!=0){
                    $transaction->rollBack();
                    return $rs;
                }
            }

            $transaction->commit();

            $trans_type=TransRecord::TYPE_COURT_PAY;
            switch($row['type']){
                case Order::TYPE_COURT: $trans_type=TransRecord::TYPE_COURT_PAY;break;
                case Order::TYPE_COMPETITION: $trans_type=TransRecord::TYPE_COMPETITION_PAY;break;
                case Order::TYPE_TRIP: $trans_type=TransRecord::TYPE_TRIP_PAY;break;
            }

            TransRecord::Add($trans_type,-$amount,$serial_number);

            OrderLog::Add($order_id,$serial_number);

            return array('status'=>0,'desc'=>'成功');
        }catch (Exception $e){
            $transaction->rollBack();
            return array('status'=>3,'desc'=>'失败');;
        }

    }

}


    