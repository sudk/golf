<?php

/**
 * 用户接口
 * @author sudk
 */
class SaleController extends CMDBaseController
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

    public function actionSearch(){
        if(!isset(Yii::app()->command->cmdObj->city)||Yii::app()->command->cmdObj->city==""){
            $msg['status']=1;
            $msg['desc']="城市不能为空！";
            echo json_encode($msg);
            return;
        }
        $rows=Court::Sale(Yii::app()->command->cmdObj);
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