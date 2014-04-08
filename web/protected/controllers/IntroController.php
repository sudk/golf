<?php
/**
 * Created by JetBrains PhpStorm.
 * User: wangdy
 * Date: 12-2-9
 * Time: 下午1:27
 * To change this template use File | Settings | File Templates.
 */
class IntroController extends CController
{
    public $defaultAction = 'index';

    public function actionIndex()
    {
        $this->renderPartial('index');
    }
}
