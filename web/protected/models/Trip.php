<?php
/**
 * --请填写模块名称--
 *行程模型
 * @author #guohao <sudk@trunkbow.com>#
 * @copyright Copyright &copy; 2003-2009 TrunkBow Co., Inc
 */
class Trip extends CActiveRecord {

    public $province;
    

    public static function model($className=__CLASS__){
        return parent::model($className);
    }

    public function tableName(){
        return 'g_trip';
    }

    public function rules(){
        return array();
    }


   

    /**
     * 
     * @param type $s
     */
   public static function getPayType($s = null)
   {
       $rs = array(
           '0'=>'现付',
           '1'=>'全额预付',
           '2'=>'部分预付',
         
       );
       
       return $s=="" ? $rs : $rs[$s];
   }
   
   /**
    * 是否需要确认
    * @param type $s
    * @return type
    */
   public static function getIsCheck($s = null)
   {
       $rs = array(
           '0'=>'是',
           '1'=>'否'
       );
       
       return $s!=null ? $rs[$s] : $rs;
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

        
        if ($args['pay_type'] != ''){
            $condition.=' AND g_trip.pay_type = :pay_type';
            $params['pay_type'] = $args['pay_type'];
        }
        
        
        if ($args['court_id'] != ''){
            $condition.=' AND g_trip.court_id=:court_id';
            $params['court_id'] = $args['court_id'];
        }

        if ($args['city'] != ''){
            $condition.=' AND g_trip.city=:city';
            $params['city'] = $args['city'];
        }
        
        if ($args['agent_id'] != ''){
            $condition.=' AND g_trip.agent_id=:agent_id';
            $params['agent_id'] = $args['agent_id'];
        }
        
        if ($args['trip_name'] != ''){
            $condition.=' AND g_trip.trip_name like :trip_name';
            $params['trip_name'] = "%".$args['trip_name']."%";
        }

        
        $total_num = Yii::app()->db->createCommand()
            ->select("count(1)")
            ->from("g_trip")
            ->leftJoin("g_court","g_trip.court_id=g_court.court_id")
            ->leftJoin("g_agent","g_trip.agent_id=g_agent.id")
            ->where($condition,$params)
            ->queryScalar();
        $order = 'g_trip.record_time DESC';
        $rows=Yii::app()->db->createCommand()
            ->select("g_trip.*,g_court.name court_name,g_agent.agent_name,concat('".Img::IMG_PATH."',g_img.img_url) img")
            ->from("g_trip")
            ->leftJoin("g_court","g_trip.court_id=g_court.court_id")
            ->leftJoin("g_agent","g_trip.agent_id=g_agent.id")
            ->leftJoin("g_img","g_trip.id=g_img.relation_id and g_img.type=".Img::TYPE_TRIP)
            ->where($condition,$params)
            ->order($order)
            ->limit($pageSize)
            ->offset($page * $pageSize)
            ->group("g_trip.id")
            ->queryAll();

        $rs['status'] = 0;
        $rs['desc'] = '成功';
        $rs['page_num'] = ($pages->currentPage + 1);
        $rs['total_num'] = $total_num;
        $rs['total_page'] = ceil($total_num/ $pageSize);
        $rs['num_of_page'] = $pageSize;
        $rs['rows'] = $rows;

        return $rs;
    }

    public static function Info($id) {

        $condition = ' 1=1 ';
        $params = array();


        if ($id != ''){
            $condition.=' AND g_trip.id = :id';
            $params['id'] =$id;
        }else{
            return false;
        }

        $row=Yii::app()->db->createCommand()
            ->select("g_trip.*,g_court.name court_name,g_agent.agent_name")
            ->from("g_trip")
            ->leftJoin("g_court","g_trip.court_id=g_court.court_id")
            ->leftJoin("g_agent","g_trip.agent_id=g_agent.id")
            ->where($condition,$params)
            ->queryRow();
        if($row){
            $row['imgs']=Img::GetImgs($id,Img::TYPE_TRIP);
        }
        return $row;

    }
    
}


    