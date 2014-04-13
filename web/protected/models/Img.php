<?php
/**
 * --请填写模块名称--
 *
 * @author #author#
 * @copyright Copyright &copy; 2003-2009 TrunkBow Co., Inc
 */
class Img extends CActiveRecord {

    public static function model($className=__CLASS__){
        return parent::model($className);
    }

    public function tableName(){
        return 'g_img';
    }
    
    public static function  getType($type=null)
    {
        $rs = array(
            '0'=>'球场风景',
            '1'=>'球道',
            '2'=>'球场附近设施',
            '3'=>'赛事图片',
            '4'=>'寄卖物品',
            '5'=>'会员卡',
            '6'=>'行程',
            '7'=>'广告',
        );
        
        return $type?$rs[$type]:$rs;
    }
    
    public static function queryList($page,$pageSize,$args)
    {
        $params = array();
        $condition= "";
        if ($args['relation_id'] != ''){
            $condition.= ' relation_id=:relation_id';
            $params['relation_id'] = $args['relation_id'];
        }
        
        if ($args['type'] != ''){
            $condition.= ' type=:type';
            $params['type'] = $args['type'];
        }
        
        
        
        $total_num = Img::model()->count($condition, $params); //总记录数

        $criteria = new CDbCriteria();
        $criteria->order = 'type,record_time  DESC';
    	

        $criteria->condition = $condition;
        $criteria->params = $params;

        $pages = new CPagination($total_num);
        $pages->pageSize = $pageSize;
        $pages->setCurrentPage($page);
        $pages->applyLimit($criteria);

        $rows = Img::model()->findAll($criteria);

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


    