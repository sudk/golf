<?php

/**
 * 赛事
 * @author sudk
 */
class CardController extends CMDBaseController
{
    public function accessRules() {
        return array(
            array('allow',
                'actions' => array('info','Bandcard'),
                'users' => array('@'),
            ),
            array('allow',
                'users' => array('*'),
            ),
            array('deny',
                'actions' => array(),
            ),
        );
    }

    public function actionInfo(){
        if(!Yii::app()->command->cmdObj->card_no){
            $msg['status']=1;
            $msg['desc']="卡号不能为空！";
            echo json_encode($msg);
            return;
        }

        $rows=Card::Info(substr(Yii::app()->command->cmdObj->card_no,0,4));
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