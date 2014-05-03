<?php

/**
 * 赛事
 * @author sudk
 */
class CompetitionController extends CMDBaseController
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

    public function actionList(){
        if(Yii::app()->command->cmdObj->name){
            $args['name']=Yii::app()->command->cmdObj->name;
        }

        if(Yii::app()->command->cmdObj->_pg_==null||Yii::app()->command->cmdObj->_pg_==""){
            $msg['status']=1;
            $msg['desc']="分页参数不能为空！";
            echo json_encode($msg);
            return;
        }

        $rows=Competition::queryList(Yii::app()->command->cmdObj->_pg_,$this->pageSize,$args);
        if($rows){
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

    public function actionInfo(){
        if(!Yii::app()->command->cmdObj->id){
            $msg['status']=1;
            $msg['desc']="赛事ID不能为空！";
            echo json_encode($msg);
            return;
        }
        $row=Competition::Info(Yii::app()->command->cmdObj->id);
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


}