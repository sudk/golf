<?php
$t->echo_grid_header();
if (is_array($list['rows']))
{
    $court_list = Court::getCourtArray();
    $total = 0;
    $t_user_cnt = 0;
    $t_h_cnt = 0;
    $t_n_cnt = 0;
    $t_court_cnt = 0;
    $t_amount = 0;
    foreach ($list['rows'] as $i => $row)
    {
        $total += $row['cnt'];
        $t_user_cnt += $row['user_cnt'];
        $t_h_cnt += $row['holiday_cnt'];
        $t_n_cnt += $row['normal_cnt'];
        $t_court_cnt += $row['court_cnt'];
        $t_amount += $row['amount'];
    
        $t->echo_td($row['contact']);
        $t->echo_td($row['cnt']?$row['cnt']:"0"); 
        $t->echo_td($row['normal_cnt']?$row['normal_cnt']:"0");
        $t->echo_td($row['holiday_cnt']?$row['holiday_cnt']:"0");
        $t->echo_td($row['user_cnt']?$row['user_cnt']:"0");
        $t->echo_td($row['court_cnt']?$row['court_cnt']:"0");
        $t->echo_td($row['amount']?round($row['amount']/100,2)."元":"0元");               
        $t->end_row();
    }
    
    $t->echo_td("总计：");
    $t->echo_td($total?$total:"0"); 
    $t->echo_td($t_n_cnt?$t_n_cnt:"0"); 
    $t->echo_td($t_h_cnt?$t_h_cnt:"0"); 
    $t->echo_td($t_user_cnt?$t_user_cnt:"0"); 
    $t->echo_td($t_court_cnt?$t_court_cnt:"0"); 
    $t->echo_td($t_amount?round($t_amount/100,2)."元":"0元"); 

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