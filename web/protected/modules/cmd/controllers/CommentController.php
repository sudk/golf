<?php

/**
 * 赛事
 * @author sudk
 */
class CommentController extends CMDBaseController
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

        if(Yii::app()->command->cmdObj->_pg_==null||Yii::app()->command->cmdObj->_pg_==""){
            $msg['status']=1;
            $msg['desc']="分页参数不能为空！";
            echo json_encode($msg);
            return;
        }
        if(!isset(Yii::app()->command->cmdObj->court_id)){
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

    public function actionInfo(){
        if(!isset(Yii::app()->command->cmdObj->id)){
            $msg['status']=1;
            $msg['desc']="ID不能为空！";
            echo json_encode($msg);
            return;
        }
        $row=Comment::Info(Yii::app()->command->cmdObj->id);
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

    public function actionCreate(){
        if(!isset(Yii::app()->command->cmdObj->court_id)||Yii::app()->command->cmdObj->court_id==''){
            $msg['status']=1;
            $msg['desc']="球场ID不能为空！";
            echo json_encode($msg);
            return;
        }

        if(!isset(Yii::app()->command->cmdObj->desc)){
            $msg['status']=2;
            $msg['desc']="球场评论不能为空！";
            echo json_encode($msg);
            return;
        }
        if(!isset(Yii::app()->command->cmdObj->service)){
            $msg['status']=3;
            $msg['desc']="服务得分不能为空！";
            echo json_encode($msg);
            return;
        }
        if(!isset(Yii::app()->command->cmdObj->design)){
            $msg['status']=4;
            $msg['desc']="设计得分不能为空！";
            echo json_encode($msg);
            return;
        }
        if(!isset(Yii::app()->command->cmdObj->facilitie)){
            $msg['status']=5;
            $msg['desc']="设施得分不能为空！";
            echo json_encode($msg);
            return;
        }
        if(!isset(Yii::app()->command->cmdObj->lawn)){
            $msg['status']=6;
            $msg['desc']="草坪得分不能为空！";
            echo json_encode($msg);
            return;
        }

        if(Yii::app()->user->isGuest){
            $user_id="guest";
        }else{
            $user_id=Yii::app()->user->id;
        }

        try{
            $record_time=date("Y-m-d H:i:s");
            $sql="insert into g_comment (court_id,`desc`,service,design,facilitie,lawn,user_id,record_time) values (:court_id,:desc,:service,:design,:facilitie,:lawn,:user_id,:record_time)";
            $command = Yii::app()->db->createCommand($sql);
            $command->bindParam(":court_id", Yii::app()->command->cmdObj->court_id, PDO::PARAM_STR);
            $command->bindParam(":desc", Yii::app()->command->cmdObj->desc, PDO::PARAM_STR);
            $command->bindParam(":service", Yii::app()->command->cmdObj->service, PDO::PARAM_STR);
            $command->bindParam(":design", Yii::app()->command->cmdObj->design, PDO::PARAM_STR);
            $command->bindParam(":facilitie", Yii::app()->command->cmdObj->facilitie, PDO::PARAM_STR);
            $command->bindParam(":lawn", Yii::app()->command->cmdObj->lawn, PDO::PARAM_STR);
            $command->bindParam(":user_id", $user_id, PDO::PARAM_STR);
            $command->bindParam(":record_time",$record_time, PDO::PARAM_STR);
            $command->execute();
            $msg['status']=0;
            $msg['desc']="成功";
        }catch (Exception $e){
            $msg['status']=7;
            $msg['desc']="保存失败";
        }
        echo json_encode($msg);
        return;
    }
}