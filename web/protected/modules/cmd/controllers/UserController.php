<?php

/**
 * 用户接口
 * @author sudk
 */
class UserController extends MBaseController
{
    public function actionLogin(){

        $identity = new MUserIdentity(trim($form['username']), trim($form['passwd']));
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
                break;
            case UserIdentity::ERROR_USERNAME_INVALID:
                $message = '用户名错误！';
                break;
            case UserIdentity::ERROR_PASSWORD_INVALID:
                $message = '密码错误！';
                break;
            default: // UserIdentity::ERROR_PASSWORD_INVALID
                $message = '用户名或口令错误！';
                break;
        }
    }
}