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

    public function beforeAction($action)
    {
        if(Yii::app()->user->isGuest){
            $msg['status']=-1;
            $msg['desc']="用户未登陆！";
            echo json_encode($msg);
            return false;
        }else{
            return true;
        }
    }

    public function actionList(){
        if(Yii::app()->command->cmdObj->_pg_==null||Yii::app()->command->cmdObj->_pg_==""){
            $msg['status']=1;
            $msg['desc']="分页参数不能为空！";
            echo json_encode($msg);
            return;
        }

        $rows=Flea::InfoList(Yii::app()->command->cmdObj->_pg_,$this->pageSize,Yii::app()->command->cmdObj);
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

    public function actionMylist(){
        if(Yii::app()->command->cmdObj->_pg_==null||Yii::app()->command->cmdObj->_pg_==""){
            $msg['status']=1;
            $msg['desc']="分页参数不能为空！";
            echo json_encode($msg);
            return;
        }

        $rows=Flea::MyInfoList(Yii::app()->command->cmdObj->_pg_,$this->pageSize,Yii::app()->command->cmdObj);
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

        $user_id=Yii::app()->user->id;
        $conn=Yii::app()->db;
        $transaction = $conn->beginTransaction();
//        $id=date("YmdHis").rand(100000,999999);
        $id=Yii::app()->command->cmdObj->id;
        try{
            $record_time=date("Y-m-d H:i:s");
            $sql="insert into g_flea (id,title,`city`,`desc`,price,record_time,user_id,phone,contact) values (:id,:title,:city,:desc,:price,:record_time,:user_id,:phone,:contact)";
            $command = $conn->createCommand($sql);
            $command->bindParam(":id",$id, PDO::PARAM_STR);
            $command->bindParam(":title", Yii::app()->command->cmdObj->title, PDO::PARAM_STR);
            $command->bindParam(":city", Yii::app()->command->cmdObj->city, PDO::PARAM_STR);
            $command->bindParam(":desc", Yii::app()->command->cmdObj->desc, PDO::PARAM_STR);
            $command->bindParam(":price", Yii::app()->command->cmdObj->price, PDO::PARAM_STR);
            $command->bindParam(":phone", Yii::app()->command->cmdObj->phone, PDO::PARAM_STR);
            $command->bindParam(":contact", Yii::app()->command->cmdObj->contact, PDO::PARAM_STR);
            $command->bindParam(":record_time",$record_time, PDO::PARAM_STR);
            $command->bindParam(":user_id", $user_id, PDO::PARAM_STR);
            $command->execute();
            if(isset(Yii::app()->command->cmdObj->imgs)&&count(Yii::app()->command->cmdObj->imgs)){
                $type=Img::TYPE_FLEA;
                foreach(Yii::app()->command->cmdObj->imgs as $img){
                    $img=Img::GetBasePath($img);
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
            //print_r($e);
            $transaction->rollBack();
            $msg['status']=8;
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
        $status=Flea::STATUS_UNAUDITED;
        try{
            $sql="update g_flea set title=:title,city=:city,`desc`=:desc,price=:price,status=:status,record_time=:record_time,phone=:phone,contact=:contact,check_id='',check_time='' where id=:id";
            $command = $conn->createCommand($sql);
            $command->bindParam(":title", Yii::app()->command->cmdObj->title, PDO::PARAM_STR);
            $command->bindParam(":city", Yii::app()->command->cmdObj->city, PDO::PARAM_STR);
            $command->bindParam(":desc", Yii::app()->command->cmdObj->desc, PDO::PARAM_STR);
            $command->bindParam(":price", Yii::app()->command->cmdObj->price, PDO::PARAM_STR);
            $command->bindParam(":phone", Yii::app()->command->cmdObj->phone, PDO::PARAM_STR);
            $command->bindParam(":contact", Yii::app()->command->cmdObj->contact, PDO::PARAM_STR);
            $command->bindParam(":status",$status, PDO::PARAM_STR);
            $command->bindParam(":record_time",$record_time, PDO::PARAM_STR);
            $command->bindParam(":id", Yii::app()->command->cmdObj->id, PDO::PARAM_STR);
            $command->execute();

            $type=Img::TYPE_FLEA;
            $sql="delete from g_img where relation_id=:relation_id and type=:type";
            $command = $conn->createCommand($sql);
            $command->bindParam(":relation_id",Yii::app()->command->cmdObj->id, PDO::PARAM_STR);
            $command->bindParam(":type",$type, PDO::PARAM_STR);
            $command->execute();

            if(isset(Yii::app()->command->cmdObj->imgs)&&count(Yii::app()->command->cmdObj->imgs)){

                foreach(Yii::app()->command->cmdObj->imgs as $img){
                    $img=Img::GetBasePath($img);
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
            $msg['status']=8;
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

            $type=Img::TYPE_FLEA;
            $sql="delete from g_img where relation_id=:relation_id and type=:type";
            $command = $conn->createCommand($sql);
            $command->bindParam(":relation_id",Yii::app()->command->cmdObj->id, PDO::PARAM_STR);
            $command->bindParam(":type",$type, PDO::PARAM_STR);
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
    public function actionUpload(){

        try{
            if(!$_POST['id']){
                $msg['status']=1;
                $msg['desc']="ID不能为空！";
                throw new RuntimeException($msg['desc']);
            }
            $file = $_FILES['my_file'];
            if ($file["error"]>0) {
                $msg['status']=3;
                $msg['msg']='上传失败，请重试上传一张更小一点的图片';
                throw new RuntimeException($msg['desc']);
            }

            $f_info = new finfo(FILEINFO_MIME_TYPE);
            if (false === $ext = array_search(
                    $f_info->file($file['tmp_name']),
                    array(
                        'jpg' => 'image/jpeg',
                        'png' => 'image/png',
                        'gif' => 'image/gif',
                    ),
                    true
                ))
            {
                $msg['status']=4;
                $msg['desc']="格式错误！";
                throw new RuntimeException($msg['desc']);
            }

            $upload_rs = Img::uploadImg($file['tmp_name'], $file['name'],$_POST['id'], Img::TYPE_FLEA);
            if ($upload_rs['status'] != 0) {
                $upload_rs['msg'] .= "图片上传失败。";
                $msg['status']=$upload_rs['status'];
                $msg['msg']=$upload_rs['msg'];
                throw new RuntimeException($msg['desc']);
            }

            $msg['status']=$upload_rs['status'];
            $msg['msg']=$upload_rs['msg'];
            $msg['data'][]=array('url'=>$upload_rs['url']);

        }catch (Exception $e){
            Yii::log($e->getMessage(),'error','application.firebuglog');
        }
        echo json_encode($msg);
        return;
    }

    public function actionGetid(){
        $id=Utils::GenerateSerialNumber();
        $msg['status']=0;
        $msg['msg']='成功';
        $msg['data'][]=array('id'=>$id);
        echo json_encode($msg);
        return;
    }
}