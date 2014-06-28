<?php
/**
 * --请填写模块名称--
 *
 * @author #guohao 
 * @copyright Copyright &copy; 2003-2009 TrunkBow Co., Inc
 */
class User extends CActiveRecord {

    const STATUS_NORMAL=0;//正常
    const STATUS_DISABLE=-1;//禁用

    const SEX_MEN="0";
    const SEX_WOMEN="1";


    const VIP_STATUS_NO=0;//非会员
    const VIP_STATUS_NORMAL=1;//VIP
    const VIP_STATUS_EXPIRE=-1;//已过期

    

    public static function model($className=__CLASS__){
        return parent::model($className);
    }

    public function tableName(){
        return 'g_user';
    }

    public function rules(){
        return array(
             //安全性
            array('user_id,user_name,phone,card_no,email,sex,remark,record_time,status,balance,point', 'safe', 'on' => 'modify'),
            array('user_id,user_name,phone,card_no,passwd,email,sex,remark,record_time,status,balance,point', 'safe', 'on' => 'create'),
            array('phone,passwd','required','on'=>'create'),
            //array('password', 'compare', 'compareAttribute'=>'passwordc', 'on'=>'create,modify'),
         );
    }


    public static function  GetSex($s = "")
    {
        $ar=array(
            "0"=>"男",
            "1"=>"女",
        );
        return trim($s)!=""?$ar[$s]:$ar;
    }

    public static function  GetStatus($s = "")
    {
        $ar=array(
            self::STATUS_NORMAL=>"正常",
            self::STATUS_DISABLE=>"禁用",
        );
        return trim($s)!=""?$ar[$s]:$ar;
    }


    /**
     * 删除
     * @param  string authitemid
     * @return array
     */
    public function delete($authitemid=null) {

    	$authitemid = trim($authitemid);

    	//检查非空性
    	if($authitemid == ''){
    	   $r['message'] = '主键为空，不能删除';
           $r['refresh'] = false;
           return $r;
    	}

        $sql = 'DELETE FROM operator WHERE mchtid=:mchtid';
        $command = Yii::app()->db->createCommand($sql);
        $command->bindParam(":authitemid", $authitemid, PDO::PARAM_STR);
        $rs = $command->execute();

        if ($rs == 0)
        {
            $r['message'] = '您要删除的记录不存在！';
            $r['refresh'] = false;
        }
        else
        {
            $r['message'] = '删除成功';
            $r['refresh'] = true;
        }
        return $r;
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

        if ($args['user_id'] != ''){
            $condition.= ' AND user_id=:user_id';
            $params['user_id'] = $args['user_id'];
        }
        if ($args['user_name'] != ''){
            $condition.=' AND user_name like :user_name';
            $params['name'] = "%".$args['user_name']."%";
        }
        if ($args['status'] != ''){
            $condition.= ' AND status=:status';
            $params['status'] = $args['status'];
        }
        if ($args['phone'] != ''){
            $condition.=' AND phone=:phone';
            $params['phone'] = $args['phone'];
        }
        if ($args['card_no'] != ''){
            $condition.=' AND card_no = :card_no';
            $params['card_no'] = $args['card_no'];
        }
        
        $total_num = User::model()->count($condition, $params); //总记录数

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

        $rows = User::model()->findAll($criteria);

        $rs['status'] = 0;
        $rs['desc'] = '成功';
        $rs['page_num'] = ($pages->currentPage + 1);
        $rs['total_num'] = $total_num;
        $rs['total_page'] = ceil($rs['total_num'] / $rs['page_num']);
        $rs['num_of_page'] = $pages->pageSize;
        $rs['rows'] = $rows;

        return $rs;
    }

    public static function FindOneById($id){
        $row = Yii::app()->db->createCommand()
            ->select("user_id,user_name,phone,card_no,email,sex,remark,record_time,status,balance,point,vip_status,vip_expire_date")
            ->from("g_user")
            ->where("user_id='{$id}'")
            ->queryRow();
        return $row;
    }
    public static function FindOneByPhone($phone){
        $row = Yii::app()->db->createCommand()
            ->select("user_id,user_name,phone,card_no,email,sex,remark,record_time,status,balance,point,vip_status,vip_expire_date")
            ->from("g_user")
            ->where("phone='{$phone}'")
            ->queryRow();
        return $row;
    }

    public static function GetBoxAr(){

    	$rows = Yii::app()->db->createCommand()
    	->select("id,name,type,abbreviation")
    	->from("g_operator")
    	->order("abbreviation")
    	->queryAll();
    	$ar=array(""=>"--请选择--");
    	if($rows){
    		foreach($rows as $row){
    			$ar[$row['id']]=$row['name'];
    		}
    	}
    	return $ar;
    }

    public static function Deduct(&$conn,$amount,$id){
        $amount=-$amount;
        self::ChangeBalance($conn,$amount,$id);
    }

    private static function ChangeBalance(&$conn,$amount,$id){

        $msg=array('status'=>0,'desc'=>'成功');
        if(!trim($id)){
            $msg['status']=3;
            $msg['desc']='用户ID不能为空！';
        }
        $row=Yii::app()->db->createCommand()
            ->select("*")
            ->from("g_user")
            ->where("user_id=:user_id",array("user_id"=>$id))
            ->queryRow();
        if(!$row){
            $msg['status']=4;
            $msg['desc']='用户不存在！';
            return $msg;
        }
        $balance=$row['balance'];

        if($row['status']!=User::STATUS_NORMAL){
            $msg['status']=1;
            $msg['desc']='账户状态异常！';
            return $msg;
        }
        if($amount<0&&$balance<abs($amount)){
            $msg['status']=2;
            $msg['desc']='余额不足！';
            return $msg;
        }

        $sql = "update g_user set balance=balance+:amount where user_id=:user_id";
        $command = $conn->createCommand($sql);
        $command->bindParam(":amount",$amount, PDO::PARAM_STR);
        $command->bindParam(":user_id",$id, PDO::PARAM_STR);
        $command->execute();

        return $msg;
    }

    public static function Recharge(&$conn,$amount,$id){
        self::ChangeBalance($conn,$amount,$id);
    }

    public static function CheckVipStatus($phone){
        $msg=array('status'=>0,'desc'=>'成功');
        if(!trim($phone)){
            $msg['status']=4;
            $msg['desc']='用户ID不能为空！';
        }

        $row=Yii::app()->db->createCommand()
            ->select("*")
            ->from("g_user")
            ->where("phone=:phone",array("phone"=>$phone))
            ->queryRow();

        if(!$row){
            $msg['status']=5;
            $msg['desc']='用户不存在！';
            return $msg;
        }

        if($row['status']==1){
            $vip_expire_date=$row['vip_expire_date'];
            if($vip_expire_date<date("Y-m-d")){
                $sql = "update g_user set vip_status=:vip_status where phone=:phone";
                $command = Yii::app()->db->createCommand($sql);
                $command->bindParam(":vip_status",User::VIP_STATUS_EXPIRE, PDO::PARAM_STR);
                $command->bindParam(":phone",$phone, PDO::PARAM_STR);
                $command->execute();
            }
        }

        return $msg;
    }

    public static function AddVipNumber(&$conn,$id,$amount){
        $msg=array('status'=>0,'desc'=>'成功');
        if(!trim($id)){
            $msg['status']=4;
            $msg['desc']='用户ID不能为空！';
        }

        $row=Yii::app()->db->createCommand()
            ->select("*")
            ->from("g_user")
            ->where("user_id=:user_id",array("user_id"=>$id))
            ->queryRow();

        if(!$row){
            $msg['status']=5;
            $msg['desc']='用户不存在！';
            return $msg;
        }

        $card_no=$row['phone'];//电话号码做为会员卡号

        $vip_expire_date_old=$row['vip_expire_date'];
        if($row['status']!=User::STATUS_NORMAL){
            $msg['status']=6;
            $msg['desc']='账户状态异常！';
            return $msg;
        }

        $vip_status=User::VIP_STATUS_NORMAL;
        $SysSetting=SysSetting::GetSettingKV();
        $vip_expire_date_now=0;
        if($amount==$SysSetting[SysSetting::VIP_ONE_YEAR]){
            //一年的VIP
            $y=1;
        }elseif($amount==$SysSetting[SysSetting::VIP_THREE_YEAR]){
            //三年的VIP
            $y=3;
        }else{
            $msg['status']=7;
            $msg['desc']='会员费用和系统设置不一至！';
            return $msg;
        }

        $today=date("Y-m-d");
        if($vip_expire_date_old>$today){//续费VIP会员
            $old_ar=explode("-",$vip_expire_date_old);
            $old_ar[0]=$old_ar[0]+$y;
            $vip_expire_date_now=implode("-",$old_ar);
        }else{//新VIP会员或已经过期的VIP会员
            $today_y=date("Y")+$y;
            $today_m=date("m");
            $today_d=date("d");
            $vip_expire_date_now="$today_y-$today_m-$today_d";
        }

        $sql = "update g_user set card_no=:card_no,vip_status=:vip_status,vip_expire_date=:vip_expire_date where user_id=:user_id";
        $command = $conn->createCommand($sql);
        $command->bindParam(":card_no",$card_no, PDO::PARAM_STR);
        $command->bindParam(":vip_status",$vip_status, PDO::PARAM_STR);
        $command->bindParam(":vip_expire_date",$vip_expire_date_now, PDO::PARAM_STR);
        $command->bindParam(":user_id",$id, PDO::PARAM_STR);
        $command->execute();

        return $msg;
    }


}


    