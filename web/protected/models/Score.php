<?php
/**
 * --请填写模块名称--
 *
 * @author #guohao 
 * @copyright Copyright &copy; 2003-2009 TrunkBow Co., Inc
 */
class Score extends CActiveRecord {

   
    

    public static function model($className=__CLASS__){
        return parent::model($className);
    }

    public function tableName(){
        return 'g_score';
    }

    public function rules(){
        return array(
          
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

        
        $total_num = Score::model()->count($condition, $params); //总记录数

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

        $rows = Score::model()->findAll($criteria);

        $rs['status'] = 0;
        $rs['desc'] = '成功';
        $rs['page_num'] = ($pages->currentPage + 1);
        $rs['total_num'] = $total_num;
        $rs['total_page'] = ceil($rs['total_num'] / $rs['page_num']);
        $rs['num_of_page'] = $pages->pageSize;
        $rs['rows'] = $rows;

        return $rs;
    }

    public static function InfoList($page, $pageSize, $args = array()) {

        $condition = ' 1=1 ';
        $params = array();

        $condition.=' AND user_id = :user_id';
        $params['user_id'] = Yii::app()->user->id;

        $order = 'record_time DESC';
        //print_r($args);
        $rows=Yii::app()->db->createCommand()
            ->select("g_score.*,g_court.name court_name")
            ->from("g_score")
            ->leftJoin("g_court","g_score.court_id=g_court.court_id")
            ->where($condition,$params)
            ->order($order)
            ->limit($pageSize)
            ->offset($page * $pageSize)
            ->queryAll();

        return $rows;
    }

    public static function Info($id) {
        $row=Yii::app()->db->createCommand()
            ->select("g_score.*,g_court.name court_name")
            ->from("g_score")
            ->leftJoin("g_court","g_score.court_id=g_court.court_id")
            ->where("id=:id",array(":id"=>$id))
            ->queryRow();
        return $row;
    }
}


    