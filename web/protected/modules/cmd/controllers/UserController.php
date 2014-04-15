<?php

/**
 * 用户接口
 * @author sudk
 */
class UserController extends CMDBaseController
{
    public function actionLogin()
    {

        if (!Yii::app()->command->cmdObj->phone) {
            $msg['desc']="缺少参数";
            $msg['status']=-1;
            echo json_encode($msg);
            return;
        }

        if (!Yii::app()->command->cmdObj->passwd) {
            $msg['desc']="缺少参数";
            $msg['status']=-1;
            echo json_encode($msg);
            return;
        }

        $identity = new MUserIdentity(trim(Yii::app()->command->cmdObj->phone), trim(Yii::app()->command->cmdObj->passwd));
        $identity->authenticate();
        switch ($identity->errorCode) {
            case UserIdentity::ERROR_NONE:
                $duration = isset($form['rememberMe']) ? 3600 * 24 * 1 : 0; // 1 day
                Yii::app()->user->login($identity);
                if ($duration !== 0) {
                    setcookie('golf', trim($form['username']), time() + $duration, Yii::app()->request->baseUrl);
                } else {
                    unset($_COOKIE['golf']);
                    setcookie('golf', NULL, -1);
                }
                $message = '成功！';
                $status=0;
                break;
            case UserIdentity::ERROR_USERNAME_INVALID:
                $message = '用户名错误！';
                $status=1;
                break;
            case UserIdentity::ERROR_PASSWORD_INVALID:
                $message = '密码错误！';
                $status=2;
                break;
            default:
                $message = '用户名或口令错误！';
                $status=3;
                break;
        }
        $msg['desc']=$message;
        $msg['status']=$status;
        echo json_encode($msg);

    }

    public function actionRegiste(){
        $model=new User('create');
        $model->phone=Yii::app()->command->cmdObj->phone;
        $model->user_name=Yii::app()->command->cmdObj->user_name;
        $model->card_no=Yii::app()->command->cmdObj->card_no;
        $model->email=Yii::app()->command->cmdObj->email;
        $model->sex=Yii::app()->command->cmdObj->sex;
        $model->record_time=date("Y-m-d H:i:s");
        $model->passwd=crypt(Yii::app()->command->cmdObj->passwd);
        if($model->validate()&&$model->save()){
            $msg['status']=0;
            $msg['desc']="成功";
        }else{
            $msg['status']=1;
            $msg['desc']="失败1";
        }
        echo json_encode($msg);
    }

    public function actionSecurity()
    {

        //echo session_id();
        echo "-----";
        //Yii::app()->fcache->set(session_id(),);

    }
}