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
        
        if ($args['model'] != ''){
            $condition.= ' AND model=:model';
            $params['model'] = $args['model'];
        }
        
        if ($args['name'] != ''){
            $condition.= ' AND name like :name';
            $params['name'] = "%".$args['name']."%";
        }
        //var_dump($condition);
        
        
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
    
    /**
     * 球场删除
     * 包括 
     * 球场信息
     * 球场的图片
     * 球场的评论
     * 球场的附近设施
     * 球场的比赛。
     * 球场的预约信息
     * @param type $court_id
     * @return type
     */
    public static function deleteCourt($court_id)
    {
        if(!$court_id){
            return array('status'=>false,'msg'=>'删除失败，球场不存在');
        }
        $conn=Yii::app()->db;
        $transaction = $conn->beginTransaction();
        
        $info = $conn->createCommand()->select("*")->from("g_court")->where("court_id=:court_id",array('court_id'=>$court_id))->queryRow();
        if(!info){
            return array('status'=>false,'msg'=>'删除失败，球场不存在');
        }
        
        $order_info = $conn->createCommand()->select("*")->from("g_order")->where("court_id=:court_id",array('court_id'=>$court_id))->queryRow();
        if(!$order_info){
            return array('status'=>false,'msg'=>'删除失败，本球场有预约信息');
        }
        
        $comp_info = $conn->createCommand()->select("*")->from("g_competition")->where("court_id=:court_id",array('court_id'=>$court_id))->queryRow();
        if(!$comp_info){
            return array('status'=>false,'msg'=>'删除失败，本球场有赛事信息');
        }
        
        try{
            //球场
            $sql = "delete from g_court where court_id=:court_id";
            $command = $conn->createCommand($sql);
            $command->bindParam(":court_id", $court_id, PDO::PARAM_STR);
            $command->execute();
            //图片
            $sql = "delete from g_img where relation_id=:relation_id and type in('0','1','8')";
            $command = $conn->createCommand($sql);
            $command->bindParam(":relation_id", $court_id, PDO::PARAM_STR);
            $command->execute();
            //球场附近设施
            $sql = "delete from g_court_facilities where court_id=:court_id";
            $command = $conn->createCommand($sql);
            $command->bindParam(":court_id", $court_id, PDO::PARAM_STR);
            $command->execute();
            //球场评论
            $sql = "delete from g_comment where court_id=:court_id";
            $command = $conn->createCommand($sql);
            $command->bindParam(":court_id", $court_id, PDO::PARAM_STR);
            $command->execute();         
            
            $transaction->commit();
            //删除球场风景，球道，附近设施图片，logo图片
            $rs = Img::delImg($court_id, '0');
            $rs = Img::delImg($court_id, '1');
            $rs = Img::delImg($court_id, '2');
            $rs = Img::delImg($court_id, '8');
            
            return array('status'=>true,'msg'=>'删除成功');
        } catch (Exception $ex) {
            $transaction->rollBack();
            return array('status'=>false,'desc'=>'删除失败，未知错误');
        }
        return array('status'=>true,'msg'=>'删除成功');
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
            $condition.= ' AND g_policy.start_date <= :date ';
            $condition.= ' AND g_policy.end_date >= :date ';
            $params['date'] = $args->date;

            $day=date("w",strtotime($args->date));
            $condition.= ' AND ( g_policy_detail.day < 0 or g_policy_detail.day = :day )';
            $params['day'] = $day;
        }

        if (isset($args->time)&&$args->time != ''){
            $condition.= ' AND ( g_policy_detail.end_time = \'\' or ( g_policy_detail.start_time <= :time  AND g_policy_detail.end_time >= :time ))';
            $params['time'] = $args->time;
        }

        $condition.= " AND g_policy.status=".Policy::STATUS_NORMAL;
        $condition.= ' AND g_policy_detail.status='.PolicyDetail::STATUS_NORMAL;
        if(isset($args->long_lat)){
            list($u_lon,$u_lat)=explode(",",$args->long_lat);
        }


        //print_r($condition);
        //print_r($params);
        $rows = Yii::app()->db->createCommand()
            ->select("g_policy.*,g_court.name name,g_court.addr,g_court.lon lon,g_court.lat lat,g_policy_detail.price,g_policy_detail.day,g_policy_detail.start_time,g_policy_detail.end_time,g_agent.agent_name,g_img.img_url ico_img")
            ->from("g_policy_detail")
            ->leftJoin("g_policy","g_policy_detail.policy_id=g_policy.id")
            ->leftJoin("g_court","g_policy.court_id=g_court.court_id")
            ->leftJoin("g_agent","g_agent.id=g_policy.agent_id")
            ->leftJoin("g_img","g_img.type=8 and g_img.relation_id=g_court.court_id")
            ->where($condition,$params)
            ->queryAll();
        //print_r($rows);
        //return $row;
        $rows_tmp=array();

        if($rows){//先出球场中的最低有效报价
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

        $rows=array();

        if($rows_tmp){
            foreach($rows_tmp as $row){
                if(trim($row['lon'])&&trim($row['lat'])&&trim($u_lon)&&trim($u_lat)){
                    $row['distance']=BaiduDistance::GetLongDistance($row['lon'],$row['lat'],$u_lon,$u_lat);
                }else{
                    $row['distance']="未知";
                }
                if($row['ico_img']){
                    $row['ico_img']=Img::IMG_PATH.$row['ico_img'];
                }
                $rows[]=$row;
            }
        }

        return $rows;

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
                ->queryAll();
            if($rows){
                foreach($rows as $row_img){
                    if(intval($row_img['type'])==1){
                        $fairway_imgs[]=Img::IMG_PATH.$row_img['img_url'];
                    }else{
                        $court_imgs[]=Img::IMG_PATH.$row_img['img_url'];
                    }
                }
            }
            $row['court_imgs']=$court_imgs;
            $row['fairway_imgs']=$fairway_imgs;
            $comment_total=Comment::ComAvg($court_id);
            if($comment_total){
                $row['comment']=$comment_total;
            }else{
                $row['comment']=array("comment_count"=>0,"service_total"=>0,"design_total"=>0,"facilitie_total"=>0,"lawn_total"=>0);
            }

        }
        return $row;
    }

    public static function Price($args){
        //$court_id,$type,$date_time
        $condition = ' 1=1 ';
        $params = array();

        if (isset($args->court_id)&&$args->court_id != ''){
            $condition.= ' AND g_policy.court_id=:court_id ';
            $params['court_id'] = $args->court_id;
        }

        if (isset($args->date_time)&&$args->date_time != ''){
            $d=date("Y-m-d",strtotime($args->date_time));
            $condition.= ' AND g_policy.start_date <= :date ';
            $condition.= ' AND g_policy.end_date >= :date ';
            $params['date'] = $d;

            $day=date("w",strtotime($args->date_time));
            $condition.= ' AND ( g_policy_detail.day < 0 or g_policy_detail.day = :day)';
            $params['day'] = $day;

            $t=date("H:i",strtotime($args->date_time));
            $condition.= ' AND ( g_policy_detail.end_time = \'\' or ( g_policy_detail.start_time <= :time  AND g_policy_detail.end_time >= :time ))';
            $params['time'] = $t;
        }

        if (isset($args->type)&&$args->type != ''){
            $condition.= ' AND g_policy.type >= :type ';
            $params['type'] = $args->type;
        }

        $condition.= " AND g_policy.status=".Policy::STATUS_NORMAL;
        $condition.= ' AND g_policy_detail.status='.PolicyDetail::STATUS_NORMAL;

        $rows = Yii::app()->db->createCommand()
            ->select("g_policy.*,g_court.name court_name,g_policy_detail.price,g_policy_detail.day,g_policy_detail.start_time,g_policy_detail.end_time,g_agent.agent_name")
            ->from("g_policy_detail")
            ->leftJoin("g_policy","g_policy_detail.policy_id=g_policy.id")
            ->leftJoin("g_court","g_policy.court_id=g_court.court_id")
            ->leftJoin("g_agent","g_agent.id=g_policy.agent_id")
            ->where($condition,$params)
            ->queryAll();

        //print_r($rows);

        $rows_tmp=array();

        if($rows){
            foreach($rows as $row){//筛选每个代理商在这一时段的有效报价
                $agent_id=$row['agent_id'];
                if(isset($rows_tmp[$agent_id])){
                    $row_tmp=$rows_tmp[$agent_id];
                    if($row_tmp['type']==$row['type']&&$row_tmp['price']>$row['price']){
                        $rows_tmp[$agent_id]=$row;
                        continue;
                    }
                    if($row_tmp['type']<$row['type']){
                        $rows_tmp[$agent_id]=$row;
                        continue;
                    }
                }else{
                    $rows_tmp[$agent_id]=$row;
                }
            }
        }
        $rows=array();
        if($rows_tmp){
            if($args->type==Policy::TYPE_FOVERABLE){//如果是只查询优惠报价的话那么，要把特殊报价去掉
                foreach($rows_tmp as $row){
                    if($row['type']!=Policy::TYPE_SPECIAL){
                        $rows[]=$row;
                    }
                }
            }else{
                foreach($rows_tmp as $row){
                    $rows[]=$row;
                }
            }
        }
        return $rows;
    }

    public static function InfoList($page, $pageSize, $args = array()) {

        $condition = ' 1=1 ';
        $params = array();

        if (isset($args->court_id)&&$args->court_id != ''){
            $condition.=' AND court_id = :court_id';
            $params['court_id'] = $args->court_id;
        }

        if (isset($args->name)&&$args->name != ''){
            $condition.= ' AND name like :name';
            $params['name'] = "%".$args->name."%";
        }

        if (isset($args->city)&&$args->city != ''){
            $condition.= ' AND city = :city';
            $params['city'] = $args->city;
        }


        $order = 'record_time  DESC';

        $rows=Yii::app()->db->createCommand()
            ->select("court_id,city,name")
            ->from("g_court")
            ->where($condition,$params)
            ->order($order)
            ->limit($pageSize)
            ->offset($page * $pageSize)
            ->queryAll();
        return $rows;
    }


    public static function Sale($args = array()){

        $condition = ' 1=1 ';
        $params = array();

        $condition.= ' AND g_court.city=:city ';
        $params['city'] = $args->city;

        if (isset($args->court_id)&&$args->court_id != ''){
            $condition.= ' AND g_policy.court_id=:court_id ';
            $params['court_id'] = $args->court_id;
        }

        if (isset($args->key_word)&&$args->key_word != ''){
            $condition.= ' AND g_court.name like :key_word ';
            $params['key_word'] = "%".$args->key_word."%";
        }

        $weeksToDates=Utils::WeeksToDates();

        if (isset($args->day)&&$args->day != ''){
            $d=$weeksToDates[$args->day];
            $condition.= ' AND g_policy.start_date <= :date ';
            $condition.= ' AND g_policy.end_date >= :date ';
            $params['date'] = $d;

            //$day=date("w",strtotime($args->date));
            $condition.= ' AND ( g_policy_detail.day < 0 or g_policy_detail.day = :day )';
            $params['day'] = $args->day;
        }

        $condition.= ' AND g_policy.type <= :type ';
        $params['type'] = Policy::TYPE_FOVERABLE;


        $condition.= " AND g_policy.status=".Policy::STATUS_NORMAL;
        $condition.= ' AND g_policy_detail.status='.PolicyDetail::STATUS_NORMAL;

        if(isset($args->long_lat)){
            list($u_lon,$u_lat)=explode(",",$args->long_lat);
        }


        //print_r($condition);
        //print_r($params);
        $rows = Yii::app()->db->createCommand()
            ->select("g_policy.*,g_court.name name,g_court.addr,g_court.lon lon,g_court.lat lat,g_policy_detail.price,g_policy_detail.day,g_policy_detail.start_time,g_policy_detail.end_time,g_agent.agent_name,g_img.img_url ico_img")
            ->from("g_policy_detail")
            ->leftJoin("g_policy","g_policy_detail.policy_id=g_policy.id")
            ->leftJoin("g_court","g_policy.court_id=g_court.court_id")
            ->leftJoin("g_agent","g_agent.id=g_policy.agent_id")
            ->leftJoin("g_img","g_img.type=8 and g_img.relation_id=g_court.court_id")
            ->where($condition,$params)
            ->queryAll();

        $rows_tmp=array();
        $normal_price=array();
        if($rows){
            foreach($rows as $row){
                if($row['type']==Policy::TYPE_NORMAL){
                    $normal_price[$row['court_id']]=$row['price'];
                }else{
                    $rows_tmp[]=$row;
                }
            }
        }


        $return_rows=array();
        if($rows_tmp){
            foreach($rows_tmp as $row){
                if(trim($row['lon'])&&trim($row['lat'])&&trim($u_lon)&&trim($u_lat)){
                    $row['distance']=BaiduDistance::GetLongDistance($row['lon'],$row['lat'],$u_lon,$u_lat);
                }else{
                    $row['distance']="未知";
                }
                if($row['ico_img']){
                    $row['ico_img']=Img::IMG_PATH.$row['ico_img'];
                }
                $row['normal_price']=$normal_price[$row['court_id']];
                $row['date']=$weeksToDates[$args->day];

                $return_rows[]=$row;
            }
        }

        return $return_rows;

    }
}


    