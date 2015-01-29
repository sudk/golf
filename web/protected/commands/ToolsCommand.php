<?php

/**
 * Description of Statsystem
 *
 * @author sudk
 */
class ToolsCommand extends CConsoleCommand
{
    /*获得刷卡记录

    */
    public function actionInitstaff()
    {
        Staff::InitStaff();
    }
    /**
     * 生成回访计划
     * @param null $vdate
     */
    public function actionRemindvisit($vdate=null){
        echo MchtVdetail::createDetail($vdate);
    }

    public function actionMchtVisitC(){
        echo "begin compute...";
        MchtVisit::VisitCompute();
        echo "end compute";
    }

    public function actionCheckPayTime(){
        date_default_timezone_set('Asia/Shanghai');
        $now=date("Y-m-d H:i:s",strtotime("-240 seconds"));
        $rows=Yii::app()->db->createCommand()
            ->select("order_id")
            ->from("g_order")
            ->where("last_pay_time <= '{$now}' and status=1")
            ->queryAll();
        if(count($rows)){
            foreach($rows as $row){
                Order::Cancel($row['order_id']);
            }
        }
    }
}
