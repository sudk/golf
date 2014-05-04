<?php
$t->echo_grid_header();
if (is_array($rows))
{
	$j = 1;
    foreach ($rows as $i => $row)
    {

		$num = ($curpage-1)*$this->pageSize + $j++;
        $t->begin_row("onclick","getDetail(this,'{$row['msgid']}');");
        
        
        $title = $row['title'];
        $title_len = mb_strlen($title,'UTF-8');
        $title_txt = $title_len > 30 ? mb_substr($title, 0, 30,'UTF-8')."...": $title;
        $title_txt = '<span title="'.$title_txt.'">'.$title_txt.'</span>';
        
		$t->echo_td($num); 
        $t->echo_td($title_txt); //学校编号
        $t->echo_td(MsgListener::GetType($row['type']));
        $t->echo_td($row['sender']);
        $t->echo_td($row['record_time']);
        $t->echo_td(CHtml::link('详细',"javascript:itemDetail('{$row['msgid']}')", array()));
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