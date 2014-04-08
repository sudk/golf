<?php
/*
 * 球场管理
 */
class CourtController extends BaseController
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