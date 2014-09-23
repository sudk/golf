<?php
$t->echo_grid_header();
if (is_array($list['rows']))
{

    $t_success_cnt = 0;
    $t_user_cnt = 0;
    $t_tobe_pay = 0;
    $t_n_amount = 0;
    $t_h_amount = 0;
    $t_t_amount = 0;
    foreach ($list['rows'] as $i => $row)
    {
        $t_success_cnt += $row['success_cnt'];
        $t_user_cnt += $row['user_cnt'];
        $t_tobe_pay += $row['tobe_pay'];
        $t_n_amount += $row['normal_amount'];
        $t_h_amount += $row['holiday_amount'];
        $t_t_amount += $row['total_amount'];
        
        $t->echo_td($row['court_name']);
        $t->echo_td($row['success_cnt']?$row['success_cnt']:"0");  
        $t->echo_td($row['user_cnt']?$row['user_cnt']:"0");  
        $t->echo_td($row['tobe_pay']?$row['tobe_pay']:"0");  
        $t->echo_td(round($row['normal_amount']/100,2)."元");
        $t->echo_td(round($row['holiday_amount']/100,2)."元");
        $t->echo_td(round($row['total_amount']/100,2)."元");
        $t->end_row();
    }
    $t->echo_td("总计：");
    $t->echo_td($t_success_cnt?$t_success_cnt:"0"); 
    $t->echo_td($t_user_cnt?$t_user_cnt:"0"); 
    $t->echo_td($t_tobe_pay?$t_tobe_pay:"0"); 
    $t->echo_td($t_n_amount?round($t_n_amount/100,2)."元":"0元"); 
    $t->echo_td($t_h_amount?round($t_h_amount/100,2)."元":"0元"); 
    $t->echo_td($t_t_amount?round($t_t_amount/100,2)."元":"0元"); 
    
    $t->end_row();
}
$t->echo_grid_floor();
$cnt = $list['total_num'];
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