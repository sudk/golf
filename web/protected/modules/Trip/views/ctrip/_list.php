<?php
$t->echo_grid_header();
if (is_array($rows))
{
    foreach ($rows as $i => $row)
    {
        $t->begin_row("onclick","getDetail(this,'{$row['order_id']}');");
        $link = "";
        
        $t->echo_td($row['id']);
        $t->echo_td($row['area']);
        $t->echo_td($row['court_name']);
        $t->echo_td($row['contact']);
        $t->echo_td($row['phone']);
        $t->echo_td($row['m_count']);
        //$t->echo_td($row['status']);
        $t->echo_td($row['record_time']);
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