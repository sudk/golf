<?php
$t->echo_grid_header();
if (is_array($rows))
{
	$j = 1;
        $is_show = Score::getIsShow();
        $court_list = Court::getCourtArray();
    foreach ($rows as $i => $row)
    {
        //$t->begin_row("onclick","getDetail(this,'{$row['id']}');");
        $num = ($curpage-1)*$this->pageSize + $j++;
        $link = "";
        
        $link .= CHtml::link('详情',"javascript:itemDetail('{$row['id']}')", array());
        $t->echo_td($num);
        $t->echo_td($row['user_phone']); //
        $t->echo_td($court_list[$row['court_id']]);
        $t->echo_td($row['holes']);
        $t->echo_td($row['fee_time']);
        $t->echo_td($is_show[$row['is_show']]);
        $t->echo_td($row['team_menbers']);
        $t->echo_td($row['handicap']?$row['handicap']:"--");
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