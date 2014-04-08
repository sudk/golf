<?php
/**
 * Created by JetBrains PhpStorm.
 * User: wangdy
 * Date: 12-2-6
 * Time: 下午2:27
 * To change this template use File | Settings | File Templates.
 */
class ProfileController extends CController
{
    public function actionSmsnotice()
    {
        if(!Yii::app()->user->usertype==UserIdentity::USERTYPE_SCHOOL)
            return;

        $model = Staff::model()->findbyPk(Yii::app()->user->id);

        $message = '';

        if (isset($_POST['Staff']))
        {
            $model->notice2sms = $_POST['Staff']['notice2sms'];
            $model->remark2sms = $_POST['Staff']['remark2sms'];
            $model->attendance2sms = $_POST['Staff']['attendance2sms'];

            $model->save();
            $message = '修改成功';
        }
        $this->layout='//layouts/base';
        $this->render('smsnotice', array('model' => $model, 'message' => $message));

    }
}
