<?php

/**
 * 我的卡包
 * @author sudk
 */
class CardController extends CMDBaseController
{
    public function accessRules() {
        return array('login' => array('list','info'));
    }

    public function actionInfo(){
        if(!Yii::app()->command->cmdObj->id){
            $msg['status']=1;
            $msg['desc']="卡号不能为空！";
            echo json_encode($msg);
            return;
        }
        $row=Card::Info(Yii::app()->command->cmdObj->id);
        if($row){
            $msg['status']=0;
            $msg['desc']="成功";
            $msg['data']=$row;
        }else{
            $msg['status']=4;
            $msg['desc']="没有数据";
        }
        echo json_encode($msg);
        return;
    }

    public function actionList(){
        $rows=Card::InfoList();
        if($rows){
            $msg['status']=0;
            $msg['desc']="成功";
            $msg['data']=$rows;
        }else{
            $msg['status']=4;
            $msg['desc']="没有数据";
        }
        echo json_encode($msg);
        return;
    }

}