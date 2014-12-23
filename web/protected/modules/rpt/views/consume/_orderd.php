<?php
$t->echo_grid_header();
if (is_array($list['rows']))
{
    $court_list = Court::getCourtArray();
    $total = 0;
    $user_cnt = 0;
    foreach ($list['rows'] as $i => $row)
    {
        $total += $row['total'];
        $user_cnt += $row['pay_user'];
        $t->echo_td($row['court_name']);
        $t->echo_td($row['success']?$row['success']:"0"); 
        $t->echo_td($row['pay']?$row['pay']:"0");
        $t->echo_td($row['cancel']?$row['cancel']:"0");
        $t->echo_td($row['tobe_confirm']?$row['tobe_confirm']:"0");
        $t->echo_td($row['tobe_pay']?$row['tobe_pay']:"0");
        $t->echo_td($row['tobe_cancel']?$row['tobe_cancel']:"0");
        $t->echo_td($row['canceling']?$row['canceling']:"0");
        $t->echo_td($row['total']?$row['total']:"0");
        $t->echo_td($row['normal']?$row['normal']:"0");
        $t->echo_td($row['holiday']?$row['holiday']:"0");
        $t->echo_td($row['pay_user']?$row['pay_user']:"0");               
        $t->end_row();
    }
    
    $t->echo_td("总计：");
    $t->echo_td(""); 
    $t->echo_td(""); 
    $t->echo_td(""); 
    $t->echo_td(""); 
    $t->echo_td(""); 
    $t->echo_td(""); 
    $t->echo_td(""); 
     
    $t->echo_td($total?$total:"0");
    $t->echo_td(""); 
    $t->echo_td("");   
    $t->echo_td($user_cnt?$user_cnt:"0");

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