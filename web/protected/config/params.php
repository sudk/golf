<?php

return array(
    //图片上传路径
    'upload_dir'=>dirname(__FILE__)."/../runtime/picture/",
    'cmd_status'=>array(
        0=>'成功',
        -1=>"命令格式错误",
        1=>"用户名或密码错误",
        -2=>'密钥过期',
        -3=>'缺少参数',
    ),
);