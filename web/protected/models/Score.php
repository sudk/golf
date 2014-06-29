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

        $condition.=' AND g_score.user_id = :user_id';
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


    public static function HandicapList($page, $pageSize){
        $condition = ' 1=1 ';
        $params = array();
        $condition.= ' AND g_score.handicap is not null';
        $condition.= ' AND g_score.is_show = '.self::SHOW_YES;
        $order = 'handicap DESC';
        return self::HandicapRows($page,$pageSize,$params,$condition,$order);
    }

    public static function PersonalHandicapList(){
        $condition = ' 1=1 ';
        $params = array();
        $condition.= ' AND g_score.handicap is not null';

        $condition.=' AND g_score.user_id = :user_id';
        $params['user_id'] = Yii::app()->user->id;
        $order = 'fee_time desc,handicap DESC';
        return self::HandicapRows(0,10,$params,$condition,$order);
    }

    private static function HandicapRows($page,$pageSize,$params,$condition,$order){

        //print_r($args);
        $rows=Yii::app()->db->createCommand()
            ->select("g_score.fee_time,g_score.handicap,g_court.name court_name,g_user.user_name")
            ->from("g_score")
            ->leftJoin("g_court","g_score.court_id=g_court.court_id")
            ->leftJoin("g_user","g_score.user_id=g_user.user_id")
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

    public static function CalculateHandicap(&$conn,$id){
        $score_details=Yii::app()->db->createCommand()
            ->select("*")
            ->from("g_score_detail")
            ->where("score_id='{$id}'")
            ->queryAll();
        if(count($score_details)<18){
            return;
        }
        /*差点计算
         *  新新贝利亚计算法
            A、从18洞中任选12洞的杆数总和×1.5=总数
            B、从此总杆数减去标准杆后×0.8=差点
            即（12洞杆数总和×1.5—标准杆）×0.8=差点
            C、净杆=总杆-差点
         * */

        $hosts=range(1,18);
        //shuffle 将数组顺序随即打乱
        shuffle($hosts);
        //获得要丢弃的6个洞杆数
        $give_up_hosts = array_slice($hosts,0,6);

        $standard=0;
        $active=0;
        foreach($score_details as $score_detail){
            $standard+=$score_detail['standard_bar'];
            $hole_no=$score_detail['hole_no'];
            if(!in_array($hole_no,$give_up_hosts)){
                $active+=$score_detail['lang_bar']+$score_detail['push_bar'];
            }
        }
        $handicap=(($active*1.5)-$standard)*0.8;
        $sql="update g_score set handicap=:handicap where id=:id";
        $command = $conn->createCommand($sql);
        $command->bindParam(":handicap",$handicap, PDO::PARAM_STR);
        $command->bindParam(":id",$id, PDO::PARAM_STR);
        $command->execute();

    }
}


    