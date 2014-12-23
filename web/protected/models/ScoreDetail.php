<?php
/**
 * --请填写模块名称--
 *
 * @author #guohao 
 * @copyright Copyright &copy; 2003-2009 TrunkBow Co., Inc
 */
class ScoreDetail extends CActiveRecord {

   
    

    public static function model($className=__CLASS__){
        return parent::model($className);
    }

    public function tableName(){
        return 'g_score_detail';
    }

    public function rules(){
        return array(
          
         );
    }


    //Tee台；1、黑Tee；2、金Tee；3、蓝Tee；4、白Tee；5、红Tee
    public static function getTee()
    {
        return array(
            '1'=>'黑Tee',
            '2'=>'金Tee',
            '3'=>'蓝Tee',
            '4'=>'白Tee',
            '5'=>'红Tee',
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

        if ($args['score_id'] != ''){
            $condition.= ' AND score_id=:score_id';
            $params['score_id'] = $args['score_id'];
        }
        
        
        $total_num = ScoreDetail::model()->count($condition, $params); //总记录数

        $criteria = new CDbCriteria();
        $criteria->order = 'record_time  DESC';
    	

        $criteria->condition = $condition;
        $criteria->params = $params;

        $pages = new CPagination($total_num);
        $pages->pageSize = $pageSize;
        $pages->setCurrentPage($page);
        $pages->applyLimit($criteria);

        $rows = ScoreDetail::model()->findAll($criteria);

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

        if (isset($args->score_id)&&$args->score_id != ''){
            $condition.=' AND score_id = :score_id';
            $params['score_id'] = $args->score_id;
        }

        $order = 'hole_no';

        $rows=Yii::app()->db->createCommand()
            ->select("*")
            ->from("g_score_detail")
            ->where($condition,$params)
            ->order($order)
            ->limit($pageSize)
            ->offset($page * $pageSize)
            ->queryAll();

        return $rows;
    }

    public static function Info($id) {
        $row=Yii::app()->db->createCommand()
            ->select("*")
            ->from("g_score_detail")
            ->where("id=:id",array(":id"=>$id))
            ->queryRow();
        return $row;
    }
}


    