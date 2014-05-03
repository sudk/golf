<?php

/**
 * 赛事
 * @author sudk
 */
class FleaController extends CMDBaseController
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

        $rs=Flea::InfoList(Yii::app()->command->cmdObj->_pg_,$this->pageSize,Yii::app()->command->cmdObj);
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

    public function actionInfo(){
        if(!Yii::app()->command->cmdObj->id){
            $msg['status']=1;
            $msg['desc']="ID不能为空！";
            echo json_encode($msg);
            return;
        }
        $row=Flea::Info(Yii::app()->command->cmdObj->id);
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
        if(!isset(Yii::app()->command->cmdObj->title)||Yii::app()->command->cmdObj->title==''){
            $msg['status']=1;
            $msg['desc']="标题不能为空！";
            echo json_encode($msg);
            return;
        }

        if(!isset(Yii::app()->command->cmdObj->city)||Yii::app()->command->cmdObj->city==''){
            $msg['status']=1;
            $msg['desc']="城市不能为空！";
            echo json_encode($msg);
            return;
        }

        if(!isset(Yii::app()->command->cmdObj->desc)||Yii::app()->command->cmdObj->desc==''){
            $msg['status']=1;
            $msg['desc']="商户描述不能为空！";
            echo json_encode($msg);
            return;
        }

        if(!isset(Yii::app()->command->cmdObj->price)||Yii::app()->command->cmdObj->price==''){
            $msg['status']=1;
            $msg['desc']="价格不能为空！";
            echo json_encode($msg);
            return;
        }
        if(!isset(Yii::app()->command->cmdObj->contact)||Yii::app()->command->cmdObj->contact==''){
            $msg['status']=1;
            $msg['desc']="联系人不能为空！";
            echo json_encode($msg);
            return;
        }
        if(!isset(Yii::app()->command->cmdObj->phone)||Yii::app()->command->cmdObj->phone==''){
            $msg['status']=1;
            $msg['desc']="电话不能为空！";
            echo json_encode($msg);
            return;
        }

        $user_id=Yii::app()->user->id;
        $conn=Yii::app()->db;
        $transaction = $conn->beginTransaction();
        $id=date("YmdHis").rand(100000,999999);
        try{
            $record_time=date("Y-m-d H:i:s");
            $sql="insert into g_flea (id,title,`city`,`desc`,price,record_time,user_id) values (:id,:title,:city,:desc,:price,:record_time,:user_id)";
            $command = $conn->createCommand($sql);
            $command->bindParam(":id",$id, PDO::PARAM_STR);
            $command->bindParam(":title", Yii::app()->command->cmdObj->title, PDO::PARAM_STR);
            $command->bindParam(":city", Yii::app()->command->cmdObj->city, PDO::PARAM_STR);
            $command->bindParam(":desc", Yii::app()->command->cmdObj->desc, PDO::PARAM_STR);
            $command->bindParam(":price", Yii::app()->command->cmdObj->price, PDO::PARAM_STR);
            $command->bindParam(":record_time",$record_time, PDO::PARAM_STR);
            $command->bindParam(":user_id", $user_id, PDO::PARAM_STR);
            $command->execute();
            if(!isset(Yii::app()->command->cmdObj->imgs)&&count(Yii::app()->command->cmdObj->imgs)){
                $type=Img::TYPE_FLEA;
                foreach(Yii::app()->command->cmdObj->imgs as $img){
                    $img=str_replace(Img::IMG_PATH,'',$img);
                    $sql="insert into g_img (relation_id,`type`,`img_url`,record_time) values (:relation_id,:type,:img_url,:record_time)";
                    $command = $conn->createCommand($sql);
                    $command->bindParam(":relation_id",$id, PDO::PARAM_STR);
                    $command->bindParam(":type",$type, PDO::PARAM_STR);
                    $command->bindParam(":img_url",$img, PDO::PARAM_STR);
                    $command->bindParam(":record_time",$record_time, PDO::PARAM_STR);
                    $command->execute();
                }
            }
            $transaction->commit();
            $msg['status']=0;
            $msg['desc']="成功，等管理员审核后才能显示。";
        }catch (Exception $e){
            $transaction->rollBack();
            $msg['status']=7;
            $msg['desc']="保存失败";
        }
        echo json_encode($msg);
        return;
    }

    public function actionUpdate(){
        if(!isset(Yii::app()->command->cmdObj->id)||Yii::app()->command->cmdObj->id==''){
            $msg['status']=1;
            $msg['desc']="ID不能为空！";
            echo json_encode($msg);
            return;
        }
        if(!isset(Yii::app()->command->cmdObj->title)||Yii::app()->command->cmdObj->title==''){
            $msg['status']=2;
            $msg['desc']="标题不能为空！";
            echo json_encode($msg);
            return;
        }

        if(!isset(Yii::app()->command->cmdObj->city)||Yii::app()->command->cmdObj->city==''){
            $msg['status']=3;
            $msg['desc']="城市不能为空！";
            echo json_encode($msg);
            return;
        }

        if(!isset(Yii::app()->command->cmdObj->desc)||Yii::app()->command->cmdObj->desc==''){
            $msg['status']=4;
            $msg['desc']="商户描述不能为空！";
            echo json_encode($msg);
            return;
        }

        if(!isset(Yii::app()->command->cmdObj->price)||Yii::app()->command->cmdObj->price==''){
            $msg['status']=5;
            $msg['desc']="价格不能为空！";
            echo json_encode($msg);
            return;
        }
        if(!isset(Yii::app()->command->cmdObj->contact)||Yii::app()->command->cmdObj->contact==''){
            $msg['status']=6;
            $msg['desc']="联系人不能为空！";
            echo json_encode($msg);
            return;
        }
        if(!isset(Yii::app()->command->cmdObj->phone)||Yii::app()->command->cmdObj->phone==''){
            $msg['status']=7;
            $msg['desc']="电话不能为空！";
            echo json_encode($msg);
            return;
        }
        $conn=Yii::app()->db;
        $transaction = $conn->beginTransaction();
        $record_time=date("Y-m-d H:i:s");
        try{
            $sql="update g_flea set title=:title,city=:city,`desc`=:desc,price=:price,status=:status,record_time=:record_time,check_id='',check_time='' where id=:id";
            $command = $conn->createCommand($sql);
            $command->bindParam(":title", Yii::app()->command->cmdObj->title, PDO::PARAM_STR);
            $command->bindParam(":city", Yii::app()->command->cmdObj->city, PDO::PARAM_STR);
            $command->bindParam(":desc", Yii::app()->command->cmdObj->desc, PDO::PARAM_STR);
            $command->bindParam(":price", Yii::app()->command->cmdObj->price, PDO::PARAM_STR);
            $command->bindParam(":status",Flea::STATUS_UNAUDITED, PDO::PARAM_STR);
            $command->bindParam(":record_time",$record_time, PDO::PARAM_STR);
            $command->bindParam(":id", Yii::app()->command->cmdObj->id, PDO::PARAM_STR);
            $command->execute();

            $sql="delete from g_img where relation_id=:relation_id";
            $command = $conn->createCommand($sql);
            $command->bindParam(":relation_id",Yii::app()->command->cmdObj->id, PDO::PARAM_STR);
            $command->execute();

            if(!isset(Yii::app()->command->cmdObj->imgs)&&count(Yii::app()->command->cmdObj->imgs)){
                $type=Img::TYPE_FLEA;
                foreach(Yii::app()->command->cmdObj->imgs as $img){
                    $img=str_replace(Img::IMG_PATH,'',$img);
                    $sql="insert into g_img (relation_id,`type`,`img_url`,record_time) values (:relation_id,:type,:img_url,:record_time)";
                    $command = $conn->createCommand($sql);
                    $command->bindParam(":relation_id",Yii::app()->command->cmdObj->id, PDO::PARAM_STR);
                    $command->bindParam(":type",$type, PDO::PARAM_STR);
                    $command->bindParam(":img_url",$img, PDO::PARAM_STR);
                    $command->bindParam(":record_time",$record_time, PDO::PARAM_STR);
                    $command->execute();
                }
            }
            $transaction->commit();
            $msg['status']=0;
            $msg['desc']="成功，等管理员审核后才能显示。";
        }catch (Exception $e){
            $transaction->rollBack();
            $msg['status']=7;
            $msg['desc']="保存失败";
        }
        echo json_encode($msg);
        return;
    }
    public function actionDel(){
        if(!isset(Yii::app()->command->cmdObj->id)||Yii::app()->command->cmdObj->id==''){
            $msg['status']=1;
            $msg['desc']="ID不能为空！";
            echo json_encode($msg);
            return;
        }

        $conn=Yii::app()->db;
        $transaction = $conn->beginTransaction();
        try{
            $sql="delete from g_flea where id=:id";
            $command = $conn->createCommand($sql);
            $command->bindParam(":id",Yii::app()->command->cmdObj->id, PDO::PARAM_STR);
            $command->execute();

            $sql="delete from g_img where relation_id=:relation_id";
            $command = $conn->createCommand($sql);
            $command->bindParam(":relation_id",Yii::app()->command->cmdObj->id, PDO::PARAM_STR);
            $command->execute();

            $transaction->commit();
            $msg['status']=0;
            $msg['desc']="成功，等管理员审核后才能显示。";
        }catch (Exception $e){
            $transaction->rollBack();
            $msg['status']=7;
            $msg['desc']="保存失败";
        }
        echo json_encode($msg);
        return;
    }
}