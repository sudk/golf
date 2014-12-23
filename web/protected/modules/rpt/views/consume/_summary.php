<?php
$t->echo_grid_header();
if (is_array($list['rows']))
{
	$j = 1;
        $trans_type = TransRecord::getTransType();
        $status = TransRecord::getStatus();
        
        $order_type = Order::getOrderType();
        $order_pay = Order::getPayMethod();
        $agent_list = Agent::getAgentList();
    foreach ($list['rows'] as $i => $row)
    {
        $agent = $agent_list[$list['agent_id']]?$agent_list[$list['agent_id']]:"全部";
        $t->echo_td($agent); 
        
        $t->echo_td($order_type[$list['type']]); //学校编号
        $t->echo_td($order_pay[$list['pay_type']]); //学校编号
        $t->echo_td($trans_type[$list['trans_type']]); //学校编号
        $t->echo_td(-(intval($row['sum_amount'])/100)."元");
        
        $t->echo_td($row['cnt']);
        
        $t->end_row();
    }
}
$t->echo_grid_floor();

?>
<div class="alternate-rule" style="display: none;"></div>