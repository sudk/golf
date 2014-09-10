<?php

/**
 * 消费明细
 * @author sudk
 */
class TransController extends CMDBaseController
{


    public function accessRules() {
        return array('login' => array('list'));
    }

    public function actionList(){
        if(Yii::app()->command->cmdObj->_pg_==null||Yii::app()->command->cmdObj->_pg_==""){
            $msg['status']=1;
            $msg['desc']="分页参数不能为空！";
            echo json_encode($msg);
            return;
        }
        if(Yii::app()->command->cmdObj->month==null||Yii::app()->command->cmdObj->month==""){
            $msg['status']=2;
            $msg['desc']="交易月份不能为空！";
            echo json_encode($msg);
            return;
        }

        $rows=TransRecord::PersonalList(Yii::app()->command->cmdObj->_pg_,$this->pageSize,Yii::app()->command->cmdObj);
        if(count($rows)){
            $msg['status']=0;
            $msg['desc']="成功";
            $msg['_pg_']=Yii::app()->command->cmdObj->_pg_;
            $msg['data']=$rows;
        }else{
            $msg['status']=4;
            $msg['desc']="没有数据";
        }
        echo json_encode($msg);
        return;
    }

}