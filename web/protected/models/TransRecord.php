<?php
/**
 * --请填写模块名称--
 *交易记录
 * @author #guohao#
 * @copyright Copyright &copy; 2003-2009 TrunkBow Co., Inc
 */
class TransRecord extends CActiveRecord {

//    const TYPE_COURT_PAY=10;//订场
//    const TYPE_COURT_CANCEL=11;//订场撤销
//    const TYPE_TRIP_PAY=20;
//    const TYPE_TRIP_CANCEL=21;
//    const TYPE_COMPETITION_PAY=30;
//    const TYPE_COMPETITION_CANCEL=31;
//    const TYPE_RECHARGE_PAY=40;
//    const TYPE_RECHARGE_CANCEL=41;

    const TRANS_TYPE_PURCHASE="01";//消费
    const TRANS_TYPE_VOID="31";//消费撤销
    const TRANS_TYPE_REFUND="04";//退货
    const TRANS_TYPE_RECHARGE="90";//充值

    const STATUS_SUCCESS="00";
    const STATUS_PROCESS="01";
    const STATUS_FALSE="02";

    public static function model($className=__CLASS__){
        return parent::model($className);
    }

    public function tableName(){
        return "g_transrecord_";
    }

    public function rules(){
        return array(
         );
    }
    
    
    public static function getTransType($s = "")
    {
        $rs = array(
            self::TRANS_TYPE_PURCHASE=>'消费',
            self::TRANS_TYPE_VOID=>'消费撤销',
            self::TRANS_TYPE_REFUND=>'退货',
            self::TRANS_TYPE_RECHARGE=>'充值',
        );
        
        return $s == "" ? $rs : $rs[$s];
    }

    //通过订单类型，转换成交易类型
    public static function GetPayTypeByOrderType($order_type){
        return self::TRANS_TYPE_PURCHASE;
    }

    //通过订单类型，转换成交易类型
    public static function GetCancelTypeByOrderType($order_type){
        return self::TRANS_TYPE_REFUND;
    }
    
    public static function getStatus($s="")
    {
        $rs = array(
            self::STATUS_SUCCESS=>'交易成功',
            self::STATUS_PROCESS=>'处理中',
            self::STATUS_FALSE=>'交易失败',
        );
        
        return $s == "" ? $rs : $rs[$s];
    }

    /**
     * 查询
     * @param int $page
     * @param int $pageSize
     * @param array $args
     * @return array
     */
    public static function queryList($page, $pageSize, $args = array()) {


        $condition = '';
        $params = array();

        if ($args['user_id'] != ''){
            $condition.= ( $condition == '') ? ' user_id=:user_id' : ' AND user_id=:user_id';
            $params['user_id'] = $args['user_id'];
        }
        
        if($args['user_isdn'] != "")
        {
            $user_info = User::model()->find("phone='{$args['user_isdn']}'");
            if($user_info)
            {
                $condition.= ( $condition == '') ? ' user_id=:user_id' : ' AND user_id=:user_id';
                $params['user_id'] = $user_info['user_id'];
            }
        }
        
        if ($args['trans_type'] != ''){
            $condition.= ( $condition == '') ? ' trans_type=:trans_type' : ' AND trans_type=:trans_type';
            $params['trans_type'] = $args['trans_type'];
        }
        if ($args['status'] != ''){
            $condition.= ( $condition == '') ? ' status=:status' : ' AND status=:status';
            $params['status'] = $args['status'];
        }
        if ($args['startdate'] != ''){
            $condition.= ( $condition == '') ? ' record_time >=:startdate' : ' AND record_time>=:startdate';
            $params['startdate'] = $args['startdate'];
        }
        if ($args['enddate'] != ''){
            $condition.= ( $condition == '') ? ' record_time<=:enddate' : ' AND record_time<=:enddate';
            $params['enddate'] = $args['enddate']." 23:59:59";
        }
        $table=self::getTable($params['startdate']);
        //$total_num = Translog::model()->count($condition, $params); //总记录数
//        print_r($condition);
//        print_r($params);
//        print_r($table);
        $total_num = Yii::app()->db->createCommand()
            ->select("count(1) c")
            ->from($table)
            ->where($condition, $params)
            ->queryRow();

        $order = 'record_time DESC ';
    	

        $rows = Yii::app()->db->createCommand()
            ->select("*")
            ->from($table)
            ->where($condition, $params)
            ->order($order)
            ->limit($pageSize)
            ->offset($page * $pageSize)
            ->queryAll();

        $rs['status'] = 0;
        $rs['desc'] = '成功';
        $rs['page_num'] = ($page+1);
        $rs['total_num'] = $total_num['c'];
        $rs['num_of_page'] = $pageSize;
        $rs['rows'] = $rows;

        return $rs;
    }

    public static function getTable($settdate=""){
        if($settdate==""){
            $date_str=date("Ym");
            $table="g_transrecord_".$date_str;
        }else{
            $settdate=str_replace("-","",$settdate);
            $date_str=substr($settdate,0,6);
            $table="g_transrecord_".$date_str;
        }
        try{
            Yii::app()->db->createCommand('create table if not exists `'.$table.'` like g_transrecord_')->execute();
        }catch (Exception $e){
            echo "";
        }
        return $table;
    }

    public static function GetBySerialNumber($serial_number,$trans_type){
        $table=self::getTable(substr($serial_number,0,6));
        $row = Yii::app()->db->createCommand()
            ->select("*")
            ->from($table)
            ->where("serial_number='{$serial_number}' and status='".TransRecord::STATUS_SUCCESS."' and trans_type='".$trans_type."'")
            ->queryRow();
        return $row;
    }

    public static function GetByOrderNumber($order_id,$trans_type){
        $table=self::getTable(substr($order_id,0,6));
        $row = Yii::app()->db->createCommand()
            ->select("*")
            ->from($table)
            ->where("order_id='{$order_id}' and status='".TransRecord::STATUS_SUCCESS."' and trans_type='".$trans_type."'")
            ->queryRow();
        return $row;
    }

    public static function Add(&$conn,$order_id,$type,$amount,$serial_number,$status,$re_serial_number="",$out_serial_number="",$user_id="",$operator_id="",$out_order_number="",$record_time=""){
        if(!$record_time){
            $record_time=date("YmdHis");
        }
        $sql = "insert into ".self::getTable($order_id)."
                   (order_id,serial_number,trans_type,amount,re_serial_number,status,user_id,operator_id,out_serial_number,record_time,out_order_number)
                     values
                   (:order_id,:serial_number,:trans_type,:amount,:re_serial_number,:status,:user_id,:operator_id,:out_serial_number,:record_time,:out_order_number)";
        $command = $conn->createCommand($sql);
        $command->bindParam(":order_id",$order_id, PDO::PARAM_STR);
        $command->bindParam(":serial_number",$serial_number, PDO::PARAM_STR);
        $command->bindParam(":trans_type",$type, PDO::PARAM_STR);
        $command->bindParam(":amount",$amount, PDO::PARAM_STR);
        $command->bindParam(":re_serial_number",$re_serial_number, PDO::PARAM_STR);
        $command->bindParam(":status",$status, PDO::PARAM_STR);
        $command->bindParam(":user_id",$user_id, PDO::PARAM_STR);
        $command->bindParam(":operator_id",$operator_id, PDO::PARAM_STR);
        $command->bindParam(":out_serial_number",$out_serial_number, PDO::PARAM_STR);
        $command->bindParam(":record_time",$record_time, PDO::PARAM_STR);
        $command->bindParam(":out_order_number",$out_order_number, PDO::PARAM_STR);
        $command->execute();
    }

    public static function ChangeStatusByOutOrderNumber(&$conn,$order_id,$out_order_number,$out_serial_number,$status){
        $sql = "update ".self::getTable($order_id)." set status=:status,out_serial_number=:out_serial_number where out_order_number=:out_order_number";
        $command = $conn->createCommand($sql);
        $command->bindParam(":status",$status, PDO::PARAM_STR);
        $command->bindParam(":out_serial_number",$out_serial_number, PDO::PARAM_STR);
        $command->bindParam(":out_order_number",$out_order_number, PDO::PARAM_STR);
        $command->execute();
    }

}


    