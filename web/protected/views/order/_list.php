<?php
$t->echo_grid_header();
if (is_array($rows))
{
	$j = 1;
    $type_list = Order::getOrderType();
    $status_list = Order::getStatus();
    $pay_type = Order::getPayType();
    foreach ($rows as $i => $row)
    {
        $t->begin_row("onclick","getDetail(this,'{$row['order_id']}');");
        $link = "";
        $status = $row['status'];
        $link .= CHtml::link('详细',"javascript:itemLog('{$row['order_id']}')", array());
        if(Yii::app()->user->type == Operator::TYPE_AGENT)
        {
            $link .= CHtml::link('编辑',"javascript:itemEdit('{$row['order_id']}')", array());

            if($status == Order::STATUS_TOBE_CANCEL)
            {
                $link .= CHtml::link('删除',"javascript:itemDelete('{$row['order_id']}');", array());
            }
            
            $next_opt = Order::getNextStatus($status, $row['pay_type']);
            //var_dump($next_opt);var_dump($status);var_dump($row['pay_type']);
            if(@count($next_opt)>0)
            {
                foreach($next_opt as $opt)
                {
                    $link .= CHtml::link($opt['desc'],"javascript:itemNextStatus('{$row['order_id']}','{$opt['now_status']}','{$opt['status']}','{$row['pay_type']}','{$opt['desc']}');", array());
                }
            }
        }
        $status_text = "";
        $status_text = '<span style="color:black;font-weight:bolder;">'.$status_list[$status].'</span>';
        
        
        $amount = intval($row['amount']);
        $amount = $amount!=0? floatval($amount/100):0;
        $amount .= "元";
        
        $t->echo_td($row['order_id']);
        $t->echo_td($type_list[$row['type']]); //
        $t->echo_td($row['contact']); //
        $t->echo_td($row['relation_name']); //
        $t->echo_td($amount); //
        $t->echo_td($pay_type[$row['pay_type']]); //
        $t->echo_td($status_text); //
        $t->echo_td($row['record_time']); //
        $t->echo_td($link);
        $t->end_row();
    }
}
$t->echo_grid_floor();

$pager = new CPagination($cnt);
$pager->pageSize = $this->pageSize;
$pager->itemCount = $cnt;
?>
<div class="page">
    <div class="floatL">共 <strong><?php echo $cnt; ?></strong>&nbsp;条</div>
    <div class="pageNumber">
<?php $this->widget('AjaxLinkPager', array('bindTable' => $t, 'pages' => $pager)); ?>
    </div>
</div>
<div class="alternate-rule" style="display: none;"></div>