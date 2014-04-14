<?php
/**
 * --请填写模块名称--
 *
 * @author #guohao 
 * @copyright Copyright &copy; 2003-2009 TrunkBow Co., Inc
 */
class Court extends CActiveRecord {

   
    public $province;

    public static function model($className=__CLASS__){
        return parent::model($className);
    }

    public function tableName(){
        return 'g_court';
    }

    public function rules(){
        return array(
          array('court_id,name,city,area,addr,model,lon,lat,create_year,area,green_grass,court_data,designer,fairway_length,fairway_grass,phone,creatorid', 'safe', 'on' => 'create'),
            array('court_id,name,city,area,addr,model,lon,lat,create_year,area,green_grass,court_data,designer,fairway_length,fairway_grass,phone,creatorid', 'safe', 'on' => 'modify'),
         );
    }

    public static function getCourtModel()
    {
//        $rs = array(
//            '滨海球场'=>'滨海球场',
//            '林克斯球场'=>'林克斯球场',
//            '欧石南荒地球场'=>'欧石南荒地球场',
//            '平原球场'=>'平原球场',
//           
//        );
        $rs = array();
        $sql = "select distinct model from g_court ";
        $rows = Yii::app()->db->createCommand($sql)->queryAll();
        if($rows)
        {
            foreach($rows as $row)
            {
                $rs[$row['model']] = $row['model'];
            }
        }
        return $rs;
    }
    
    
    public static function getCourtArray()
    {
        $rows = Court::model()->findAll();
        $data = array();
        if($rows)
        {
            foreach($rows as $row)
            {
                $data[$row['court_id']] = $row['name'];
            }
        }
        
        return $data;
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
        
        
        
        $total_num = Court::model()->count($condition, $params); //总记录数

        $criteria = new CDbCriteria();
        
    	if($_REQUEST['q_order']==''){
            $criteria->order = 'record_time  DESC';
        }else{
            if(substr($_REQUEST['q_order'],-1)=='~')
                $criteria->order = substr($_REQUEST['q_order'],0,-1).' DESC';
            else
                $criteria->order = $_REQUEST['q_order'].' ASC';
        }

        $criteria->condition = $condition;
        $criteria->params = $params;

        $pages = new CPagination($total_num);
        $pages->pageSize = $pageSize;
        $pages->setCurrentPage($page);
        $pages->applyLimit($criteria);

        $rows = Court::model()->findAll($criteria);

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


    