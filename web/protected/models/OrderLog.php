<?php
/**
 * --请填写模块名称--
 *订单操作记录
 * @author #guohao#
 * @copyright Copyright &copy; 2003-2009 TrunkBow Co., Inc
 */
class OrderLog extends CActiveRecord {


    public static function model($className=__CLASS__){
        return parent::model($className);
    }

    public function tableName(){
        return "g_order_log_";
    }

    public function rules(){
        return array(
         );
    }
    
    //操作类型；0、下单；1、订单确认；2、付款；3、修改；4、撤销
    public static function getOptType($s="")
    {
        $rs = array(
            '0'=>'下单',
            '1'=>'订单确认',
            '2'=>'付款',
            '3'=>'修改',
            '4'=>'撤销',
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
        
        if ($args['order_id'] != ''){
            $condition.= ( $condition == '') ? ' order_id=:order_id' : ' AND order_id=:order_id';
            $params['order_id'] = $args['order_id'];
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
        
        $table=self::getTable($args['order_time']);//下单时间。订单的跟踪过程，都会在下单时间所在的月表中
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
            //$date_str=substr($date_str,0,6);
            $table="g_order_log_".$date_str;
        }else{
            $settdate=str_replace("-","",$settdate);
            $date_str=substr($settdate,0,6);
            $table="g_order_log_".$date_str;
        }
        try{
            Yii::app()->db->createCommand('create table if not exists `'.$table.'` like g_order_log_')->execute();
        }catch (Exception $e){
            echo "";
        }
        return $table;
    }

    public static function Add($order_id,$serial_number){
        $record_time=date("Y-m-d H:i:s");
        $log_table="g_order_log_".date("Ym");

        Yii::app()->db->createCommand('create table if not exists `'.$log_table.'` like g_order_log_')->execute();

        $row=Yii::app()->db->createCommand()
            ->select("*")
            ->from("g_order")
            ->where("order_id=:order_id",array("order_id"=>$order_id))
            ->queryRow();

        $sql = "insert into ".$log_table."
                   (order_id,user_id,`type`,relation_id,relation_name,tee_time,`count`,unitprice,amount,had_pay,pay_type,status,record_time,serial_number,agent_id,contact,phone,pay_method,`desc`)
                     values
                    (:order_id,:user_id,:type,:relation_id,:relation_name,:tee_time,:count,:unitprice,:amount,:had_pay,:pay_type,:status,:record_time,:serial_number,:agent_id,:contact,:phone,:pay_method,:desc)";
        $command = Yii::app()->db->createCommand($sql);
        $command->bindParam(":order_id",$row['order_id'], PDO::PARAM_STR);
        $command->bindParam(":user_id",$row['user_id'], PDO::PARAM_STR);
        $command->bindParam(":type",$row['type'], PDO::PARAM_STR);
        $command->bindParam(":relation_id",$row['relation_id'], PDO::PARAM_STR);
        $command->bindParam(":relation_name",$row['relation_name'], PDO::PARAM_STR);
        $command->bindParam(":tee_time",$row['tee_time'], PDO::PARAM_STR);
        $command->bindParam(":count",$row['count'], PDO::PARAM_STR);
        $command->bindParam(":unitprice",$row['unitprice'], PDO::PARAM_STR);
        $command->bindParam(":amount",$row['amount'], PDO::PARAM_STR);
        $command->bindParam(":had_pay",$row['had_pay'], PDO::PARAM_STR);
        $command->bindParam(":pay_type",$row['pay_type'], PDO::PARAM_STR);
        $command->bindParam(":status",$row['status'], PDO::PARAM_STR);
        $command->bindParam(":record_time",$record_time, PDO::PARAM_STR);
        $command->bindParam(":serial_number",$serial_number, PDO::PARAM_STR);
        $command->bindParam(":agent_id",$row['agent_id'] , PDO::PARAM_STR);
        $command->bindParam(":contact",$row['contact'] , PDO::PARAM_STR);
        $command->bindParam(":phone",$row['phone'], PDO::PARAM_STR);
        $command->bindParam(":pay_method",$row['pay_method'], PDO::PARAM_STR);
        $command->bindParam(":desc",$row['desc'], PDO::PARAM_STR);
        $command->execute();

    }
    
   
}


    