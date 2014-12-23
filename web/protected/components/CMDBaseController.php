<?php
/**
 * Controller is the customized base controller class.
 * All controller classes for this application should extend from this base class.
 */
class CMDBaseController extends CController
{

    public $pageSize=20;

     public function init()
     {

     }

    /**
	 * Checks if rbac access is granted for the current user
	 * @param String $action . The current action
	 * @return boolean true if access is granted else false
	*/
	protected function beforeAction($action)
	{
        $rules=$this->accessRules();
        if(isset($rules['login'])){
            $actions=$rules['login'];
            if(in_array($action->getId(),$actions)){
                if(Yii::app()->user->isGuest){
                    $msg['status']=-1;
                    $msg['desc']="用户未登陆！";
                    echo json_encode($msg);
                    return false;
                }else{
                    return true;
                }
            }else{

                return true;
            }
        }else{
            return true;
        }
    }
}