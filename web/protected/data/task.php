<?php

return array(
    'posrpt' => array(
        'type' => CAuthItem::TYPE_TASK,
        'display'=>true,
        'description' => 'POS交易报表',
        'children' => array(
            'posrpt_posmanfeat','posrpt_eff','posrpt_detail'
        ),
        'bizRules' => '',
        'data' => ''
    ),
    'posrpt_posmanfeat' => array(
        'type' => CAuthItem::TYPE_TASK,
        'display'=>false,
        'description' => 'POS交易报表-客户经理业绩报表',
        'children' => array(
         'posrpt/posmanfeat/grid','posrpt/posmanfeat/list','posrpt/posmanfeat/export','posrpt/posmanfeat/getstatus','posrpt/posmanfeat/ajaxexport',

        ),
        'bizRules' => '',
        'data' => ''
    ),
    'posrpt_eff' => array(
        'type' => CAuthItem::TYPE_TASK,
        'display'=>false,
        'description' => 'POS交易报表-有效率报表',
        'children' => array(
            'posrpt/poseff/grid','posrpt/poseff/list','posrpt/poseff/export','posrpt/poseff/getstatus','posrpt/poseff/ajaxexport','posrpt/poseff/detail',

        ),
        'bizRules' => '',
        'data' => ''
    ),
    'posrpt_detail' => array(
        'type' => CAuthItem::TYPE_TASK,
        'display'=>false,
        'description' => 'POS交易报表-交易明细报表',
        'children' => array(
            'posrpt/posdetail/grid','posrpt/posdetail/list'
        ),
        'bizRules' => '',
        'data' => ''
    ),
    'riskc' => array(
        'type' => CAuthItem::TYPE_TASK,
        'display'=>true,
        'description' => '风险监控',
        'children' => array(
            'riskc/dayc/grid','riskc/dayc/list','riskc/dayc/export','riskc/dayc/getstatus','riskc/dayc/ajaxexport','riskc/dayc/detail','riskc/monthc/grid','riskc/monthc/list','riskc/monthc/export','riskc/monthc/getstatus','riskc/monthc/ajaxexport','riskc/monthc/detail','riskc/monthu/grid','riskc/monthu/list','riskc/monthu/export','riskc/monthu/getstatus','riskc/monthu/ajaxexport','riskc/monthu/detail','riskc/posmanfeat/grid','riskc/posmanfeat/list','riskc/posmanfeat/export','riskc/posmanfeat/getstatus','riskc/posmanfeat/ajaxexport','riskc/repeatc/grid','riskc/repeatc/list','riskc/singlec/grid','riskc/singlec/list','riskc/timeinter/grid','riskc/timeinter/list','riskc/timeout/grid','riskc/timeout/list','riskc/transamount/grid','riskc/transamount/list'
        ),
        'bizRules' => '',
        'data' => ''
    ),
    'busirpt' => array(
        'type' => CAuthItem::TYPE_TASK,
        'display'=>true,
        'description' => '业务报表',
        'children' => array(
            'busirpt/cdailay/list','busirpt/cmanager/grid','busirpt/cmanager/list','busirpt/cmanager/detail','busirpt/pdailay/list','busirpt/poverall/list','busirpt/cmanager/download'
        ),
        'bizRules' => '',
        'data' => ''
    ),
    'mchtm' => array(
        'type' => CAuthItem::TYPE_TASK,
        'display'=>true,
        'description' => '商户管理',
        'children' => array(
            	'mchtm/mcht/grid',
        		'mchtm/mcht/list',
        		'mchtm/mcht/new',
        		'mchtm/mcht/edit',
        		'mchtm/mcht/checkid',
        		'mchtm/mcht/del',
        		'mchtm/mcht/detail',
        		'mchtm/mcht/changepw'

        ),
        'bizRules' => '',
        'data' => ''
    ),
    'mchtm_r' => array(
        'type' => CAuthItem::TYPE_TASK,
        'display'=>false,
        'description' => '商户管理-查看',
        'children' => array(
            'mchtm/mcht/grid','mchtm/mcht/list','mchtm/mcht/detail','mchtm/mcht/checkid','mchtm/mcht/detail',
        ),
        'bizRules' => '',
        'data' => ''
    ),
    'posm' => array(
        'type' => CAuthItem::TYPE_TASK,
        'display'=>true,
        'description' => 'POS终端管理',
        'children' => array(
            'posm/posinfo/grid','posm/posinfo/list','posm/posinfo/new','posm/posinfo/edit','posm/posinfo/checkid','posm/posinfo/del','posm/posinfo/detail',

        ),
        'bizRules' => '',
        'data' => ''
    ),
    'posm_r' => array(
        'type' => CAuthItem::TYPE_TASK,
        'display'=>false,
        'description' => 'POS终端管理-查看',
        'children' => array(
            'posm/posinfo/grid','posm/posinfo/list','posm/posinfo/checkid','posm/posinfo/detail','posm/posinfo/checkid',

        ),
        'bizRules' => '',
        'data' => ''
    ),
    'staffm' => array(
        'type' => CAuthItem::TYPE_TASK,
        'display'=>true,
        'description' => '人员管理',
        'children' => array(
            'operator/operator/grid','operator/operator/list','operator/operator/new','operator/operator/edit','operator/operator/checkid','operator/operator/checkloginid','operator/operator/del','operator/operator/detail',
            'auth/edit','auth/delete',
        ),
        'bizRules' => '',
        'data' => ''
    ),
    'msg' => array(
        'type' => CAuthItem::TYPE_TASK,
        'display'=>true,
        'description' => '消息管理',
        'children' => array(
            'msg/inbox/grid','msg/inbox/list','msg/outbox/grid','msg/outbox/list','msg/outbox/new','msg/outbox/del','msg/outbox/show','msg/outbox/detail'
        ),
        'bizRules' => '',
        'data' => ''
    ),
    'systemlog' => array(
        'type' => CAuthItem::TYPE_TASK,
        'display'=>true,
        'description' => '系统操作日志',
        'children' => array(
            'log/systemlog/grid','log/systemlog/list','log/systemlog/detail'
        ),
        'bizRules' => '',
        'data' => ''
    ),
    'log' => array(
        'type' => CAuthItem::TYPE_TASK,
        'display'=>true,
        'description' => '交易记录获取日志',
        'children' => array(
            'log/translog/grid','log/translog/list',
        ),
        'bizRules' => '',
        'data' => ''
    ),
    'normaltask' => array(
        'type' => CAuthItem::TYPE_TASK,
        'display'=>false,
        'description' => '一般任务',
        'children' => array(
            'dboard/index','dboard/system','intro/index','site/s','site/chart','site/index','site/error','site/contact','site/login','site/getpass','site/logout','site/passwd','site/switchschool','site/updateoperation','site/showtask','operator/operator/editpri','msg/outbox/show','workflow/install/edit'
        ),
        'bizRules' => '',
        'data' => ''
    ),
	'workflow'=> array(
        'type' => CAuthItem::TYPE_TASK,
        'display'=>false,
        'description' => '工作流-客户经理',//客户经理
        'children' => array(
            'workflow/install/grid','workflow/poshook/grid','workflow/poshook/detail','workflow/poshook/new','workflow/install/list','workflow/install/new','workflow/install/edit','workflow/install/checkid','workflow/install/del','workflow/install/upfile','workflow/install/getattachs','workflow/install/detail','workflow/install/apps','workflow/install/sta','workflow/install/checktype','workflow/install/genmchid','workflow/install/allocate','workflow/install/inst','workflow/install/downexcel','workflow/poshook/index',
        ),
        'bizRules' => '',
        'data' => ''
    ),
    'workflow_m'=> array(
        'type' => CAuthItem::TYPE_TASK,
        'display'=>false,
        'description' => '工作流-终端维护',//终端维护人员
        'children' => array(
            'workflow/install/grid','workflow/poshook/grid','workflow/poshook/detail','workflow/poshook/new','workflow/poshook/index','workflow/install/list','workflow/install/checkid','workflow/install/upfile','workflow/install/getattachs','workflow/install/detail','workflow/install/apps','workflow/install/checktype','workflow/install/genmchid','workflow/install/allocate','workflow/install/inst',
        ),
        'bizRules' => '',
        'data' => ''
    ),
    'visit_r'=> array(
        'type' => CAuthItem::TYPE_TASK,
        'display'=>false,
        'description' => '回访-查看',
        'children' => array(
            'mchtm/mchtvisit/grid',
            'mchtm/mchtvisit/list',
            'mchtm/mchtvisit/detail',
            'mchtm/mchtvisit/download',
            'mchtm/mchtvisit/bdownload'
        ),
        'bizRules' => '',
        'data' => ''
    ),
    'visit_visit'=> array(
        'type' => CAuthItem::TYPE_TASK,
        'display'=>false,
        'description' => '回访-处理',
        'children' => array(
            'visit_view',
            'mchtm/mchtvisit/status'
        ),
        'bizRules' => '',
        'data' => ''
    ),
    'visit_check'=> array(
        'type' => CAuthItem::TYPE_TASK,
        'display'=>false,
        'description' => '回访-确认',
        'children' => array(
            'visit_view',
            'mchtm/mchtvisit/status'
        ),
        'bizRules' => '',
        'data' => ''
    ),
    'visit_cancel'=> array(
        'type' => CAuthItem::TYPE_TASK,
        'display'=>false,
        'description' => '回访-撤销',
        'children' => array(
            'visit_view',
            'mchtm/mchtvisit/status'
        ),
        'bizRules' => '',
        'data' => ''
    ),
    'visit_change'=> array(
        'type' => CAuthItem::TYPE_TASK,
        'display'=>false,
        'description' => '回访-更换回访人',
        'children' => array(
            'visit_view',
            'mchtm/mchtvisit/changevisitor'
        ),
        'bizRules' => '',
        'data' => ''
    ),
	'mcht_reconcile'=> array(
		'type' => CAuthItem::TYPE_TASK,
		'display'=>false,
		'description' => '商户对账',
		'children' => array(
				'reconcile/monthrpt/list','reconcile/monthrpt/grid',
				'reconcile/dayrpt/list','reconcile/dayrpt/grid',
				'reconcile/timerpt/list','reconcile/timerpt/grid',
				'mchtm/mcht/changepw','reconcile/dayrpt/remotedata',
		),
		'bizRules' => '',
		'data' => ''
		),
);

