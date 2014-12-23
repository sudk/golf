<?php
$t->echo_grid_header();
if (is_array($rows))
{
	$j = 1;
        $tee_array = ScoreDetail::getTee();
    foreach ($rows as $i => $row)
    {
        //$t->begin_row("onclick","getDetail(this,'{$row['order_id']}');");
        $num = ($curpage-1)*$this->pageSize + $j++;
        
        $t->echo_td($num);
        $t->echo_td($row['hole_no']); //
        $t->echo_td($tee_array[$row['tee']]); //
        $t->echo_td($row['standard_bar']);
        $t->echo_td($row['lang_bar']); //
        $t->echo_td($row['push_bar']); //
        $t->echo_td($row['record_time']);
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