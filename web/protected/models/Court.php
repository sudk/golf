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

    public static function Search($args = array()){

        $condition = ' 1=1 ';
        $params = array();

        if (isset($args->city)&&$args->city != ''){
            $condition.= ' AND g_court.city=:city ';
            $params['city'] = $args->city;
        }

        if (isset($args->court_id)&&$args->court_id != ''){
            $condition.= ' AND g_policy.court_id=:court_id ';
            $params['court_id'] = $args->court_id;
        }

        if (isset($args->key_word)&&$args->key_word != ''){
            $condition.= ' AND g_court.name like :key_word ';
            $params['key_word'] = "%".$args->key_word."%";
        }

        if (isset($args->date)&&$args->date != ''){
            $condition.= ' AND g_policy.start_date >= :start_date ';
            $params['start_date'] = $args->date;

            $condition.= ' AND g_policy.end_date >= :end_date ';
            $params['end_date'] = $args->date;

            $day=date("w",$args->date);
            $condition.= ' AND ( g_policy_detail.day < 0 or g_policy_detail.day = :day))';
            $params['day'] = $day;
        }

        if (isset($args->time)&&$args->time != ''){
            $condition.= ' AND ( g_policy_detail.end_time is null or ( g_policy_detail.start_time >= :start_time  AND g_policy_detail.end_time >= :end_time ))';
            $params['start_time'] = $args->time;
            $params['end_time'] = $args->time;
        }

        $condition.= ' AND g_policy.status=0 ';
        $condition.= ' AND g_policy_detail.status=1 ';
        if(isset($args->long_lat)){
            list($u_lon,$u_lat)=explode(",",$args->long_lat);
        }



        $rows = Yii::app()->db->createCommand()
            ->select("g_policy.*,g_court.name name,g_court.addr,g_court.lon lon,g_court.lat lat,g_policy_detail.price,g_policy_detail.day,g_policy_detail.start_time,g_policy_detail.end_time,g_agent.agent_name,g_img.img_url ico_img")
            ->from("g_policy_detail")
            ->leftJoin("g_policy","g_policy_detail.policy_id=g_policy.id")
            ->leftJoin("g_court","g_policy.court_id=g_court.court_id")
            ->leftJoin("g_agent","g_agent.id=g_policy.agent_id")
            ->leftJoin("g_img","g_img.type=8 and g_img.relation_id=g_court.court_id")
            ->where($condition,$params)
            ->queryAll();
        //return $row;
        $rows_tmp=array();

        if($rows){
            foreach($rows as $row){
                $court_id=$row['court_id'];
                if(isset($rows_tmp[$court_id])){
                    $row_tmp=$rows_tmp[$court_id];

                    if($row_tmp['type']==$row['type']&&$row_tmp['price']>$row['price']){
                        $rows_tmp[$court_id]=$row;
                        continue;
                    }

                    if($row_tmp['type']<$row['type']){
                        $rows_tmp[$court_id]=$row;
                        continue;
                    }
                }else{
                    $rows_tmp[$court_id]=$row;
                }
            }
        }

        if($rows_tmp){
            foreach($rows_tmp as $row){
                if(trim($row['lon'])&&trim($row['lat'])&&trim($u_lon)&&trim($u_lat)){

                    $row['distance']=BaiduDistance::GetLongDistance($row['lon'],$row['lat'],$u_lon,$u_lat);

                }else{
                    $row['distance']="未知";
                }
            }
        }

        return $rows_tmp;

    }

    public static function Info($court_id){
        $condition="court_id=:court_id";
        $row = Yii::app()->db->createCommand()
            ->select("*")
            ->from("g_court")
            ->where($condition,array(":court_id"=>$court_id))
            ->queryRow();
        if($row){
            $fairway_imgs=array();
            $court_imgs=array();
            $condition="relation_id=:relation_id and type in (0,1)";
            $rows = Yii::app()->db->createCommand()
                ->select("type,img_url")
                ->from("g_img")
                ->where($condition,array(":relation_id"=>$court_id))
                ->queryRow();
            if($rows){
                foreach($rows as $row){
                    if($row['type']==1){
                        $fairway_imgs[]=$row['img_url'];
                    }else{
                        $court_imgs[]=$row['img_url'];
                    }
                }
            }
            $row['court_imgs']=$court_imgs;
            $row['fairway_imgs']=$fairway_imgs;
            return $row;
        }else{
            return false;
        }
    }

    public static function Price($court_id,$type,$date_time){
        $condition = ' 1=1 ';
        $params = array();

        if (isset($args->date)&&$args->date != ''){
            $condition.= ' AND g_policy.start_date >= :start_date ';
            $params['start_date'] = $args->date;

            $condition.= ' AND g_policy.end_date >= :end_date ';
            $params['end_date'] = $args->date;

            $day=date("w",$args->date);
            $condition.= ' AND ( g_policy_detail.day < 0 or g_policy_detail.day = :day))';
            $params['day'] = $day;
        }

        if (isset($args->time)&&$args->time != ''){
            $condition.= ' AND ( g_policy_detail.end_time is null or ( g_policy_detail.start_time >= :start_time  AND g_policy_detail.end_time >= :end_time ))';
            $params['start_time'] = $args->time;
            $params['end_time'] = $args->time;
        }

        $condition.= ' AND g_policy.status=0 ';
        $condition.= ' AND g_policy_detail.status=1 ';

        $rows = Yii::app()->db->createCommand()
            ->select("g_policy.*,g_court.name name,g_court.addr,g_court.lon lon,g_court.lat lat,g_policy_detail.price,g_policy_detail.day,g_policy_detail.start_time,g_policy_detail.end_time,g_agent.agent_name,g_img.img_url ico_img")
            ->from("g_policy_detail")
            ->leftJoin("g_policy","g_policy_detail.policy_id=g_policy.id")
            ->leftJoin("g_court","g_policy.court_id=g_court.court_id")
            ->leftJoin("g_agent","g_agent.id=g_policy.agent_id")
            ->leftJoin("g_img","g_img.type=8 and g_img.relation_id=g_court.court_id")
            ->where($condition,$params)
            ->queryAll();
        //return $row;
        $rows_tmp=array();

        if($rows){
            foreach($rows as $row){
                $court_id=$row['court_id'];
                if(isset($rows_tmp[$court_id])){
                    $row_tmp=$rows_tmp[$court_id];

                    if($row_tmp['type']==$row['type']&&$row_tmp['price']>$row['price']){
                        $rows_tmp[$court_id]=$row;
                        continue;
                    }

                    if($row_tmp['type']<$row['type']){
                        $rows_tmp[$court_id]=$row;
                        continue;
                    }
                }else{
                    $rows_tmp[$court_id]=$row;
                }
            }
        }
        return $rows;
    }
}


    