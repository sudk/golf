<?php

return array(
    'user' => array(
        'type' => CAuthItem::TYPE_TASK,
        'display'=>true,
        'description' => '会员管理',
        'children' => array(
             
            'user/uinfo/grid' ,
            'user/uinfo/list' ,
            'user/uinfo/edit' ,
            'user/uinfo/resetpwd' ,
            'user/uinfo/del' ,
            'user/uinfo/detail' ,
            'user/uscore/grid' ,
            'user/uscore/list' ,
            'user/uscore/detail' ,
            'user/uscore/dgrid',
            'user/uscore/scoredetail',
            'user/uinfo/mycard',
            'user/uinfo/cardlist',
            'user/uinfo/newcard',
            'user/uinfo/delcard',
            'user/uscore/export',
            
            'user/uinfo/agrid' ,
            'user/uinfo/account' ,
            'user/uinfo/cgrid' ,
            'user/uinfo/consume' ,
            'user/uinfo/ogrid' ,
            'user/uinfo/order' ,
           
        ),
        'bizRules' => '',
        'data' => ''
    ),
    'user_r' => array(
        'type' => CAuthItem::TYPE_TASK,
        'display'=>true,
        'description' => '会员管理-查看',
        'children' => array(
            
            
            'user/uinfo/grid' ,
            'user/uinfo/list' ,
            'user/uinfo/detail' ,
            'user/uinfo/mycard',
            'user/uinfo/cardlist',
            'user/uinfo/agrid' ,
            'user/uinfo/account' ,
            'user/uinfo/cgrid' ,
            'user/uinfo/consume' ,
            'user/uinfo/ogrid' ,
            'user/uinfo/order' ,
            
            'user/uscore/grid' ,
            'user/uscore/list' ,
            'user/uscore/detail' ,
            'user/uscore/dgrid',
            'user/uscore/scoredetail',
        ),
        'bizRules' => '',
        'data' => ''
    ),
    'order' => array(
        'type' => CAuthItem::TYPE_TASK,
        'display'=>true,
        'description' => '订单管理',
        'children' => array(
            'order/grid' ,
            'order/list' ,
            'order/detail' ,
            'order/edit' ,
            'order/lgrid' ,
            'order/log' ,
            'order/nextstatus',
            'order/confirmstatus',
            'order/del',
            'order/export',
        ),
        'bizRules' => '',
        'data' => ''
    ),
    'order_r' => array(
        'type' => CAuthItem::TYPE_TASK,
        'display'=>true,
        'description' => '订单管理-查看',
        'children' => array(
            'order/grid' ,
            'order/list' ,
            'order/detail' ,
            'order/lgrid' ,
            'order/log' ,
        ),
        'bizRules' => '',
        'data' => ''
    ),
    'court' => array(
        'type' => CAuthItem::TYPE_TASK,
        'display'=>true,
        'description' => '球场管理',
        'children' => array(
             'court/grid' ,
            'court/list' ,
            'court/new' ,
            'court/edit' ,
            'court/checkid' ,
            'court/getcity' ,
            'court/del' ,
            'court/detail' ,
            'court/showpoint' ,
            'court/showpic' ,
            'court/piclist' ,
            'court/newpic' ,
            'court/loadpic' ,
            'court/delpic' ,
            'comment/list' ,
            'comment/grid' ,
            'court/mycomment' ,
            'court/mycommentlist',
            
            ),
        'bizRules' => '',
        'data' => ''
    ),
    'court_r' => array(
        'type' => CAuthItem::TYPE_TASK,
        'display'=>true,
        'description' => '球场管理-查看',
        'children' => array(
            'court/grid' ,
            'court/list' ,         
            'court/getcity' ,      
            'court/detail' ,
            'court/showpoint' ,
            'court/showpic' ,
            'court/piclist' ,      
            'court/loadpic' ,   
            'comment/list' ,
            'comment/grid' ,
            'court/mycomment' ,
            'court/mycommentlist',
            
            ),
        'bizRules' => '',
        'data' => ''
    ),
    'price' => array(
        'type' => CAuthItem::TYPE_TASK,
        'display'=>true,
        'description' => '报价单管理',
        'children' => array(
            	'price/grid' ,
                'price/list' ,
                'price/newpolicy' ,
                'price/edit' ,
                'price/del' ,
                'price/delpolicydetail' ,
                'price/detail' ,
                'price/customdetail',
                'price/policygrid' ,
                'price/policy' ,
                'price/customgrid' ,
                'price/custom' ,
                'price/specialgrid' ,
                'price/special' ,
                'price/template' ,
                'price/downtemplate' ,
                'price/loadtemplate' ,
                'price/copypolicy' ,

        ),
        'bizRules' => '',
        'data' => ''
    ),
    'price_r' => array(
        'type' => CAuthItem::TYPE_TASK,
        'display'=>false,
        'description' => '报价单管理-查看',
        'children' => array(
                'price/grid' ,
                'price/list' ,
               
                'price/detail' ,
                'price/policygrid' ,
                'price/policy' ,
                'price/customgrid' ,
                'price/custom' ,
                'price/specialgrid' ,
                'price/special' ,
                
        ),
        'bizRules' => '',
        'data' => ''
    ),
    'service_comp' => array(
        'type' => CAuthItem::TYPE_TASK,
        'display'=>true,
        'description' => '特色服务-套餐管理',
        'children' => array(
            'service/competition/grid' ,
            'service/competition/list' ,
            'service/competition/new' ,
            'service/competition/edit' ,
            'service/competition/checkid' ,
            'service/competition/del' ,
            'service/competition/detail' ,
        ),
        'bizRules' => '',
        'data' => ''
    ),
    'service_adv' => array(
        'type' => CAuthItem::TYPE_TASK,
        'display'=>true,
        'description' => '特色服务-广告管理',
        'children' => array(
           'service/adv/grid' ,
            'service/adv/list' ,
            'service/adv/new' ,
             'service/adv/edit' ,
             'service/adv/del',
        ),
        'bizRules' => '',
        'data' => ''
    ),
    'service_goods' => array(
        'type' => CAuthItem::TYPE_TASK,
        'display'=>true,
        'description' => '特色服务-精挑细选审核',
        'children' => array(
           
            'service/goods/grid' ,
            'service/goods/list' ,
            'service/goods/audit',
            'service/goods/detail' ,
            'service/goods/del' ,
        ),
        'bizRules' => '',
        'data' => ''
    ),
    
    'service_goods_new' => array(
        'type' => CAuthItem::TYPE_TASK,
        'display'=>true,
        'description' => '特色服务-精挑细选商品添加',
        'children' => array(
           
            'service/goods/grid' ,
            'service/goods/list' ,
            'service/goods/newgoods',
            'service/goods/detail' ,
            
        ),
        'bizRules' => '',
        'data' => ''
    ),
    
    
    
    'service_mcht' => array(
        'type' => CAuthItem::TYPE_TASK,
        'display'=>true,
        'description' => '特色服务-特约商户管理',
        'children' => array(
            
            'service/mcht/grid' ,
            'service/mcht/list' ,
            'service/mcht/new' ,
            'service/mcht/edit' ,
            'service/mcht/checkid' ,
            'service/mcht/del' ,
            'service/mcht/detail' ,
        ),
        'bizRules' => '',
        'data' => ''
    ),
    'service_route' => array(
        'type' => CAuthItem::TYPE_TASK,
        'display'=>true,
        'description' => '特色服务-套餐管理',
        'children' => array(
            'service/route/grid' ,
            'service/route/list' ,
            'service/route/new' ,
            'service/route/edit' ,
            'service/route/del' ,
            'service/route/checkid' ,
            'service/route/detail' ,
        ),
        'bizRules' => '',
        'data' => ''
    ),
    'service_news'=>array(
        'type' => CAuthItem::TYPE_TASK,
        'display'=>true,
        'description' => '特色服务-新闻管理',
        'children'=>array(
            'service/news/grid',
            'service/news/list',
            'service/news/new',
            'service/news/edit',
            'service/news/checkid',
            'service/news/del',
            'service/news/detail',
        )
    ),
    'consume_log' => array(
        'type' => CAuthItem::TYPE_TASK,
        'display'=>true,
        'description' => '报表管理-消费记录',
        'children' => array(
           'rpt/consume/grid' ,
            'rpt/consume/list' ,
            'rpt/consume/detail' ,
            'rpt/recharge/grid' ,
            'rpt/recharge/list' ,
            
            'rpt/consume/summary' ,
            'rpt/consume/sgrid' ,
            
            'rpt/consume/agent' ,
            'rpt/consume/agrid' ,
             'rpt/consume/agentd' ,
            'rpt/consume/adgrid' ,
            
            'rpt/consume/order' ,
            'rpt/consume/ogrid' ,
             'rpt/consume/orderd' ,
            'rpt/consume/odgrid' ,
            
            'rpt/consume/user' ,
            'rpt/consume/ugrid' ,
             'rpt/consume/userd' ,
            'rpt/consume/udgrid' ,
            
            'rpt/consume/balance' ,
            'rpt/consume/bgrid' ,
             'rpt/consume/balanced' ,
            'rpt/consume/bdgrid' ,
        ),
        'bizRules' => '',
        'data' => ''
    ),
    'msg' => array(
        'type' => CAuthItem::TYPE_TASK,
        'display'=>false,
        'description' => '消息管理',
        'children' => array(
            'msg/inbox/grid',
            'msg/inbox/list',
            'msg/outbox/grid',
            'msg/outbox/list',
            'msg/outbox/new',
            'msg/outbox/del',
            'msg/outbox/show',
            'msg/outbox/detail'
        ),
        'bizRules' => '',
        'data' => ''
    ),
    'systemlog' => array(
        'type' => CAuthItem::TYPE_TASK,
        'display'=>false,
        'description' => '系统操作日志',
        'children' => array(
            'log/systemlog/grid','log/systemlog/list','log/systemlog/detail'
        ),
        'bizRules' => '',
        'data' => ''
    ),
    
    'systemset' => array(
        'type' => CAuthItem::TYPE_TASK,
        'display'=>false,
        'description' => '系统设置',
        'children' => array(
            'operator/set/grid','operator/set/list','operator/set/edit'
        ),
        'bizRules' => '',
        'data' => ''
    ),
    
    'agent' => array(
        'type' => CAuthItem::TYPE_TASK,
        'display'=>true,
        'description' => '代理商管理',
        'children' => array(
           'operator/agent/grid' ,
            'operator/agent/list' ,
            'operator/agent/new' ,
            'operator/agent/edit' ,
            'operator/agent/checkagentname' ,
            'operator/agent/del' ,
        ),
        'bizRules' => '',
        'data' => ''
    ),
    
    'operator' => array(
        'type' => CAuthItem::TYPE_TASK,
        'display'=>false,
        'description' => '操作员管理',
        'children' => array(
            'operator/operator/grid' ,
            'operator/operator/list' ,
            'operator/operator/new' ,
            'operator/operator/edit' ,
            'operator/operator/editpri' ,
            'operator/operator/checkid' ,
            'operator/operator/checkloginid' ,
            'operator/operator/del' ,
            'operator/operator/detail' ,
            'auth/edit' ,
            'auth/delete' ,
        ),
        'bizRules' => '',
        'data' => ''
    ),
    
    'default' => array(
        'type' => CAuthItem::TYPE_TASK,
        'display'=>false,
        'description' => '默认权限',
        'children' => array(
           'dboard/index' ,
           'dboard/system' ,           
            'site/s' ,
            'site/queryarea' ,
            'site/applyquery' ,
            'site/index' ,
            'site/error' ,
            'site/login' ,
            'site/getpass' ,
            'site/logout' ,
            'site/passwd' ,
            'site/updateoperation' ,
            'site/showtask' ,
            'test/index' ,
            'test/cmd' ,
           'operator/operator/editpri' ,
        ),
        'bizRules' => '',
        'data' => ''
    ),
    
    
    
);

