<?php
/**
 * --请填写模块名称--
 *寄卖商品模型
 * @author #guohao <sudk@trunkbow.com>#
 * @copyright Copyright &copy; 2003-2009 TrunkBow Co., Inc
 */
class Sms extends CActiveRecord {

    const USER_NAME='sudk';
    const PASSWORD='sudunkuai';
    const URL='http://sms.106jiekou.com/utf8/sms.aspx?account=sudk&password=sudunkuai&mobile=num&content=';

    public static function model($className=__CLASS__){
        return parent::model($className);
    }

    public function tableName(){
        return 'g_sms';
    }

    public function rules(){
        return array(
            array('id,phone,content,status,record_time', 'safe', 'on' => 'create'),
            array('id,phone,content,status,record_time', 'safe', 'on' => 'modify'),
         );
    }

    private function http_request ($url){
        if (function_exists("curl_init")) {
            $curl = curl_init();
//            curl_setopt($curl, CURLOPT_POST, 1);
//            curl_setopt($curl, CURLOPT_POSTFIELDS, $content);
            curl_setopt($curl, CURLOPT_URL, $url);
            curl_setopt($curl, CURLOPT_RETURNTRANSFER, 1);
            curl_setopt($curl, CURLOPT_HEADER, false);
            curl_setopt($curl, CURLOPT_TIMEOUT, 60); //seconds

//             https verify
//            curl_setopt($curl, CURLOPT_SSL_VERIFYPEER, upmp_config::VERIFY_HTTPS_CERT);
//            curl_setopt($curl, CURLOPT_SSL_VERIFYHOST, upmp_config::VERIFY_HTTPS_CERT);

            $ret_data = curl_exec($curl);

            if (curl_errno($curl)) {
                //printf("curl call error(%s): %s\n", curl_errno($curl), curl_error($curl));
                curl_close($curl);
                return false;
            }
            else {
                curl_close($curl);
                return $ret_data;
            }
        } else {
            throw new Exception("[PHP] curl module is required");
        }
    }

    private function getStatus($code){
        $ar=array(
            "100"=>"发送成功",
            "101"=>"验证失败",
            "102"=>"手机号码格式不正确",
            "103"=>"会员级别不够",
            "104"=>"内容未审核",
            "105"=>"内容过多",
            "106"=>"账户余额不足",
            "107"=>"Ip受限",
            "108"=>"手机号码发送太频繁",
            "120"=>"系统升级"
        );
        return $ar[$code];
    }

    public function send($content,$phone,$tpl){
        if(!$content){
            return array('status'=>1,'desc'=>"内容不能为空");
        }
        if(!$phone){
            return array('status'=>2,'desc'=>"电话号码不能为空");
        }
        if(!$tpl){
            return array('status'=>3,'desc'=>"请指定模板");
        }
        $tpl=$this->tpl($tpl);
        if($tpl){
            $content=str_replace("param",$content,$tpl);
            $url=self::URL;
            $url=str_replace("num",$phone,$url);
            $url.=$content;
            $rs=$this->http_request($url);
            if($rs){
                $this->add($content,$phone,$rs);
                if($rs=="100"){
                    $rs=0;
                }
                return array('status'=>$rs,'desc'=>$this->getStatus($rs));
            }else{
                return array('status'=>5,'desc'=>'请求失败');
            }
        }else{
            return array('status'=>4,'desc'=>"模板不存在");
        }

    }

    private function tpl($t){
        $tpls=array(
            "1"=>"您的验证码是：param。请不要把验证码泄露给其他人。如非本人操作，可不用理会！",
            "2"=>"您的订单编码：param。如需帮助请联系客服。",
        );
        return $tpls[$t];
    }

    public function add($content,$phone,$status){
        $conn=Yii::app()->db;
        $sql = "
                    insert into g_sms
                    (phone,content,status,record_time)
                     values
                    (:phone,:content,:status,:record_time)
            ";
        $data_time=date("Y-m-d H:i:s");
        $command = $conn->createCommand($sql);
        $command->bindParam(":phone",$content,PDO::PARAM_STR);
        $command->bindParam(":content",$phone,PDO::PARAM_STR);
        $command->bindParam(":status",$status,PDO::PARAM_STR);
        $command->bindParam(":record_time",$data_time, PDO::PARAM_STR);
        $command->execute();
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

        
        if ($args['relation_id'] != ''){
            $condition.=' AND `relation_id` = :relation_id';
            $params['relation_id'] = intval($args['relation_id']);
        }
        
        
        if ($args['type'] != ''){
            $condition.=' AND `type`=:type';
            $params['type'] = $args['type'];
        }
        
        if ($args['status'] != ''){
            $condition.=' AND status=:status';
            $params['status'] = $args['status'];
        }
        
        
        $total_num = Sms::model()->count($condition, $params); //总记录数

        $criteria = new CDbCriteria();
        
    	
        $criteria->order = 'record_time  DESC';
        

        $criteria->condition = $condition;
        $criteria->params = $params;

        $pages = new CPagination($total_num);
        $pages->pageSize = $pageSize;
        $pages->setCurrentPage($page);
        $pages->applyLimit($criteria);

        $rows = Sms::model()->findAll($criteria);

        $rs['status'] = 0;
        $rs['desc'] = '成功';
        $rs['page_num'] = ($pages->currentPage + 1);
        $rs['total_num'] = $total_num;
        $rs['total_page'] = ceil($rs['total_num'] / $rs['page_num']);
        $rs['num_of_page'] = $pages->pageSize;
        $rs['rows'] = $rows;

        return $rs;
    }

}


    