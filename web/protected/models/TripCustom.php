<?php
/**
 * --请填写模块名称--
 *行程模型
 * @author #sudk <sudk@trunkbow.com>#
 * @copyright Copyright &copy; 2003-2009 TrunkBow Co., Inc
 */
class TripCustom extends CActiveRecord {

    public static function model($className=__CLASS__){
        return parent::model($className);
    }

    public function tableName(){
        return 'g_trip_custom';
    }

    public function rules(){
        return array();
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
            $condition.=' AND t.court_id=:court_id';
            $params['court_id'] = $args['court_id'];
        }

        if ($args['city'] != ''){
            $condition.=' AND t.city=:city';
            $params['city'] = $args['city'];
        }
        
        if ($args['agent_id'] != ''){
            $condition.=' AND t.agent_id=:agent_id';
            $params['agent_id'] = $args['agent_id'];
        }
        
        if ($args['trip_name'] != ''){
            $condition.=' AND t.trip_name like :trip_name';
            $params['trip_name'] = "%".$args['trip_name']."%";
        }

        
        $total_num = Yii::app()->db->createCommand()
            ->select("count(1)")
            ->from("g_trip_custom t")
            ->where($condition,$params)
            ->queryScalar();
        $order = 't.record_time DESC';
        $rows=Yii::app()->db->createCommand()
            ->select("t.*")
            ->from("g_trip_custom t")
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


    
}


    