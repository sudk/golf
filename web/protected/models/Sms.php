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
            array('order,type,type,start_time,end_time,creatorid,link_url,record_time', 'safe', 'on' => 'create'),
            array('order,type,type,start_time,end_time,creatorid,link_url,record_time', 'safe', 'on' => 'modify'),
         );
    }

    private function http_request ($url){

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

    private function tpl($t){
        $tpls=array(
            "1"=>"您的验证码是：param。请不要把验证码泄露给其他人。如非本人操作，可不用理会！",
            "2"=>"您的订单编码：param。如需帮助请联系客服。",
        );
        return $tpls[$t];
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


    