<?php

/**
 * 用户接口
 * @author sudk
 */
class CourtController extends CMDBaseController
{
    public function accessRules() {
        return array(

            array('allow',
                'users' => array('*'),
            ),
            array('allow',
                'users' => array('@'),
            ),
            array('deny',
                'actions' => array(),
            ),
        );
    }

    public function actionSearch()
    {

    }

}