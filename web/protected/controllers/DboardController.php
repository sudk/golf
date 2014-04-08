<?php
/*
 * 模块编号: M1001
 */
class DboardController extends BaseController
{

    public function accessRules()
    {
        return array(
            array('allow',
                'users' => array('@'),
            ),
            array('deny',
                'actions' => array(),
            ),
        );
    }

	public function actionIndex()
	{
        return $this->actionSystem();
	}

	public function actionSystem()
	{
        $this->render('system');
	}

}