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
    
    
    public $cnt;//总笔数
    
    const fee_rate = 0.01;//1%的功能费
    const commission_rate = 1000;//10块

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
            $condition.= ( $condition == '') ? ' t.user_id=:user_id' : ' AND t.user_id=:user_id';
            $params['user_id'] = $args['user_id'];
        }
        
        
        
        if ($args['trans_type'] != ''){
            $condition.= ( $condition == '') ? ' t.trans_type=:trans_type' : ' AND t.trans_type=:trans_type';
            $params['trans_type'] = $args['trans_type'];
        }
        if ($args['status'] != ''){
            $condition.= ( $condition == '') ? ' t.status=:status' : ' AND t.status=:status';
            $params['status'] = $args['status'];
        }
        if ($args['startdate'] != ''){
            $condition.= ( $condition == '') ? ' t.record_time >=:startdate' : ' AND t.record_time>=:startdate';
            $params['startdate'] = $args['startdate'];
        }
        if ($args['enddate'] != ''){
            $condition.= ( $condition == '') ? ' t.record_time<=:enddate' : ' AND t.record_time<=:enddate';
            $params['enddate'] = $args['enddate']." 23:59:59";
        }
        if($args['agent_id']!="")
        {
            $condition.= ( $condition == '') ? ' s.agent_id=:agent_id' : ' AND s.agent_id=:agent_id';
            $params['agent_id'] = $args['agent_id'];
        }
        if($args['type']!="")
        {
            $condition.= ( $condition == '') ? ' s.type=:type' : ' AND s.type=:type';
            $params['type'] = $args['type'];
        }
        if($args['pay_type']!="")
        {
            $condition.= ( $condition == '') ? ' s.pay_method=:pay_method' : ' AND s.pay_method=:pay_method';
            $params['pay_method'] = $args['pay_type'];
        }
        if($args['user_isdn'] != "")
        {
            $condition.= ( $condition == '') ? ' s.phone=:phone' : ' AND s.user_id=:phone';
            $params['phone'] = $args['user_isdn'];
        }
        $table=self::getTable($params['startdate']);
        //$total_num = Translog::model()->count($condition, $params); //总记录数
        //print_r($condition);
        //print_r($params);
//        print_r($table);
        $total_num = Yii::app()->db->createCommand()
            ->select("count(1) c")
            ->from($table." t")
            ->leftJoin("g_order s", "s.order_id=t.order_id")
            ->where($condition, $params)
            ->queryRow();

        $order = 't.record_time DESC ';
    	

        $rows = Yii::app()->db->createCommand()
            ->select("t.*,s.agent_id,s.type,s.pay_method,s.phone")
            ->from($table." t")
                ->leftJoin("g_order s", "s.order_id=t.order_id")
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
    
    /**
     * 查询-统计
     * @param int $page
     * @param int $pageSize
     * @param array $args
     * @return array
     */
    public static function querySummary($page, $pageSize, $args = array()) {


        $condition = "t.status='00' ";
        $params = array();

        $cols = "sum(t.amount) sum_amount,count(1) cnt ";
        
        
        if ($args['trans_type'] != ''){
            $condition.= ( $condition == '') ? ' t.trans_type=:trans_type' : ' AND t.trans_type=:trans_type';
            $params['trans_type'] = $args['trans_type'];
            $cols .= ",t.trans_type ";
        }
        
        if ($args['startdate'] != ''){
            $condition.= ( $condition == '') ? ' t.record_time >=:startdate' : ' AND t.record_time>=:startdate';
            $params['startdate'] = $args['startdate'];
            
        }
        if ($args['enddate'] != ''){
            $condition.= ( $condition == '') ? ' t.record_time<=:enddate' : ' AND t.record_time<=:enddate';
            $params['enddate'] = $args['enddate']." 23:59:59";
        }
        if($args['agent_id']!="")
        {
            $condition.= ( $condition == '') ? ' s.agent_id=:agent_id' : ' AND s.agent_id=:agent_id';
            $params['agent_id'] = $args['agent_id'];
            $cols .= ",s.agent_id ";
        }
        if($args['type']!="")
        {
            $condition.= ( $condition == '') ? ' s.type=:type' : ' AND s.type=:type';
            $params['type'] = $args['type'];
            $cols .= ",s.type ";
        }
        if($args['pay_type']!="")
        {
            $condition.= ( $condition == '') ? ' s.pay_type=:pay_type' : ' AND s.pay_type=:pay_type';
            $params['pay_type'] = $args['pay_type'];
            $cols .= ",s.pay_type ";
        }
        
        $table=self::getTable($params['startdate']);
        //$total_num = Translog::model()->count($condition, $params); //总记录数
        //print_r($condition);
        //print_r($params);
//        print_r($table);
        $total_num = 1;

        
    	

        $rows = Yii::app()->db->createCommand()
            ->select($cols)
            ->from($table." t")
                ->leftJoin("g_order s", "s.order_id=t.order_id")
            ->where($condition, $params)
            ->queryAll();
        //var_dump($rows);
        $rs['status'] = 0;
        $rs['desc'] = '成功';
        $rs['agent_id'] = $args['agent_id'];
        $rs['type'] = $args['type'];
        $rs['pay_type'] = $args['pay_type'];
        $rs['trans_type'] = $args['trans_type'];
        $rs['rows'] = $rows;

        return $rs;
    }

    /**
     * 查询-统计代理商 时间段内订单数量
     * @param int $page
     * @param int $pageSize
     * @param array $args
     * @return array
     */
    public static function queryOrder($page, $pageSize, $args = array()) {


        $condition = " t.type='0' ";
        
        if ($args['startdate'] != ''){
            $condition.= ( $condition == "") ? " t.tee_time >='".$args['startdate']." 00:00:00'" : " AND t.tee_time>='".$args['startdate']." 00:00:00'";
        }
        if ($args['enddate'] != ''){
            $condition.= ( $condition == '') ? " t.tee_time<='".$args['enddate']." 23:59:59'" : " AND t.tee_time<='".$args['enddate']." 23:59:59'";
        }
        if($args['agent_id']!="")
        {
            $condition.= ( $condition == '') ? ' t.agent_id=\''.$args['agent_id'].'\'' : ' AND t.agent_id=\''.$args['agent_id'].'\'';
        }
        
        //var_dump($condition);
        $table="g_order";
        $total_num = 1;
        //0、等待确认；1、等待付款；2、付款完成；3、交易关闭 ，4-未到场  5-交易成功，6-等待退款 7-拒绝退款 8-退款中
        $row = array(
            'success'=>0,
            'pay'=>0,
            'cancel'=>0,
            'tobe_confirm'=>0,
            'tobe_pay'=>0,
            'tobe_cancel'=>0,
            'canceling'=>0,
            'total'=>0,
            'normal'=>0,
            'holiday'=>0,
            'pay_user'=>0,           
        );
        $sql = "select status,count(1) as cnt from g_order t where ".$condition." group by status";
        $rs = Yii::app()->db->createCommand($sql)->queryAll();
        $total = 0;
        if($rs)
        {
            foreach($rs as $srow)
            {
                $status = $srow['status'];
                $cnt = $srow['cnt'];
                $total += $cnt;
                switch($status)
                {
                    case '0':
                        $row['tobe_confirm'] = $cnt;
                        break;
                    case '1':
                        $row['tobe_pay'] = $cnt;
                        break;
                    case '2':
                        $row['pay'] = $cnt;
                        break;
                    case '3':
                        $row['cancel'] = $cnt;
                        break;
                    case '4':
                        $row['no_present'] = $cnt;
                        break;
                    case '5':
                        $row['success'] = $cnt;
                        break;
                    case '6':
                        $row['tobe_cancel'] = $cnt;
                        break;
                    case '7':
                        $row['refuse_cancel'] = $cnt;
                        break;
                    case '8':
                        $row['canceling'] = $cnt;
                        break;
                }
                        
            }
        }
        $row['total'] = $total;
        $sql = "select DAYOFWEEK(record_time) as weekno,count(1) as cnt from g_order t where ".$condition." group by DAYOFWEEK(record_time)";
        $rs = Yii::app()->db->createCommand($sql)->queryAll();
    	if($rs)
        {
            foreach($rs as $wrow)
            {
                $week = $wrow['weekno'];
                $cnt = $wrow['cnt'];
                if($week=='6'||$week == '7')
                {
                    $row['holiday'] += $cnt;
                }else
                {
                    $row['normal'] += $cnt;
                }
            }
        }
        $sql = "select count(1) as cnt from g_order t where ".$condition." and status='5'";
        $user_cnt = Yii::app()->db->createCommand($sql)->queryScalar();
        $row['pay_user'] = $user_cnt;

        $rows = array();
        $rows[] = $row;
        
        //var_dump($rows);
        $rs['status'] = 0;
        $rs['desc'] = '成功';
        $rs['agent_id'] = $args['agent_id'];
        $rs['rows'] = $rows;
        $rs['total_num'] = $total_num;
        return $rs;
    }
    
    /**
     * 查询-统计代理商 时间段内订单数量
     * @param int $page
     * @param int $pageSize
     * @param array $args
     * @return array
     */
    public static function queryOrderD($page, $pageSize, $args = array()) {


        $condition = "t.type='0' ";
        
        if ($args['startdate'] != ''){
            $condition.= ( $condition == "") ? " t.tee_time >='".$args['startdate']." 00:00:00'" : " AND t.tee_time>='".$args['startdate']." 00:00:00'";
        }
        if ($args['enddate'] != ''){
            $condition.= ( $condition == '') ? " t.tee_time<='".$args['enddate']." 23:59:59'" : " AND t.tee_time<='".$args['enddate']." 23:59:59'";
        }
        if($args['agent_id']!="")
        {
            $condition.= ( $condition == '') ? ' t.agent_id=\''.$args['agent_id'].'\'' : ' AND t.agent_id=\''.$args['agent_id'].'\'';
        }
        
        $court_list = Court::getCourtArray();
        $rows = array();
        $court_array = array();
        foreach($court_list as $ck=>$cv)
        {
            $rows[$ck] = array(
                'court_name'=>$cv,
                'court_id'=>$ck,
                'success'=>0,
                'pay'=>0,
                'cancel'=>0,
                'tobe_confirm'=>0,
                'tobe_pay'=>0,
                'tobe_cancel'=>0,
                'canceling'=>0,
                'total'=>0,
                'normal'=>0,
                'holiday'=>0,
                'pay_user'=>0,    
            );
            
            array_push($court_array,$ck);
        }
        
        $table="g_order";
        $total_num = @count($court_list);
        //0、等待确认；1、等待付款；2、付款完成；3、交易关闭 ，4-未到场  5-交易成功，6-等待退款 7-拒绝退款 8-退款中
        
        $sql = "select relation_id,status,count(1) as cnt from g_order t where ".$condition." group by relation_id,status";
        $rs = Yii::app()->db->createCommand($sql)->queryAll();
        $total = 0;
        if($rs)
        {
            foreach($rs as $srow)
            {
                $relationid = $srow['relation_id'];
                $status = $srow['status'];
                $cnt = $srow['cnt'];
                
                if(!in_array($relationid,$court_array))
                {
                    continue;
                }
                $rows[$relationid]['total'] += $cnt;
                switch($status)
                {
                    case '0':
                        $rows[$relationid]['tobe_confirm'] += $cnt;
                        break;
                    case '1':
                        $rows[$relationid]['tobe_pay'] += $cnt;
                        break;
                    case '2':
                        $rows[$relationid]['pay'] += $cnt;
                        break;
                    case '3':
                        $rows[$relationid]['cancel'] += $cnt;
                        break;
                    case '4':
                        $rows[$relationid]['no_present'] += $cnt;
                        break;
                    case '5':
                        $rows[$relationid]['success'] += $cnt;
                        break;
                    case '6':
                        $rows[$relationid]['tobe_cancel'] += $cnt;
                        break;
                    case '7':
                        $rows[$relationid]['refuse_cancel'] += $cnt;
                        break;
                    case '8':
                        $rows[$relationid]['canceling'] += $cnt;
                        break;
                }
                        
            }
        }
        
        $sql = "select relation_id,DAYOFWEEK(record_time) as weekno,count(1) as cnt from g_order t where ".$condition." group by relation_id,DAYOFWEEK(record_time)";
        $rs = Yii::app()->db->createCommand($sql)->queryAll();
    	if($rs)
        {
            foreach($rs as $wrow)
            {
                $relationid = $wrow['relation_id'];
                $week = $wrow['weekno'];
                $cnt = $wrow['cnt'];
                if(!in_array($relationid,$court_array))
                {
                    continue;
                }
                if($week=='6'||$week == '7')
                {
                    $rows[$relationid]['holiday'] += $cnt;
                }else
                {
                    $rows[$relationid]['normal'] += $cnt;
                }
            }
        }
        $sql = "select relation_id,count(1) as cnt from g_order t where ".$condition." and status='5' group by relation_id";
        $rs = Yii::app()->db->createCommand($sql)->queryAll();
        if($rs)
        {
            foreach($rs as $urow)
            {
                $relationid = $urow['relation_id'];
                $cnt = $urow['cnt'];
                if(!in_array($relationid,$court_array))
                {
                    continue;
                }
                $rows[$relationid]['pay_user'] += $cnt;
            }
        }
        
        
        $rs['status'] = 0;
        $rs['desc'] = '成功';
        $rs['agent_id'] = $args['agent_id'];
        $rs['rows'] = $rows;
        $rs['total_num'] = $total_num;
        return $rs;
    }
    
    /**
     * 查询-统计代理商时间段内应该收入多钱，应该汇给球场多钱
     * @param int $page
     * @param int $pageSize
     * @param array $args
     * @return array
     */
    public static function queryAgent($page, $pageSize, $args = array()) {


        $condition = "t.type='0' ";
        
        if ($args['startdate'] != ''){
            $condition.= ( $condition == "") ? " t.tee_time >='".$args['startdate']." 00:00:00'" : " AND t.tee_time>='".$args['startdate']." 00:00:00'";
        }
        if ($args['enddate'] != ''){
            $condition.= ( $condition == '') ? " t.tee_time<='".$args['enddate']." 23:59:59'" : " AND t.tee_time<='".$args['enddate']." 23:59:59'";
        }
        if($args['agent_id']!="")
        {
            $condition.= ( $condition == '') ? ' t.agent_id=\''.$args['agent_id'].'\'' : ' AND t.agent_id=\''.$args['agent_id'].'\'';
        }
        
        $total_num = 1;

        $rows = array();
        $row = array(
            'success_cnt'=>0,
            'user_cnt'=>0,
            'tobe_pay'=>0,
            'normal_amount'=>0,
            'holiday_amount'=>0,
            'total_amount'=>0
        );
        $sql = "select status,count(1) as cnt,sum(count) as user_cnt from g_order  t where ".$condition." group by status";
        $rs = Yii::app()->db->createCommand($sql)->queryAll();
        if($rs)
        {
            foreach($rs as $urow)
            {
                $status = $urow['status'];
                $cnt = $urow['cnt'];
                $user_cnt = $urow['user_cnt'];
                
                $row['user_cnt'] += $user_cnt;
                if($status == '5')
                {
                    $row['success_cnt'] += $cnt;
                }else if($status == '1')
                {
                    $row['tobe_pay'] += $cnt;
                }
            }
        }
        
        $sql = "select DAYOFWEEK(record_time) as weekno,sum(amount) as amount from g_order t where ".$condition." and status='5' group by DAYOFWEEK(record_time)";
        $rs = Yii::app()->db->createCommand($sql)->queryAll();
        if($rs)
        {
            foreach($rs as $arow)
            {
                $week = $arow['weekno'];
                $amount = $arow['amount'];
                
                $row['total_amount'] += $amount;
                
                if($week == '6'||$week == '7')
                {
                    $row['holiday_amount'] += $amount;
                }else
                {
                    $row['normal_amount'] += $amount;
                }
            }
        }
    	$rows[] = $row;

        //var_dump($rows);
        $rs['status'] = 0;
        $rs['desc'] = '成功';
        $rs['agent_id'] = $args['agent_id'];
        $rs['rows'] = $rows;
        $rs['total_num'] = $total_num;

        return $rs;
    }
    
    /**
     * 查询-统计代理商时间段内应该收入多钱，应该汇给球场多钱
     * @param int $page
     * @param int $pageSize
     * @param array $args
     * @return array
     */
    public static function queryAgentD($page, $pageSize, $args = array()) {


        $condition = "t.type='0' ";
        
        if ($args['startdate'] != ''){
            $condition.= ( $condition == "") ? " t.tee_time >='".$args['startdate']." 00:00:00'" : " AND t.tee_time>='".$args['startdate']." 00:00:00'";
        }
        if ($args['enddate'] != ''){
            $condition.= ( $condition == '') ? " t.tee_time<='".$args['enddate']." 23:59:59'" : " AND t.tee_time<='".$args['enddate']." 23:59:59'";
        }
        if($args['agent_id']!="")
        {
            $condition.= ( $condition == '') ? ' t.agent_id=\''.$args['agent_id'].'\'' : ' AND t.agent_id=\''.$args['agent_id'].'\'';
        }
        
        $court_list = Court::getCourtArray();
        $total_num = @count($court_list);
        
        $rows = array();
        foreach($court_list as $ck=>$cv)
        {
            $rows[$ck] = array(
                'court_id'=>$ck,
                'court_name'=>$cv,
                'success_cnt'=>0,
                'user_cnt'=>0,
                'tobe_pay'=>0,
                'normal_amount'=>0,
                'holiday_amount'=>0,
                'total_amount'=>0
            );
        }
        
        $sql = "select relation_id,status,count(1) as cnt,sum(count) as user_cnt from g_order  t where ".$condition." group by relation_id,status";
        $rs = Yii::app()->db->createCommand($sql)->queryAll();
        if($rs)
        {
            foreach($rs as $urow)
            {
                $relationid = $urow['relation_id'];
                $status = $urow['status'];
                $cnt = $urow['cnt'];
                $user_cnt = $urow['user_cnt'];
                
                $rows[$relationid]['user_cnt'] += $user_cnt;
                if($status == '5')
                {
                    $rows[$relationid]['success_cnt'] += $cnt;
                }else if($status == '1')
                {
                    $rows[$relationid]['tobe_pay'] += $cnt;
                }
            }
        }
        
        $sql = "select relation_id,DAYOFWEEK(record_time) as weekno,sum(amount) as amount from g_order t where ".$condition." and status='5' group by relation_id,DAYOFWEEK(record_time)";
        $rs = Yii::app()->db->createCommand($sql)->queryAll();
        if($rs)
        {
            foreach($rs as $arow)
            {
                $week = $arow['weekno'];
                $amount = $arow['amount'];
                $relationid = $arow['relation_id'];
                $rows[$relationid]['total_amount'] += $amount;
                
                if($week == '6'||$week == '7')
                {
                    $rows[$relationid]['holiday_amount'] += $amount;
                }else
                {
                    $rows[$relationid]['normal_amount'] += $amount;
                }
            }
        }
    	
        //var_dump($rows);
        $rs['status'] = 0;
        $rs['desc'] = '成功';
        $rs['agent_id'] = $args['agent_id'];
        $rs['rows'] = $rows;
        $rs['total_num'] = $total_num;
        return $rs;
    }
    
    /**
     * 查询-统计代理商时间段内客户统计
     * @param int $page
     * @param int $pageSize
     * @param array $args
     * @return array
     */
    public static function queryUser($page, $pageSize, $args = array()) {


        $condition = "t.type='0' and t.status='5' ";
        
        if ($args['startdate'] != ''){
            $condition.= ( $condition == "") ? " t.tee_time >='".$args['startdate']." 00:00:00'" : " AND t.tee_time>='".$args['startdate']." 00:00:00'";
        }
        if ($args['enddate'] != ''){
            $condition.= ( $condition == '') ? " t.tee_time<='".$args['enddate']." 23:59:59'" : " AND t.tee_time<='".$args['enddate']." 23:59:59'";
        }
        if($args['agent_id']!="")
        {
            $condition.= ( $condition == '') ? ' t.agent_id=\''.$args['agent_id'].'\'' : ' AND t.agent_id=\''.$args['agent_id'].'\'';
        }
        
        $total_num = 1;

        $rows = array();
        $row = array(
            'success_cnt'=>0,
            'user_cnt'=>0,
            'normal_cnt'=>0,
            'holiday_cnt'=>0,
        );
        
        
        $sql = "select DAYOFWEEK(record_time) as weekno,sum(count) as user_cnt,count(1) as cnt from g_order t where ".$condition."  group by DAYOFWEEK(record_time)";
        //var_dump($sql);
        $rs = Yii::app()->db->createCommand($sql)->queryAll();
        if($rs)
        {
            foreach($rs as $arow)
            {
                $week = $arow['weekno'];
                $user_cnt = $arow['user_cnt'];
                $cnt = $arow['cnt'];
                
                $row['success_cnt'] += $cnt;
                $row['user_cnt'] +=$user_cnt;
                
                if($week == '6'||$week == '7')
                {
                    $row['holiday_cnt'] += $user_cnt;
                }else
                {
                    $row['normal_cnt'] += $user_cnt;
                }
            }
        }
    	$rows[] = $row;

        //var_dump($rows);
        $rs['status'] = 0;
        $rs['desc'] = '成功';
        $rs['agent_id'] = $args['agent_id'];
        $rs['rows'] = $rows;
        $rs['total_num'] = $total_num;

        return $rs;
    }
    
    /**
     * 查询-统计代理商时间段内应该收入多钱，应该汇给球场多钱
     * @param int $page
     * @param int $pageSize
     * @param array $args
     * @return array
     */
    public static function queryUserD($page, $pageSize, $args = array()) {


        $condition = "t.type='0' and t.status='5' ";
        
        if ($args['startdate'] != ''){
            $condition.= ( $condition == "") ? " t.tee_time >='".$args['startdate']." 00:00:00'" : " AND t.tee_time>='".$args['startdate']." 00:00:00'";
        }
        if ($args['enddate'] != ''){
            $condition.= ( $condition == '') ? " t.tee_time<='".$args['enddate']." 23:59:59'" : " AND t.tee_time<='".$args['enddate']." 23:59:59'";
        }
        if($args['agent_id']!="")
        {
            $condition.= ( $condition == '') ? ' t.agent_id=\''.$args['agent_id'].'\'' : ' AND t.agent_id=\''.$args['agent_id'].'\'';
        }
        
        $base_sql = "select user_id,contact,count(1) as cnt,sum(count) as user_cnt,count(distinct relation_id) as court_cnt,sum(amount) as amount from g_order t where ".$condition." group by user_id";
        $cnt_sql = "select count(1) from (".$base_sql.") tt";
        $conn = Yii::app()->db;
        
        $total_num = $conn->createCommand($cnt_sql)->queryScalar();
        
        $sql = "select tt.* from (".$base_sql.") tt limit ".($page*$pageSize).",".$pageSize;
        
        $rows = $conn->createCommand($sql)->queryAll();
        
        if($rows)
        {
            foreach($rows as $key=>$row)
            {
                $user_id = $row['user_id'];
                
                $tmp_sql = "select DAYOFWEEK(record_time) as weekno,count(1) as cnt from g_order t where ".$condition." and user_id='".$user_id."' group by DAYOFWEEK(record_time)";
                $tmp_rows = $conn->createCommand($tmp_sql)->queryAll();
                if($tmp_rows)
                {
                    foreach($tmp_rows as $tmp_row)
                    {
                        $tmp_week = $tmp_row['weekno'];
                        $tmp_cnt = $tmp_row['cnt'];
                        if($tmp_week == '6'||$tmp_week == '7')
                        {
                            $rows[$key]['holiday_cnt'] += $tmp_cnt;
                        }else
                        {
                            $rows[$key]['normal_cnt'] += $tmp_cnt;
                        }
                    }
                }else
                {
                    $rows[$key]['holiday_cnt'] += 0;
                    $rows[$key]['normal_cnt'] += 0;
                }
            }
        }
    	
        //var_dump($rows);
        $rs['status'] = 0;
        $rs['desc'] = '成功';
        $rs['agent_id'] = $args['agent_id'];
        $rs['rows'] = $rows;
        $rs['total_num'] = $total_num;
        return $rs;
    }
    
    
    /**
     * 查询-统计代理商时间段内客户统计
     * @param int $page
     * @param int $pageSize
     * @param array $args
     * @return array
     */
    public static function queryBalance($page, $pageSize, $args = array()) {


        $condition = "t.type='0' ";
        
        if ($args['startdate'] != ''){
            $condition.= ( $condition == "") ? " t.tee_time >='".$args['startdate']." 00:00:00'" : " AND t.tee_time>='".$args['startdate']." 00:00:00'";
        }
        if ($args['enddate'] != ''){
            $condition.= ( $condition == '') ? " t.tee_time<='".$args['enddate']." 23:59:59'" : " AND t.tee_time<='".$args['enddate']." 23:59:59'";
        }
        if($args['agent_id']!="")
        {
            $condition.= ( $condition == '') ? ' t.agent_id=\''.$args['agent_id'].'\'' : ' AND t.agent_id=\''.$args['agent_id'].'\'';
        }
        
        $total_num = 1;

        $rows = array();
        $row = array(
            'total_cnt'=>0,
            'success_cnt'=>0,
            'cancel_cnt'=>0,
            'success_ucnt'=>0,
            'cancel_ucnt'=>0,
            'balance'=>0,
            'cancel_amount'=>0,
            'total_amount'=>0,
            'fee'=>0,
            'commission'=>0,
            'user_cnt'=>0,
            'final_amount'=>0
        );
        
        
        $sql = "select status,sum(count) as user_cnt,count(1) as cnt from g_order t where ".$condition."  group by status";
        //var_dump($sql);
        $rs = Yii::app()->db->createCommand($sql)->queryAll();
        if($rs)
        {
            foreach($rs as $arow)
            {
                $status = $arow['status'];
                $user_cnt = $arow['user_cnt'];
                $cnt = $arow['cnt'];
                
                $row['total_cnt'] += $cnt;
                
                
                if($status == '5')
                {
                    $row['success_cnt'] += $cnt;
                    $row['success_ucnt'] += $user_cnt;
                    $row['user_cnt'] += $user_cnt;
                }else if($status == '3')
                {
                    $row['cancel_cnt'] += $cnt;
                    $row['cancel_ucnt'] += $user_cnt;
                }
            }
        }
        
        //获取时间段内交易记录表内容
        $amount_rs = self::getSubSql($args['startdate'], $args['enddate'], $condition);
        $row['balance'] = -($amount_rs['balance']);
        $row['cancel_amount'] = $amount_rs['cancel_amount'];
        $row['total_amount'] = $row['balance']-$row['cancel_amount'];
        
        $row['fee'] = $row['total_amount']*self::fee_rate;
        $row['commission'] = $row['user_cnt'] * self::commission_rate;
        $row['final_amount'] = $row['total_amount'] - $row['fee'] - $row['commission'];
        
        
    	$rows[] = $row;

        //print_r($rows);
        $rs['status'] = 0;
        $rs['desc'] = '成功';
        $rs['agent_id'] = $args['agent_id'];
        $rs['rows'] = $rows;
        $rs['total_num'] = $total_num;

        return $rs;
    }
    
    public static function getSubSql($stime,$etime,$condition)
    {
        
        $result =  array(
                    'balance'=>0,
                    'cancel_amount'=>0
                );
        
        $stime = str_replace("-", "", $stime);
        $etime = str_replace("-", "", $etime);
        $ym_s = intval(substr($stime, 0, 6));
        $ym_e = intval(substr($etime, 0, 6));
        
        $conn = Yii::app()->db;
        $sql = "";
        $table_pre = "g_transrecord_";
        if($ym_e == $ym_s)
        {
            $table_name = $table_pre . $ym_e;
            $cnt_table_sql = "select count(1) as count from `INFORMATION_SCHEMA`.`TABLES` where `TABLE_SCHEMA`='golf' and  `TABLE_NAME`='" . $table_name . "' ";
            $cnt_table_cnt = $conn->createCommand($cnt_table_sql)->queryScalar();
            if ($cnt_table_cnt == 0) {
                return $result;
            }
            $sql .= "select s.trans_type,sum(s.amount) as amount from " . $table_name . " s left join g_order t on s.order_id = t.order_id where " . $condition." and trans_type in ('01','31') group by s.trans_type";
            $rs = $conn->createCommand($sql)->queryAll();
            if($rs)
            {
                foreach($rs as $row)
                {
                    $type = $row['trans_type'];
                    $amount = $row['amount'];
                    if($type == '01')
                    {
                        $result['balance'] += $amount;
                    }else if($type == '31')
                    {
                        $result['cancel_amount'] += $amount;
                    }
                }
            }
            return $result;
        }
        //e and s 不想等时执行
        
        while($ym_e>=$ym_s)
        {
            $table_name = $table_pre . $ym_e;
            
            //检测当前的数据库表存在么
            $cnt_table_sql = "select count(1) as count from `INFORMATION_SCHEMA`.`TABLES` where `TABLE_SCHEMA`='golf' and  `TABLE_NAME`='" . $table_name . "' ";
            $cnt_table = $conn->createCommand($cnt_table_sql)->queryScalar();
            
            if ($cnt_table == 0) {
                $before_month_sql = "select DATE_FORMAT(DATE_SUB('".$etime."',INTERVAL 1 MONTH),'%Y%m%d') as before_month";                
                $etime = $conn->createCommand($before_month_sql)->queryScalar();
                $ym_e = intval(substr($etime,0,6));
                
                continue;
            }
            
            $sql = "select s.trans_type,sum(s.amount) as amount from " . $table_name . " s left join g_order t on s.order_id = t.order_id where " . $condition." and trans_type in ('01','31') group by s.trans_type";
            
            $rs = $conn->createCommand($sql)->queryAll();
            if($rs)
            {
                foreach($rs as $row)
                {
                    $type = $row['trans_type'];
                    $amount = $row['amount'];
                    if($type == '01')
                    {
                        $result['balance'] += $amount;
                    }else if($type == '31')
                    {
                        $result['cancel_amount'] += $amount;
                    }
                }
            }
            
            
            $before_month_sql = "select DATE_FORMAT(DATE_SUB('".$etime."',INTERVAL 1 MONTH),'%Y%m%d') as before_month";
            
            $etime = $conn->createCommand($before_month_sql)->queryScalar();
            $ym_e = intval(substr($etime,0,6));
            
        }
        return $result;
    }
    
    /**
     * 查询-统计代理商时间段内应该收入多钱，应该汇给球场多钱
     * @param int $page
     * @param int $pageSize
     * @param array $args
     * @return array
     */
    public static function queryBalanceD($page, $pageSize, $args = array()) {

        //var_dump($page);
        $condition = "t.type='0' ";
        
        if ($args['startdate'] != ''){
            $condition.= ( $condition == "") ? " t.tee_time >='".$args['startdate']." 00:00:00'" : " AND t.tee_time>='".$args['startdate']." 00:00:00'";
        }
        if ($args['enddate'] != ''){
            $condition.= ( $condition == '') ? " t.tee_time<='".$args['enddate']." 23:59:59'" : " AND t.tee_time<='".$args['enddate']." 23:59:59'";
        }
        if($args['agent_id']!="")
        {
            $condition.= ( $condition == '') ? ' t.agent_id=\''.$args['agent_id'].'\'' : ' AND t.agent_id=\''.$args['agent_id'].'\'';
        }
        
        $base_sql = "select date_format(t.tee_time,'%Y-%m-%d') as tee_date,count(1) as total_cnt from g_order t where ".$condition." group by date_format(t.tee_time,'%Y-%m-%d')";
        $cnt_sql = "select count(1) from (".$base_sql.") tt";
        $conn = Yii::app()->db;
        
        $total_num = $conn->createCommand($cnt_sql)->queryScalar();
        
        $sql = "select tt.* from (".$base_sql.") tt limit ".($page*$pageSize).",".$pageSize;
        
        $rows = $conn->createCommand($sql)->queryAll();
        $rs['t_total_cnt'] = 0;
        $rs['t_success_cnt'] = 0;
        $rs['t_cancel_cnt'] = 0;
        $rs['t_success_ucnt'] = 0;
        $rs['t_cancel_ucnt'] = 0;
        $rs['t_balance'] = 0;
        $rs['t_cancel_amount'] = 0;
        $rs['t_total_amount'] = 0;
        if($rows)
        {
            foreach($rows as $key=>$row)
            {
                $rows[$key]['success_cnt'] = 0;
                $rows[$key]['cancel_cnt'] = 0;
                $rows[$key]['success_ucnt'] = 0;
                $rows[$key]['cancel_ucnt'] = 0;
                
                
                $tee_date = $row['tee_date'];
                
                $tmp_sql = "select status,count(1) as cnt,sum(count) as user_cnt from g_order t where ".$condition." and t.status in('5','3') and date_format(t.tee_time,'%Y-%m-%d')='".$tee_date."' group by status";
                $tmp_rows = $conn->createCommand($tmp_sql)->queryAll();
                if($tmp_rows)
                {
                    foreach($tmp_rows as $tmp_row)
                    {
                        $tmp_status = $tmp_row['status'];
                        $tmp_cnt = $tmp_row['cnt'];
                        $tmp_ucnt = $tmp_row['user_cnt'];
                        if($tmp_status == '5')
                        {
                            $rows[$key]['success_cnt'] += $tmp_cnt;
                            $rows[$key]['success_ucnt'] += $tmp_ucnt;
                        }else
                        {
                            $rows[$key]['cancel_cnt'] += $tmp_cnt;
                            $rows[$key]['cancel_ucnt'] += $tmp_ucnt;
                        }
                    }
                }
                
                //然后计算单天的金额
                $cdt = $condition." and date_format(t.tee_time,'%Y-%m-%d')='".$tee_date."'";
                $amount_rs = self::getSubSql($tee_date, $tee_date, $cdt);
                $rows[$key]['balance'] = -($amount_rs['balance']);
                $rows[$key]['cancel_amount'] = $amount_rs['cancel_amount'];
                $rows[$key]['total_amount'] = $rows[$key]['balance']-$rows[$key]['cancel_amount'];
                
                $rs['t_total_cnt'] += $rows[$key]['total_cnt'];
                $rs['t_success_cnt'] += $rows[$key]['success_cnt'];
                $rs['t_cancel_cnt'] += $rows[$key]['cancel_cnt'];
                $rs['t_success_ucnt'] += $rows[$key]['success_ucnt'];
                $rs['t_cancel_ucnt'] += $rows[$key]['cancel_ucnt'];
                $rs['t_balance'] += $rows[$key]['balance'];
                $rs['t_cancel_amount'] += $rows[$key]['cancel_amount'];
                $rs['t_total_amount'] += $rows[$key]['total_amount'];
            }
        }
    	
        //var_dump($rows);
        $rs['status'] = 0;
        $rs['desc'] = '成功';
        $rs['agent_id'] = $args['agent_id'];
        $rs['rows'] = $rows;
        $rs['total_num'] = $total_num;
        
        return $rs;
    }

    
    
    public static function PersonalList($page,$pageSize,$args){
        $condition = ' 1=1 ';
        $params = array();
        $condition.=' AND order.user_id=:user_id';
        $params['user_id'] = Yii::app()->user->id;

        $condition.=' AND t.status=:status';
        $params['status'] = TransRecord::STATUS_SUCCESS;

        $table=self::getTable($args->month);
        $order = 't.record_time desc';

        $rows=Yii::app()->db->createCommand()
            ->select("t.status,t.amount,t.order_id,t.trans_type,t.record_time")
            ->from("{$table} t")
            ->leftJoin("order","order.order_id=t.order_id")
            ->where($condition,$params)
            ->order($order)
            ->limit($pageSize)
            ->offset($page * $pageSize)
            ->queryAll();

        return $rows;
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


    