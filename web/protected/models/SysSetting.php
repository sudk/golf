<?php
/**
 * --请填写模块名称--
 *寄卖商品模型
 * @author #guohao <sudk@trunkbow.com>#
 * @copyright Copyright &copy; 2003-2009 TrunkBow Co., Inc
 */
class SysSetting extends CActiveRecord {

    const VIP_START_NUM = 'vip_start_num';//vip起始卡号

    public static function model($className=__CLASS__){
        return parent::model($className);
    }

    public function tableName(){
        return 'g_sys_setting';
    }

    public function rules(){
        return array(
            array('id,value,desc', 'safe', 'on' => 'create'),
            array('id,value,desc', 'safe', 'on' => 'modify'),
         );
    }

    public static function GetNewVipNumber(){

        $condition="id='".SysSetting::VIP_START_NUM."' FOR UPDATE";
        $value=Yii::app()->db->createCommand()
            ->select("value")
            ->from("g_sys_setting")
            ->where($condition)
            ->queryScalar();
        $value++;
        $sql = 'update g_sys_setting set `value` = :value';
        $command = Yii::app()->db->createCommand($sql);
        $command->bindParam(":value", $value, PDO::PARAM_STR);
        $rs = $command->execute();

        if($rs){
            return $value;
        }else{
            return false;
        }

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

        
        if ($args['order'] != ''){
            $condition.=' AND `order` = :order';
            $params['order'] = intval($args['order']);
        }
        
        
        if ($args['type'] != ''){
            $condition.=' AND `type`=:type';
            $params['type'] = $args['type'];
        }
        
        if ($args['status'] != ''){
            $condition.=' AND status=:status';
            $params['status'] = $args['status'];
        }
        
        
        $total_num = Adv::model()->count($condition, $params); //总记录数

        $criteria = new CDbCriteria();
        
    	
        $criteria->order = 'record_time  DESC';
        

        $criteria->condition = $condition;
        $criteria->params = $params;

        $pages = new CPagination($total_num);
        $pages->pageSize = $pageSize;
        $pages->setCurrentPage($page);
        $pages->applyLimit($criteria);

        $rows = Adv::model()->findAll($criteria);

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


    