<?php
$t->echo_grid_header();
if (is_array($rows))
{
	$j = 1;
    foreach ($rows as $i => $row)
    {
        //$t->begin_row("onclick","getDetail(this,'{$row['id']}');");
	
        $link = CHtml::link('编辑',"javascript:itemEdit('{$row['id']}')", array());
        
        $link .= CHtml::link('删除',"javascript:itemDelete('{$row['id']}','{$row['agent_name']}')", array());
	
        $agent_name = htmlspecialchars($row['agent_name']);
        $agent_text = "<span title='".$agent_name."'>";
        $agent_text .= mb_strlen($agent_name, 'UTF-8') > 20 ? mb_substr($agent_name,0,20,'UTF-8')."..." : $agent_name;
        $agent_text .= "</span>";
        
        $t->echo_td($agent_text); //
        $t->echo_td($row['contactor']);
        $t->echo_td($row['phone']);
        $t->echo_td(floatval(intval($row['extra'])/100)."元");
        $t->echo_td(Agent::GetStatus($row['status']));
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