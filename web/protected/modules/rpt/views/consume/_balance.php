<?php
$t->echo_grid_header();
if (is_array($list['rows']))
{
    foreach ($list['rows'] as $i => $row)
    {
        $t->echo_td($row['total_cnt']?$row['total_cnt']:"0"); 
        $t->echo_td($row['success_cnt']?$row['success_cnt']:"0");
        $t->echo_td($row['cancel_cnt']?$row['cancel_cnt']:"0");
        $t->echo_td($row['success_ucnt']?$row['success_ucnt']:"0");
        $t->echo_td($row['cancel_ucnt']?$row['cancel_ucnt']:"0"); 
        $t->echo_td($row['balance']?round($row['balance']/100,2):"0");
        $t->echo_td($row['cancel_amount']?round($row['cancel_amount']/100,2):"0");
        $t->echo_td($row['total_amount']?round($row['total_amount']/100,2):"0");
        $t->echo_td($row['fee']?round($row['fee']/100,2):"0"); 
        $t->echo_td($row['user_cnt']?$row['user_cnt']:"0");
        $t->echo_td($row['commission']?round($row['commission']/100,2):"0");
        
        $t->echo_td($row['final_amount']?round($row['final_amount']/100,2):"0");
        
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