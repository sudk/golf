<?php
/**
 * --请填写模块名称--
 *
 * @author #guohao 
 * @copyright Copyright &copy; 2003-2009 TrunkBow Co., Inc
 */
class Score extends CActiveRecord {

   
    const SHOW_YES = '1';
    const SHOW_NO = '0';
    

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


    public static function getIsShow()
    {
        return array(
            self::SHOW_YES =>'是',
            self::SHOW_NO =>'否'
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
        
        if ($args['user_id'] != ''){
            $condition.= ' AND g_score.user_id=:user_id';
            $params['user_id'] = $args['user_id'];
        }
        
        if($args['phone']!="")
        {
            $condition.= ' AND g_user.phone=:phone';
            $params['phone'] = $args['phone'];
        }

        $total_num=Yii::app()->db->createCommand()
            ->select("count(1)")
            ->from("g_score")
            ->leftJoin("g_user","g_score.user_id=g_user.user_id")
            ->where($condition,$params)
            
            ->queryScalar();
      
        $order = 'g_score.record_time DESC';
        //print_r($args);
        $rows=Yii::app()->db->createCommand()
            ->select("g_score.*,g_user.phone user_phone")
            ->from("g_score")
            ->leftJoin("g_user","g_score.user_id=g_user.user_id")
            ->where($condition,$params)
            ->order($order)
            ->limit($pageSize)
            ->offset($page * $pageSize)
            ->queryAll();

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


    