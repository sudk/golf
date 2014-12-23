<?php

class NotifyController extends CController {

    public function actionIndex(){
        $upmp_pay=new UpmPay();
        $upmp_pay->Notice($_POST);
    }

}
