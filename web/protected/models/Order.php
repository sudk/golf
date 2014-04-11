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
    
    //0、待确认；1、待付款；2、完成；3、撤销
    const STATUS_TOBE_CONFIRM = '0';
    const STATUS_TOBE_PAID = '1';
    const STATUS_TOBE_SUCCESS = '2';
    const STATUS_TOBE_CANCEL = '3';

    public static function model($className=__CLASS__){
        return parent::model($className);
    }

    public function tableName(){
        return 'g_order';
    }

    public function rules(){
        return array(
          
                );
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
            self::STATUS_TOBE_SUCCESS=>'预约成功',
            self::STATUS_TOBE_CANCEL=>'订单撤销'
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

        if ($args['court_id'] != ''){
            $condition.= ' AND court_id=:court_id';
            $params['court_id'] = $args['court_id'];
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
}


    