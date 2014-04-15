<?php
/*
 * 模块编号: M1001
 */
class TestController extends BaseController
{
    public function accessRules() {
        return array(
            array('allow',
                'actions' => array('index','cmd'),
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

    public function actionIndex(){
        $this->render("index");

    }

    public function actionCmd(){
        if($_POST['content']){
            $tuCurl = curl_init();
            $url="http://localhost/golf/web/index.php?r=command";
            curl_setopt($tuCurl, CURLOPT_URL,$url);
            curl_setopt($tuCurl, CURLOPT_VERBOSE, 0);
            curl_setopt($tuCurl, CURLOPT_HEADER,false);
            curl_setopt($tuCurl, CURLOPT_RETURNTRANSFER,false);
            curl_setopt($tuCurl, CURLOPT_POST,true);
            curl_setopt($tuCurl, CURLOPT_POSTFIELDS,$_POST['content']);
            curl_setopt($tuCurl, CURLOPT_HTTPHEADER, array("Content-Type:text/plain"));
            curl_exec($tuCurl);
            curl_close($tuCurl);
        }
    }

}