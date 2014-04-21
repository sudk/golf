<?php
/**
 * --请填写模块名称--
 *报价单模型
 * @author #guohao #
 * @copyright Copyright &copy; 2003-2009 TrunkBow Co., Inc
 */
class Policy extends CActiveRecord {

    const STATUS_NORMAL=0;//正常
    const STATUS_DISABLE=1;//下架

   //类型改为：0正常报价；1优惠报表；2特殊日和节假日报价。
    const TYPE_NORMAL = '0';
    const TYPE_FOVERABLE = '1';
    const TYPE_SPECIAL = '2';
    
    const IS_YES = '1';
    const IS_NO = '0';
    
    public static function model($className=__CLASS__){
        return parent::model($className);
    }

    public function tableName(){
        return 'g_policy';
    }

    public function rules(){
        return array(
             //安全性
            //array('agent_name,phone,contactor,extra', 'safe', 'on' => 'create'),
            //array('agent_name,phone,contactor,extra,status', 'safe', 'on' => 'modify'),

            //array('password', 'compare', 'compareAttribute'=>'passwordc', 'on'=>'create,modify'),
         );
    }


   

    /**
     * 报价状态
     * @param type $s
     * @return type
     */
    public static function  GetStatus($s = "")
    {
        $ar=array(
            self::STATUS_NORMAL=>"正常预订",
            self::STATUS_DISABLE=>"禁止预订",
        );
        return trim($s)!=""?$ar[$s]:$ar;
    }


    /**
     * 报价单类型
     * @param type $s
     * @return type
     */
    public static function getType($s = "")
    {
        $rs = array(
            self::TYPE_NORMAL=>'正常报价',
            self::TYPE_FOVERABLE=>'优惠报价',
            self::TYPE_SPECIAL=>'节假日报价'
        );
        
        return $s == "" ? $rs : $rs[$s];
    }
    
    /**
     * 是否的判断
     * @param type $s
     * @return type
     */
    public static function getYesOrNot($s = "")
    {
        $rs = array(
            '1'=>'是',
            '0'=>'否'
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

        $condition = ' 1=1 ';
        $params = array();

        if ($args['id'] != ''){
            $condition.= ' AND id=:id';
            $params['id'] = $args['id'];
        }
        if ($args['agent_id'] != ''){
            $condition.=' AND agent_id = :agent_id';
            $params['agent_id'] = $args['agent_id'];
        }
        if ($args['court_id'] != ''){
            $condition.= ' AND court_id=:court_id';
            $params['court_id'] = $args['court_id'];
        }
        
        if ($args['type'] != ''){
            $condition.= ' AND type=:type';
            $params['type'] = $args['type'];
        }
        
        if ($args['status'] != ''){
            $condition.=' AND status=:status';
            $params['status'] = $args['status'];
        }
        
        if($args['month']!="")
        {
            $condition .= " AND date_format(start_date,'%Y-%m')='".$args['month']."'";
        }
        //var_dump($condition);
        $total_num = Policy::model()->count($condition, $params); //总记录数

        $criteria = new CDbCriteria();
        
    	
        $criteria->order = 'record_time  DESC';
        

        $criteria->condition = $condition;
        $criteria->params = $params;

        $pages = new CPagination($total_num);
        $pages->pageSize = $pageSize;
        $pages->setCurrentPage($page);
        $pages->applyLimit($criteria);

        $rows = Policy::model()->findAll($criteria);

        $rs['status'] = 0;
        $rs['desc'] = '成功';
        $rs['page_num'] = ($pages->currentPage + 1);
        $rs['total_num'] = $total_num;
        $rs['total_page'] = ceil($rs['total_num'] / $rs['page_num']);
        $rs['num_of_page'] = $pages->pageSize;
        $rs['rows'] = $rows;

        return $rs;
    }
    
    
    
    public static function deleteRecord($id)
    {
        $rs['status']=false;
        $rs['desc']="删除失败！";
        if(trim($id)==""||$id==null){
           $rs['desc']="消息标题不能为空！";
           return $rs;
        }
        
        $connection = Yii::app()->db;
        $transaction = $connection->beginTransaction();
        try {
            $sql = "delete from g_policy where id = '".$id."'";
            $connection->createCommand($sql)->execute();
            $sql = "delete from g_policy_detail where policy_id = '".$id."'";
            $connection->createCommand($sql)->execute();
            
            $transaction->commit();
            $rs['status']=true;
            return $rs;
        }
        catch (Exception $e)
        {
            //print_r($e->getMessage());
            $transaction->rollBack();
            $rs['desc']="删除失败！";
            return $rs;
        }
    }
    
    
    
    public static function insertRecord($args,$week_day)
    {
        $rs['status']=false;
        $rs['msg']="新增成功！";
        if(!isset($args)||@count($args) == 0){
           $rs['msg']="内容不能为空！";
           return $rs;
        }
        
        $connection = Yii::app()->db;
        $transaction = $connection->beginTransaction();
        try {
            //判断同一个场地，同一个代理商，同一个月不能报价两次
            $start_date = $args['start_date'];
            $month = substr($start_date,0,7);
            $type = $args['type'];
            $valid_sql = "select count(1) as cnt from g_policy where court_id='{$args['court_id']}' and agent_id='{$args['agent_id']}' and type='{$type}' and date_format(start_date,'%Y-%m')='{$month}'";
            $valid_row = $connection->createCommand($valid_sql)->queryRow();
            if($valid_row==null||$valid_row['cnt']!=0)
            {
                $connection->rollBack();
                $rs['status'] = false;
                $rs['msg'] = "本月的报价单已经提交，不能重复提交";
                return $rs;
            }
            //insert Policy
            $sql = "insert into g_policy(id,start_date,end_date,agent_id,court_id,remark,cancel_remark,
                is_green,is_caddie,is_car,is_wardrobe,is_meal,is_insurance,is_tip,pay_type,type,status,
                record_time,creatorid) 
                values('{$args['id']}','{$args['start_date']}','{$args['end_date']}',
                '{$args['agent_id']}','{$args['court_id']}','{$args['remark']}','{$args['cancel_remark']}','{$args['is_green']}',
                '{$args['is_caddie']}','{$args['is_car']}','{$args['is_wardrobe']}','{$args['is_meal']}','{$args['is_insurance']}',
                '{$args['is_tip']}','{$args['pay_type']}','{$args['type']}','{$args['status']}','{$args['record_time']}','{$args['creatorid']}')";
            //insert Plicy Detail
            //var_dump($sql);
            $connection->createCommand($sql)->execute();
            $policy_id = $args['id'];
            foreach($week_day as $day=>$day_row)
            {
                if($day == '7'){
                    $day = '0';
                }
                foreach($day_row as $row)
                {
                $sql = "insert into g_policy_detail(policy_id,day,start_time,end_time,price,record_time,status)
                    values('{$policy_id}','{$day}','{$row['start_time']}','{$row['end_time']}','{$row['price']}','{$row['record_time']}','{$row['status']}')";
                //var_dump($sql);
                $connection->createCommand($sql)->execute();
                }
            }
            
            $transaction->commit();
            $rs['status']=true;
            return $rs;
        }
        catch (Exception $e)
        {
            //print_r($e->getMessage());
            $transaction->rollBack();
            $rs['msg']="新增失败！";
            return $rs;
        }
    }
    
    /**
     * 更新报价单
     * 更新policy表的内容。
     * 然后删掉 Policy_detail的内容，
     * 添加新的Detail数据
     * @param type $args
     * @param type $week_day
     * @return string|boolean
     */
    public static function updateRecord($args,$week_day)
    {
        $rs['status']=false;
        $rs['msg']="更新成功！";
        if(!isset($args)||@count($args) == 0){
           $rs['msg']="内容不能为空！";
           return $rs;
        }
        
        $connection = Yii::app()->db;
        $transaction = $connection->beginTransaction();
        try {
            //更新Policy
            $sql = "update g_policy set start_date='{$args['start_date']}',end_date='{$args['end_date']}',
                remark='{$args['remark']}',cancel_remark='{$args['cancel_remark']}',is_green='{$args['is_green']}',
                is_caddie='{$args['is_caddie']}',is_car='{$args['is_car']}',is_wardrobe='{$args['is_wardrobe']}',is_meal='{$args['is_meal']}',is_insurance='{$args['is_insurance']}',
                is_tip='{$args['is_tip']}',pay_type='{$args['pay_type']}',status='{$args['status']}' where id='{$args['id']}'";
            $connection->createCommand($sql)->execute();
            //删除Detail的内容
            $sql = "delete from g_policy_detail where policy_id='{$args['id']}'";
            $connection->createCommand($sql)->execute();
            //加入新的Detail内容
            $policy_id = $args['id'];
            foreach($week_day as $day=>$day_row)
            {
                if($day == '7'){
                    $day = '0';
                }
                foreach($day_row as $row)
                {
                $sql = "insert into g_policy_detail(policy_id,day,start_time,end_time,price,record_time,status)
                    values('{$policy_id}','{$day}','{$row['start_time']}','{$row['end_time']}','{$row['price']}','{$row['record_time']}','{$row['status']}')";
                //var_dump($sql);
                $connection->createCommand($sql)->execute();
                }
            }
            $transaction->commit();
            $rs['status']=true;
            return $rs;
        }
        catch (Exception $e)
        {
            //print_r($e->getMessage());
            $transaction->rollBack();
            $rs['msg']="更新失败！";
            return $rs;
        }
    }


    
}


    