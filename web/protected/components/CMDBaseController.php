<?php
/**
 * Controller is the customized base controller class.
 * All controller classes for this application should extend from this base class.
 */
class CMDBaseController extends CController
{

    public function filters()
    {
        return array(
            'accessControl',
        );
    }

    public function accessRules() {
        return array(
            array('allow',
                'users' => array('*'),
            ),
            array('allow',
                'actions' => array('u'),
                'users' => array('@'),
            ),
            array('deny',
                'actions' => array(),
            ),
        );
    }

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
        return true;
    }
}