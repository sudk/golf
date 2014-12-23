<?php
/**
 * --请填写模块名称--
 *用户的会员卡信息
 * @author #guohao 
 * @copyright Copyright &copy; 2003-2009 TrunkBow Co., Inc
 */
class Card extends CActiveRecord {

   
    

    public static function model($className=__CLASS__){
        return parent::model($className);
    }

    public function tableName(){
        return 'g_card';
    }

    public function rules(){
        return array(
             //安全性
            array('id,card_name,desc,user_id,card_no', 'safe', 'on' => 'create'),
            array('id,card_name,desc,user_id,card_no', 'safe', 'on' => 'modify'),

            //array('password', 'compare', 'compareAttribute'=>'passwordc', 'on'=>'create,modify'),
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

        if ($args['id'] != ''){
            $condition.= ' AND id=:id';
            $params['id'] = $args['id'];
        }
        if ($args['card_name'] != ''){
            $condition.=' AND card_name like :card_name';
            $params['card_name'] = "%".$args['card_name']."%";
        }
        
        if ($args['card_no'] != ''){
            $condition.=' AND card_no like :card_no';
            $params['card_no'] = "%".$args['card_no']."%";
        }
        
        if ($args['user_id'] != ''){
            $condition.= ' AND user_id=:user_id';
            $params['user_id'] = $args['user_id'];
        }
        
        
        $total_num = Card::model()->count($condition, $params); //总记录数

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

        $rows = Card::model()->findAll($criteria);

        $rs['status'] = 0;
        $rs['desc'] = '成功';
        $rs['page_num'] = ($pages->currentPage + 1);
        $rs['total_num'] = $total_num;
        $rs['total_page'] = ceil($rs['total_num'] / $rs['page_num']);
        $rs['num_of_page'] = $pages->pageSize;
        $rs['rows'] = $rows;

        return $rs;
    }

    public static function Info($id) {

        $condition="";
        $params=array();
        if ($id != ''){
            $condition.=' id = :id';
            $params['id'] =$id;
        }else{
            return false;
        }

        $row=Yii::app()->db->createCommand()
            ->select("*")
            ->from("g_card")
            ->where($condition,$params)
            ->queryRow();
        if($row){
            $row['img']=Img::GetImg($row['id'],Img::TYPE_CARD);
        }
        return $row;
    }

    public static function InfoList() {

        $condition="";
        $params=array();

        $condition.=' user_id = :user_id';
        $params['user_id'] =Yii::app()->user->id;

        $rows=Yii::app()->db->createCommand()
            ->select("*")
            ->from("g_card")
            ->where($condition,$params)
            ->queryAll();
        if($rows){
            $rs_ar=array();
            foreach($rows as $row){
                $row['img']=Img::GetImg($row['id'],Img::TYPE_CARD);
                $rs_ar[]=$row;
            }
            return $rs_ar;
        }else{
            return false;
        }
    }
}


    