<?php
$t->echo_grid_header();
if (is_array($rows))
{
	$j = 1;
    foreach ($rows as $i => $row)
    {
        $t->begin_row("onclick","getDetail(this,'{$row['court_id']}');");
		$num = ($curpage-1)*$this->pageSize + $j++;
        $link = CHtml::link('编辑',"javascript:itemEdit('{$row['court_id']}')", array());
       
        $link .= CHtml::link('删除',"javascript:itemDelete('{$row['court_id']}','{$row['name']}')", array());
        $t->echo_td($num);
        $t->echo_td($row['name']);
        $t->echo_td($row['model']); //
        $t->echo_td($row['phone']); //
        $t->echo_td($row['addr']); //
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