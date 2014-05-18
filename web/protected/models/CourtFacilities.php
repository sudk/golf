<?php
/**
 * --请填写模块名称--
 *寄卖商品模型
 * @author #guohao <sudk@trunkbow.com>#
 * @copyright Copyright &copy; 2003-2009 TrunkBow Co., Inc
 */
class CourtFacilities extends CActiveRecord {

    const TYPE_FOOD=1;
    const TYPE_LIVE=2;
    const TYPE_PLAY=3;
    const TYPE_SHOPING=4;
    const TYPE_MEETING=5;
    const TYPE_ORDER=6;
    //1、美食；2、住宿；3、娱乐；4、购物；5、会议；6、其它

    public static function model($className=__CLASS__){
        return parent::model($className);
    }

    public function tableName(){
        return 'g_court_facilities';
    }

    public function rules(){
        return array(
            array('court_id,facilitie_name,type,feature,consumption,favourable,phone,addr,distance,record_time', 'safe', 'on' => 'create'),
            array('court_id,facilitie_name,type,feature,consumption,favourable,phone,addr,distance,record_time', 'safe', 'on' => 'modify'),
         );
    }


   

    /**
     * 吃住行游购娱
     * @param type $s
     */
   public static function getType($s = null)
   {
       $rs = array(
           self::TYPE_FOOD=>'美食',
           self::TYPE_LIVE=>'住宿',
           self::TYPE_PLAY=>'娱乐',
           self::TYPE_SHOPING=>'购物',
           self::TYPE_MEETING=>'会议',
           self::TYPE_ORDER=>'其它',
       );
       
       return $s ? $rs[$s] : $rs;
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

        
        if ($args['facilite_name'] != ''){
            $condition.=' AND facilite_name like :facilite_name';
            $params['facilite_name'] = "%".$args['facilite_name']."%";
        }
        
        
        if ($args['type'] != ''){
            $condition.=' AND type=:type';
            $params['type'] = $args['type'];
        }
        
        if ($args['court_id'] != ''){
            $condition.=' AND court_id=:court_id';
            $params['court_id'] = $args['court_id'];
        }
        
        
        //$total_num = CourtFacilities::model()->count($condition, $params); //总记录数
        $total_num = Yii::app()->db->createCommand()
            ->select("count(1)")
            ->from("g_court_facilities")
            ->where($condition,$params)
            ->queryScalar();

        $criteria = new CDbCriteria();
        
    	
        $order = 'record_time  DESC';
        

        $criteria->condition = $condition;
        $criteria->params = $params;

        $pages = new CPagination($total_num);
        $pages->pageSize = $pageSize;
        $pages->setCurrentPage($page);
        $pages->applyLimit($criteria);

        //$rows = CourtFacilities::model()->findAll($criteria);
        $rows=Yii::app()->db->createCommand()
            ->select("*")
            ->from("g_court_facilities")
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

        if (isset($args->court_id)&&$args->court_id != ''){
            $condition.=' AND g_court_facilities.court_id = :court_id';
            $params['court_id'] = $args->court_id;
        }
        if (isset($args->type)&&$args->type != ''){
            $condition.=' AND g_court_facilities.type = :type';
            $params['type'] = $args->type;
        }

        if (isset($args->city)&&$args->city != ''){
            $condition.=' AND g_court.city = :city';
            $params['city'] = $args->city;
        }

        if (isset($args->facilitie_name)&&$args->facilitie_name != ''){
            $condition.=' AND g_court_facilities.facilitie_name like :facilitie_name';
            $params['facilitie_name'] = "%".$args->facilitie_name."%";
        }

        if (isset($args->court_name)&&$args->court_name != ''){
            $condition.=' AND g_court.name like :court_name';
            $params['court_name'] = "%".$args->court_name."%";
        }

        $order = 'record_time DESC';

        $rows=Yii::app()->db->createCommand()
            ->select("g_court_facilities.*,g_court.name court_name,g_court.city city")
            ->from("g_court_facilities")
            ->leftJoin('g_court',"g_court_facilities.court_id=g_court.court_id")
            ->where($condition,$params)
            ->order($order)
            ->limit($pageSize)
            ->offset($page * $pageSize)
            ->queryAll();

        if($rows){
            $rows_tmp=array();
            foreach($rows as $row){
                $row['imgs']=Img::GetImgs($row['id'],Img::TYPE_COURT_FACILITIES);
                $rows_tmp[]=$row;
            }
        }

        return $rows_tmp;
    }


    public static function Info($id) {

        $condition = ' 1=1 ';
        $params = array();


        if ($id != ''){
            $condition.=' AND g_court_facilities.id = :id';
            $params['id'] =$id;
        }else{
            return false;
        }

        $row=Yii::app()->db->createCommand()
            ->select("g_court_facilities.*,g_court.name court_name,g_court.city city")
            ->from("g_court_facilities")
            ->leftJoin('g_court',"g_court_facilities.court_id=g_court.court_id")
            ->where($condition,$params)
            ->queryRow();
        if($row){
            $row['imgs']=Img::GetImgs($id,Img::TYPE_COURT_FACILITIES);
        }
        return $row;

    }


    
    
    
    
    
}


    