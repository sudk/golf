<?php
$t->echo_grid_header();
if (is_array($rows))
{
	$j = 1;
        $type_list = Operator::GetType();
    foreach ($rows as $i => $row)
    {
        $t->begin_row("onclick","getDetail(this,'{$row['id']}');");
		$num = ($curpage-1)*$this->pageSize + $j++;
        $link = CHtml::link('编辑',"javascript:itemEdit('{$row['id']}')", array());
        $link .= CHtml::link('权限',"javascript:authEdit('{$row['id']}','{$row['name']}')", array());
        $link .= CHtml::link('删除',"javascript:itemDelete('{$row['id']}','{$row['name']}')", array());
		$t->echo_td($num);
                $t->echo_td($type_list[$row['type']]); //
        $t->echo_td($row['id']);
        
        $t->echo_td($row['name']); //
        $t->echo_td($row['tel']);
        $t->echo_td($row['jobtitle']);
        $t->echo_td($row['abbreviation']);
        $t->echo_td(Operator::GetStatus($row['status']));
        //$t->echo_td($row['record_time']);
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