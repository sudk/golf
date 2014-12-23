<?php
$t->echo_grid_header();
if (is_array($list['rows']))
{
    foreach ($list['rows'] as $i => $row)
    {
        
        $t->echo_td($row['success_cnt']?$row['success_cnt']:"0");  
        $t->echo_td($row['user_cnt']?$row['user_cnt']:"0");  
        $t->echo_td($row['tobe_pay']?$row['tobe_pay']:"0");  
        $t->echo_td(round($row['normal_amount']/100,2)."元");
        $t->echo_td(round($row['holiday_amount']/100,2)."元");
        $t->echo_td(round($row['total_amount']/100,2)."元");
        
        
        $t->end_row();
    }
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