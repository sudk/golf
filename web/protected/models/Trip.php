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
           '2'=>'押金',
         
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
            $condition.=' AND pay_type = :pay_type';
            $params['pay_type'] = $args['pay_type'];
        }
        
        
        if ($args['court_id'] != ''){
            $condition.=' AND court_id=:court_id';
            $params['court_id'] = $args['court_id'];
        }
        
        if ($args['agent_id'] != ''){
            $condition.=' AND agent_id=:agent_id';
            $params['agent_id'] = $args['agent_id'];
        }
        
        if ($args['trip_name'] != ''){
            $condition.=' AND trip_name like :trip_name';
            $params['trip_name'] = "%".$args['trip_name']."%";
        }


        $total_num = Yii::app()->db->createCommand()
            ->select("count(1)")
            ->from("g_trip")
            ->where($condition,$params)
            ->queryScalar();
        $order = 'record_time  DESC';
        $rows=Yii::app()->db->createCommand()
            ->select("*")
            ->from("g_trip")
            ->where($condition,$params)
            ->order($order)
            ->limit($pageSize)
            ->offset($page * $pageSize)
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
            $condition.=' AND id = :id';
            $params['id'] =$id;
        }else{
            return false;
        }

        $row=Yii::app()->db->createCommand()
            ->select("*")
            ->from("g_trip")
            ->where($condition,$params)
            ->queryRow();
        if($row){
            $row['imgs']=Img::GetImgs($id,Img::TYPE_TRIP);
        }
        return $row;

    }
    
}


    