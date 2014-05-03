<?php
/**
 * --请填写模块名称--
 *交易记录
 * @author #guohao#
 * @copyright Copyright &copy; 2003-2009 TrunkBow Co., Inc
 */
class TransRecord extends CActiveRecord {

    const TYPE_COURT_PAY=10;//订场
    const TYPE_COURT_CANCEL=11;//订场撤销
    const TYPE_TRIP_PAY=20;
    const TYPE_TRIP_CANCEL=21;
    const TYPE_COMPETITION_PAY=30;
    const TYPE_COMPETITION_CANCEL=31;
    const TYPE_RECHARGE_PAY=40;
    const TYPE_RECHARGE_CANCEL=41;

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
            '10'=>'订场',
            '11'=>'订场撤销',
            '20'=>'行程',
            '21'=>'行程撤销',
            '30'=>'赛事',
            '31'=>'赛事撤销',
            '40'=>'充值',
            '41'=>'充值撤销',
        );
        
        return $s == "" ? $rs : $rs[$s];
    }
    
    public static function getStatus($s="")
    {
        $rs = array(
            '0'=>'操作成功',
            '9'=>'操作失败',
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
            //$date_str=substr($date_str,0,6);
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

    public static function Add($type,$amount,$serial_number,$re_serial_number=""){
        $record_time=date("Y-m-d H:i:s");
        $user_id=Yii::app()->user->id;
        $sql = "insert into ".self::getTable()."
                   (serial_number,trans_type,amount,re_serial_number,status,user_id,record_time)
                     values
                    (:serial_number,:trans_type,:amount,:re_serial_number,:status,:user_id,:record_time)";
        $command = Yii::app()->db->createCommand($sql);
        $command->bindParam(":serial_number",$serial_number, PDO::PARAM_STR);
        $command->bindParam(":trans_type",$type, PDO::PARAM_STR);
        $command->bindParam(":amount",$amount, PDO::PARAM_STR);
        $command->bindParam(":re_serial_number",$re_serial_number, PDO::PARAM_STR);
        $command->bindParam(":user_id",$user_id, PDO::PARAM_STR);
        $command->bindParam(":record_time",$record_time, PDO::PARAM_STR);
        $command->execute();
    }
}


    