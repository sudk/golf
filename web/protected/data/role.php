<?php
/*
 * 数据级别data：0级为最高级（不受任何限制），
 *               1级为受部分限制（目前和0级的区别在于查看交易明细时交易账号被加*号）；
 *               2级为客户经理或维护人员：只能看该客户经理所发展或维护的终端；
 * */
return array(
    'smanager' => array(
        'type' => CAuthItem::TYPE_ROLE,
        'description' => '超级管理员',
        'display'=>true,
        'children' => array(
            'posrpt',
            'mchtm',
            'posm',
            'staffm',
            'log',
            'riskc',
            'busirpt',
            'normaltask',
        	'workflow',
            'msg',
            'systemlog',
            'maintain_man',
            'visit_checker',
        ),
        'bizRules' => '',
        'data' => '0'
    ),
    'riskc_m' => array(
        'type' => CAuthItem::TYPE_ROLE,
        'description' => '风险监控员',
        'display'=>true,
        'children' => array(
            'riskc','posrpt_detail',
        ),
        'bizRules' => '',
        'data' => '1'
    ),
    'maintain_man' => array(
        'type' => CAuthItem::TYPE_ROLE,
        'description' => '终端经理',
        'display'=>true,
        'children' => array(
            'mchtm_r',
            'posm_r',
            'visit_r',
            'visit_visit',
            'visit_check',
            'visit_cancel',
            'visit_change',
        ),
        'bizRules' => '',
        'data' => '1'
    ),
    'visit_checker' => array(
        'type' => CAuthItem::TYPE_ROLE,
        'description' => '回访确认人',
        'display'=>true,
        'children' => array(
            'mchtm_r',
            'posm_r',
            'visit_r',
            'visit_check',
            'visit_cancel',
        ),
        'bizRules' => '',
        'data' => '1'
    ),
    'pre_verify' => array(
        'type' => CAuthItem::TYPE_ROLE,
        'description' => '初审员',
        'display'=>true,
        'children' => array(
            'posrpt_eff',
            'mchtm',
            'posm',
            'workflow',
        ),
        'bizRules' => '',
        'data' => '1'
    ),
    're_verify' => array(
        'type' => CAuthItem::TYPE_ROLE,
        'description' => '复审员',
        'display'=>true,
        'children' => array(
            'posrpt_eff',
            'mchtm',
            'posm',
            'workflow',
        ),
        'bizRules' => '',
        'data' => '1'
    ),
    'fin_verify' => array(
        'type' => CAuthItem::TYPE_ROLE,
        'description' => '终审员',
        'display'=>true,
        'children' => array(
            'posrpt_eff',
            'mchtm',
            'posm',
            'workflow',
        ),
        'bizRules' => '',
        'data' => '1'
    ),
    'custom_m' => array(
        'type' => CAuthItem::TYPE_ROLE,
        'description' => '客户经理',
        'display'=>true,
        'children' => array(
            'posrpt_eff',
            'mchtm',
            'posm',
            'workflow',
            'visit_r',
            'visit_visit',
        ),
        'bizRules' => '',
        'data' => '2'
    ),
    'maintain_nor' => array(
        'type' => CAuthItem::TYPE_ROLE,
        'description' => '终端维护人员',
        'display'=>true,
        'children' => array(
            'posm_r',
            'visit_r',
            'visit_visit',
            'workflow_m',
        ),
        'bizRules' => '',
        'data' => '2'
    ),
		
	'mchtinfo' => array(
			'type' => CAuthItem::TYPE_ROLE,
			'description' => '商户',
			'display'=>true,
			'children' => array(
					'mcht_reconcile',
				),
			'bizRules' => '',
			'data' => '2'
	),
);