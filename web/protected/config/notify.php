<?php

// uncomment the following to define a path alias
// Yii::setPathOfAlias('local','path/to/local-folder');

return array(
    'basePath'=>dirname(__FILE__).DIRECTORY_SEPARATOR.'..',
    'name'=>'高球生活管理系统',
    'language' => 'zh_cn',
    // preloading 'log' component
    'preload'=>array('log'),
    'runtimePath'=>dirname(__FILE__).DIRECTORY_SEPARATOR.'..'.DIRECTORY_SEPARATOR.'runtime',
    //'runtimePath'=>'/usr/local/webapp/1430/runtime',
    'import'=>array(
        'application.models.*',
        'application.components.*',
        'application.components.widgets.*',
	'ext.PHPExcel.*',
        'ext.phpexcelr.*',
        'ext.redis.*',
        'ext.phpexcelreader.*'
    ),
    'defaultController'=>'notify',
	//配置模块信息
    // application components
    'components'=>array(
        'db' => array(
            'connectionString' => 'mysql:host=115.28.77.119;port=3306; dbname=golf',
            'emulatePrepare' => true,
            'enableProfiling'=>true,
            'username' => 'root',
            'password' => 'qgolf@1qazxcde3',
            'charset' => 'utf8',
        ),

        'log'=>array(
            'class'=>'CLogRouter',
            'routes'=>array(
                array(
                    'class'=>'CFileLogRoute',
                    'levels'=>'error, warning, info',
                ),
            ),
        ),
		 'fcache'=>array(
            'class'=>'system.caching.CFileCache'
            ),
        'command'=>array(
            'class'=>'application.components.Command'
        ),
    ),
    'modules'=>array(
        'cmd'=>array(),
        'log'=>array(),
        'operator'=>array(),
        'msg'=>array(),
        'rpt'=>array(),
        'user'=>array(),
        'service'=>array(),
    ),
    'params' => require(dirname(__FILE__) . '/params.php'),
);
