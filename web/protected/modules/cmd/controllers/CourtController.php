<?php

/**
 * 用户接口
 * @author sudk
 */
class CourtController extends CMDBaseController
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
        if(!Yii::app()->command->cmdObj->city){
            $msg['status']=1;
            $msg['desc']="城市不能为空！";
            echo json_encode($msg);
            return;
        }
        if(!Yii::app()->command->cmdObj->date){
            $msg['status']=2;
            $msg['desc']="日期不能为空！";
            echo json_encode($msg);
            return;
        }
        if(!Yii::app()->command->cmdObj->time){
            $msg['status']=3;
            $msg['desc']="时段不能为空！";
            echo json_encode($msg);
            return;
        }
        $rows=Court::Search(Yii::app()->command->cmdObj);
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

    public function actionInfo(){
        if(!Yii::app()->command->cmdObj->court_id){
            $msg['status']=1;
            $msg['desc']="球场ID不能为空！";
            echo json_encode($msg);
            return;
        }
        $row=Court::Info(Yii::app()->command->cmdObj->court_id);
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

    public function actionPrice(){
        if(!Yii::app()->command->cmdObj->court_id){
            $msg['status']=1;
            $msg['desc']="球场ID不能为空！";
            echo json_encode($msg);
            return;
        }
        if(!Yii::app()->command->cmdObj->date_time){
            $msg['status']=1;
            $msg['desc']="打球日期时间不能为空！";
            echo json_encode($msg);
            return;
        }
        $rows=Court::Price(Yii::app()->command->cmdObj);
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

    public function actionFacilities(){
        if(Yii::app()->command->cmdObj->_pg_==null||Yii::app()->command->cmdObj->_pg_==""){
            $msg['status']=1;
            $msg['desc']="分页参数不能为空！";
            echo json_encode($msg);
            return;
        }

        $rs=CourtFacilities::InfoList(Yii::app()->command->cmdObj->_pg_,3,Yii::app()->command->cmdObj);
        if($rs['rows']){
            $msg['status']=0;
            $msg['desc']="成功";
            $msg['_pg_']=Yii::app()->command->cmdObj->_pg_;
            $msg['data']=$rs['rows'];
        }else{
            $msg['status']=4;
            $msg['desc']="没有数据";
        }
        echo json_encode($msg);
        return;
    }

    public function actionComment(){
        if(Yii::app()->command->cmdObj->_pg_==null||Yii::app()->command->cmdObj->_pg_==""){
            $msg['status']=1;
            $msg['desc']="分页参数不能为空！";
            echo json_encode($msg);
            return;
        }
        if(Yii::app()->command->cmdObj->court_id==null||Yii::app()->command->cmdObj->court_id==""){
            $msg['status']=2;
            $msg['desc']="球场ID不能为空！";
            echo json_encode($msg);
            return;
        }

        $msg['comment_count']=0;
        $msg['service_total']=0;
        $msg['design_total']=0;
        $msg['facilitie_total']=0;
        $msg['lawn_total']=0;

        $rs=Comment::InfoList(Yii::app()->command->cmdObj->_pg_,$this->pageSize,Yii::app()->command->cmdObj);
        if($rs['rows']){
            $msg['status']=0;
            $msg['desc']="成功";
            $com_avg=Comment::ComAvg(Yii::app()->command->cmdObj->court_id);
            if($com_avg){
                $msg['comment_count']=$com_avg['comment_count'];
                $msg['service_total']=$com_avg['service_total'];
                $msg['design_total']=$com_avg['design_total'];
                $msg['facilitie_total']=$com_avg['facilitie_total'];
                $msg['lawn_total']=$com_avg['lawn_total'];
            }
            $msg['_pg_']=Yii::app()->command->cmdObj->_pg_;
            $msg['data']=$rs['rows'];
        }else{
            $msg['status']=4;
            $msg['desc']="没有数据";
        }
        echo json_encode($msg);
        return;
    }


}