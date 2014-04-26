<?php
/**
 * --请填写模块名称--
 *寄卖商品模型
 * @author #guohao <sudk@trunkbow.com>#
 * @copyright Copyright &copy; 2003-2009 TrunkBow Co., Inc
 */
class News extends CActiveRecord {

    

    public static function model($className=__CLASS__){
        return parent::model($className);
    }

    public function tableName(){
        return 'g_news';
    }

    public function rules(){
        return array(
            array('id,title,content,creatorid,creator,record_time,status', 'safe', 'on' => 'create'),
            array('id,title,content,creatorid,creator,record_time,status', 'safe', 'on' => 'modify'),
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

        if ($args['title'] != ''){
            $condition.=' AND title like :title';
            $params['title'] = "%".$args['title']."%";
        }


        $total_num = Yii::app()->db->createCommand()
            ->select("count(1)")
            ->from("g_news")
            ->where($condition,$params)
            ->queryScalar();
        
    	
        $order = 'record_time  DESC';

        $rows=Yii::app()->db->createCommand()
            ->select("*")
            ->from("g_news")
            ->where($condition,$params)
            ->order($order)
            ->limit($pageSize)
            ->offset($page * $pageSize)
            ->queryAll();

        $rs['status'] = 0;
        $rs['desc'] = '成功';
        $rs['page_num'] = ($page + 1);
        $rs['total_num'] = $total_num;
        $rs['total_page'] = ceil($rs['total_num'] / $pageSize);
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
            ->from("g_news")
            ->where($condition,$params)
            ->queryRow();
        if($row){
            $row['imgs']=Img::GetImgs($id,Img::TYPE_NEWS);
        }
        return $row;

    }
}


    