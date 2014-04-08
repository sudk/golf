<?php
/*
 * 价格管理
 */
class PriceController extends BaseController
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

	

}