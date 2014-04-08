<?php

// This is the configuration for yiic console application.
// Any writable CConsoleApplication properties can be configured here.
return array(
	'basePath'=>dirname(__FILE__).DIRECTORY_SEPARATOR.'..',
	'name'=>'My Console Application',
    // autoloading model and component classes
    'import' => array(
        'application.models.*',
        'application.components.*',
        'application.components.widgets.*',
        'ext.PHPExcel.*',
    ),
    // application components
    'components' => array(
        'db' => array(
            'connectionString' => 'mysql:host=192.168.22.35;port=3306; dbname=posm_bate',
            'emulatePrepare' => true,
            'username' => 'root',
            'password' => '123456',
            'charset' => 'utf8',
        ),
    ),
    'params' => require(dirname(__FILE__) . '/params.php'),
);
