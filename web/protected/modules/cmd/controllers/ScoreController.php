<?php

/**
 * 记分
 * @author sudk
 */
class ScoreController extends CMDBaseController
{
    public function accessRules() {
        return array('login' => array('list','create','update','del','dlist','dcreate','dupdate','ddel'));
    }

    public function actionList(){

        if(Yii::app()->command->cmdObj->_pg_==null||Yii::app()->command->cmdObj->_pg_==""){
            $msg['status']=1;
            $msg['desc']="分页参数不能为空！";
            echo json_encode($msg);
            return;
        }

        $rows=Score::InfoList(Yii::app()->command->cmdObj->_pg_,$this->pageSize,Yii::app()->command->cmdObj);
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
        $row=Score::Info(Yii::app()->command->cmdObj->id);
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
            $msg['desc']="球场不能为空！";
            echo json_encode($msg);
            return;
        }

        if(!isset(Yii::app()->command->cmdObj->holes)||Yii::app()->command->cmdObj->holes==''){
            $msg['status']=2;
            $msg['desc']="球洞数不能为空！";
            echo json_encode($msg);
            return;
        }

        if(!isset(Yii::app()->command->cmdObj->fee_time)||Yii::app()->command->cmdObj->fee_time==''){
            $msg['status']=3;
            $msg['desc']="打球时间不能为空！";
            echo json_encode($msg);
            return;
        }

        if(!isset(Yii::app()->command->cmdObj->is_show)||Yii::app()->command->cmdObj->is_show==''){
            $msg['status']=4;
            $msg['desc']="是否保密不能为空！";
            echo json_encode($msg);
            return;
        }

        $user_id=Yii::app()->user->id;
        $conn=Yii::app()->db;
        $transaction = $conn->beginTransaction();
        $id=Utils::GenerateSerialNumber();
        $record_time=date("Y-m-d H:i:s");
        try{
            $sql="insert into g_score (id,court_id,user_id,holes,fee_time,team_menbers,is_show,record_time) values (:id,:court_id,:user_id,:holes,:fee_time,:team_menbers,:is_show,:record_time)";
            $command = $conn->createCommand($sql);
            $command->bindParam(":id",$id, PDO::PARAM_STR);
            $command->bindParam(":court_id", Yii::app()->command->cmdObj->court_id, PDO::PARAM_STR);
            $command->bindParam(":user_id",$user_id, PDO::PARAM_STR);
            $command->bindParam(":holes", Yii::app()->command->cmdObj->holes, PDO::PARAM_STR);
            $command->bindParam(":fee_time", Yii::app()->command->cmdObj->fee_time, PDO::PARAM_STR);
            $command->bindParam(":team_menbers", Yii::app()->command->cmdObj->team_menbers, PDO::PARAM_STR);
            $command->bindParam(":is_show", Yii::app()->command->cmdObj->is_show, PDO::PARAM_STR);
            $command->bindParam(":record_time",$record_time, PDO::PARAM_STR);
            $command->execute();
            $transaction->commit();
            $msg['status']=0;
            $msg['desc']="成功";
        }catch (Exception $e){
            $transaction->rollBack();
            $msg['status']=8;
            $msg['desc']="保存失败";
        }
        echo json_encode($msg);
        return;
    }

    public function actionUpdate(){
        if(!isset(Yii::app()->command->cmdObj->court_id)||Yii::app()->command->cmdObj->court_id==''){
            $msg['status']=1;
            $msg['desc']="球场不能为空！";
            echo json_encode($msg);
            return;
        }

        if(!isset(Yii::app()->command->cmdObj->holes)||Yii::app()->command->cmdObj->holes==''){
            $msg['status']=2;
            $msg['desc']="球洞数不能为空！";
            echo json_encode($msg);
            return;
        }

        if(!isset(Yii::app()->command->cmdObj->fee_time)||Yii::app()->command->cmdObj->fee_time==''){
            $msg['status']=3;
            $msg['desc']="打球时间不能为空！";
            echo json_encode($msg);
            return;
        }

        if(!isset(Yii::app()->command->cmdObj->is_show)||Yii::app()->command->cmdObj->is_show==''){
            $msg['status']=4;
            $msg['desc']="是否保密不能为空！";
            echo json_encode($msg);
            return;
        }
        if(!isset(Yii::app()->command->cmdObj->id)||Yii::app()->command->cmdObj->id==''){
            $msg['status']=5;
            $msg['desc']="ID不能为空！";
            echo json_encode($msg);
            return;
        }

        $conn=Yii::app()->db;
        $transaction = $conn->beginTransaction();
        $user_id=Yii::app()->user->id;
        try{
            $sql="update g_score set court_id=:court_id,user_id=:user_id,holes=:holes,fee_time=:fee_time,team_menbers=:team_menbers,is_show=:is_show where id=:id";
            $command = $conn->createCommand($sql);
            $command->bindParam(":court_id", Yii::app()->command->cmdObj->court_id, PDO::PARAM_STR);
            $command->bindParam(":user_id",$user_id, PDO::PARAM_STR);
            $command->bindParam(":holes", Yii::app()->command->cmdObj->holes, PDO::PARAM_STR);
            $command->bindParam(":fee_time", Yii::app()->command->cmdObj->fee_time, PDO::PARAM_STR);
            $command->bindParam(":team_menbers", Yii::app()->command->cmdObj->team_menbers, PDO::PARAM_STR);
            $command->bindParam(":is_show", Yii::app()->command->cmdObj->is_show, PDO::PARAM_STR);
            $command->bindParam(":id",Yii::app()->command->cmdObj->id, PDO::PARAM_STR);
            $command->execute();
            $transaction->commit();
            $msg['status']=0;
            $msg['desc']="成功";
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
            $sql="delete from g_score where id=:id";
            $command = $conn->createCommand($sql);
            $command->bindParam(":id",Yii::app()->command->cmdObj->id, PDO::PARAM_STR);
            $command->execute();

            $sql="delete from g_score_detail where score_id=:score_id";
            $command = $conn->createCommand($sql);
            $command->bindParam(":score_id",Yii::app()->command->cmdObj->id, PDO::PARAM_STR);
            $command->execute();

            $transaction->commit();
            $msg['status']=0;
            $msg['desc']="成功";
        }catch (Exception $e){
            $transaction->rollBack();
            $msg['status']=7;
            $msg['desc']="删除失败";
        }
        echo json_encode($msg);
        return;
    }

    public function actionDlist(){
        if(Yii::app()->command->cmdObj->_pg_==null||Yii::app()->command->cmdObj->_pg_==""){
            $msg['status']=1;
            $msg['desc']="分页参数不能为空！";
            echo json_encode($msg);
            return;
        }
        if(Yii::app()->command->cmdObj->score_id==null||Yii::app()->command->cmdObj->score_id==""){
            $msg['status']=1;
            $msg['desc']="打球ID不能为空！";
            echo json_encode($msg);
            return;
        }

        $rows=ScoreDetail::InfoList(Yii::app()->command->cmdObj->_pg_,$this->pageSize,Yii::app()->command->cmdObj);
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

    public function actionDinfo(){
        if(!Yii::app()->command->cmdObj->id){
            $msg['status']=1;
            $msg['desc']="ID不能为空！";
            echo json_encode($msg);
            return;
        }
        $row=ScoreDetail::Info(Yii::app()->command->cmdObj->id);
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

    public function actionDcreate(){

        if(!isset(Yii::app()->command->cmdObj->score_id)||Yii::app()->command->cmdObj->score_id==''){
            $msg['status']=1;
            $msg['desc']="球场不能为空！";
            echo json_encode($msg);
            return;
        }

        if(!isset(Yii::app()->command->cmdObj->hole_no)||Yii::app()->command->cmdObj->hole_no==''){
            $msg['status']=2;
            $msg['desc']="洞号不能为空！";
            echo json_encode($msg);
            return;
        }

        if(!isset(Yii::app()->command->cmdObj->tee)||Yii::app()->command->cmdObj->tee==''){
            $msg['status']=3;
            $msg['desc']="T台不能为空！";
            echo json_encode($msg);
            return;
        }

        if(!isset(Yii::app()->command->cmdObj->standard_bar)||Yii::app()->command->cmdObj->standard_bar==''){
            $msg['status']=5;
            $msg['desc']="标准杆数不能为空！";
            echo json_encode($msg);
            return;
        }
        if(!isset(Yii::app()->command->cmdObj->lang_bar)||Yii::app()->command->cmdObj->lang_bar==''){
            $msg['status']=6;
            $msg['desc']="长杆数不能为空！";
            echo json_encode($msg);
            return;
        }
        if(!isset(Yii::app()->command->cmdObj->push_bar)||Yii::app()->command->cmdObj->push_bar==''){
            $msg['status']=7;
            $msg['desc']="推杆数不能为空！";
            echo json_encode($msg);
            return;
        }
        $conn=Yii::app()->db;
        $transaction = $conn->beginTransaction();
        $record_time=date("Y-m-d H:i:s");
        try{
            $sql="insert into g_score_detail (score_id,hole_no,tee,standard_bar,lang_bar,push_bar,record_time) values (:score_id,:hole_no,:tee,:standard_bar,:lang_bar,:push_bar,:record_time)";
            $command = $conn->createCommand($sql);
            $command->bindParam(":score_id", Yii::app()->command->cmdObj->score_id, PDO::PARAM_STR);
            $command->bindParam(":hole_no",Yii::app()->command->cmdObj->hole_no, PDO::PARAM_STR);
            $command->bindParam(":tee", Yii::app()->command->cmdObj->tee, PDO::PARAM_STR);
            $command->bindParam(":standard_bar", Yii::app()->command->cmdObj->standard_bar, PDO::PARAM_STR);
            $command->bindParam(":lang_bar", Yii::app()->command->cmdObj->lang_bar, PDO::PARAM_STR);
            $command->bindParam(":push_bar", Yii::app()->command->cmdObj->push_bar, PDO::PARAM_STR);
            $command->bindParam(":record_time",$record_time, PDO::PARAM_STR);
            $command->execute();
            $transaction->commit();
            $msg['status']=0;
            $msg['desc']="成功";
        }catch(Exception $e){
            $transaction->rollBack();
            $msg['status']=$e->getCode();
            $msg['desc']="保存失败";
            if($msg['status']=="23000"){
                $msg['desc']="洞号:".Yii::app()->command->cmdObj->hole_no."重复记分";
            }
        }
        echo json_encode($msg);
        return;
    }

    public function actionDupdate(){
        if(!isset(Yii::app()->command->cmdObj->score_id)||Yii::app()->command->cmdObj->score_id==''){
            $msg['status']=1;
            $msg['desc']="球场不能为空！";
            echo json_encode($msg);
            return;
        }

        if(!isset(Yii::app()->command->cmdObj->hole_no)||Yii::app()->command->cmdObj->hole_no==''){
            $msg['status']=2;
            $msg['desc']="洞号不能为空！";
            echo json_encode($msg);
            return;
        }

        if(!isset(Yii::app()->command->cmdObj->tee)||Yii::app()->command->cmdObj->tee==''){
            $msg['status']=3;
            $msg['desc']="T台不能为空！";
            echo json_encode($msg);
            return;
        }

        if(!isset(Yii::app()->command->cmdObj->standard_bar)||Yii::app()->command->cmdObj->standard_bar==''){
            $msg['status']=5;
            $msg['desc']="标准杆数不能为空！";
            echo json_encode($msg);
            return;
        }
        if(!isset(Yii::app()->command->cmdObj->lang_bar)||Yii::app()->command->cmdObj->lang_bar==''){
            $msg['status']=6;
            $msg['desc']="长杆数不能为空！";
            echo json_encode($msg);
            return;
        }
        if(!isset(Yii::app()->command->cmdObj->push_bar)||Yii::app()->command->cmdObj->push_bar==''){
            $msg['status']=7;
            $msg['desc']="推杆数不能为空！";
            echo json_encode($msg);
            return;
        }
        if(!isset(Yii::app()->command->cmdObj->id)||Yii::app()->command->cmdObj->id==''){
            $msg['status']=5;
            $msg['desc']="ID不能为空！";
            echo json_encode($msg);
            return;
        }

        $conn=Yii::app()->db;
        $transaction = $conn->beginTransaction();
        try{
            $sql="update g_score_detail set score_id=:score_id,hole_no=:hole_no,tee=:tee,standard_bar=:standard_bar,lang_bar=:lang_bar,push_bar=:push_bar where id=:id";
            $command = $conn->createCommand($sql);
            $command->bindParam(":score_id", Yii::app()->command->cmdObj->score_id, PDO::PARAM_STR);
            $command->bindParam(":hole_no",Yii::app()->command->cmdObj->hole_no, PDO::PARAM_STR);
            $command->bindParam(":tee", Yii::app()->command->cmdObj->tee, PDO::PARAM_STR);
            $command->bindParam(":standard_bar", Yii::app()->command->cmdObj->standard_bar, PDO::PARAM_STR);
            $command->bindParam(":lang_bar", Yii::app()->command->cmdObj->lang_bar, PDO::PARAM_STR);
            $command->bindParam(":push_bar", Yii::app()->command->cmdObj->push_bar, PDO::PARAM_STR);
            $command->bindParam(":id",Yii::app()->command->cmdObj->id, PDO::PARAM_STR);
            $command->execute();
            $transaction->commit();
            $msg['status']=0;
            $msg['desc']="成功";
        }catch (Exception $e){
            $transaction->rollBack();
            $msg['status']=8;
            $msg['desc']="保存失败";
        }
        echo json_encode($msg);
        return;
    }

    public function actionDdel(){
        if(!isset(Yii::app()->command->cmdObj->id)||Yii::app()->command->cmdObj->id==''){
            $msg['status']=1;
            $msg['desc']="ID不能为空！";
            echo json_encode($msg);
            return;
        }

        $conn=Yii::app()->db;
        $transaction = $conn->beginTransaction();
        try{
            $sql="delete from g_score_detail where id=:id";
            $command = $conn->createCommand($sql);
            $command->bindParam(":id",Yii::app()->command->cmdObj->id, PDO::PARAM_STR);
            $command->execute();

            $transaction->commit();
            $msg['status']=0;
            $msg['desc']="成功";
        }catch (Exception $e){
            $transaction->rollBack();
            $msg['status']=7;
            $msg['desc']="删除失败";
        }
        echo json_encode($msg);
        return;
    }

}