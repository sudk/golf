<?php
$t->echo_grid_header();
if (is_array($list['rows']))
{
    foreach ($list['rows'] as $i => $row)
    {
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
        //$t->echo_td($row['']);
        
        
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