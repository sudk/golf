<?php
$t->echo_grid_header();
if (is_array($rows))
{
	$j = 1;
        $court_list = Court::getCourtArray();
        $type_list = CourtFacilities::getType();
        //var_dump($rows);
    foreach ($rows as $i => $row)
    {

        $num = ($curpage-1)*$this->pageSize + $j++;
        //$t->begin_row("onclick","getDetail(this,'{$row['id']}','{$row['recordtime']}');");
        
        $link = CHtml::link('详细',"javascript:itemDetail('{$row['id']}')", array());    
        $link .= CHtml::link('编辑',"javascript:itemEdit('{$row['id']}')", array());
        $link .= CHtml::link('删除',"javascript:itemDelete('{$row['id']}','{$row['facilitie_name']}')", array());
               
        
        $facilitie_name = htmlspecialchars($row['facilitie_name']);
        $name_text = "<span title='".$facilitie_name."'>";
        $name_text .= mb_strlen($facilitie_name, 'UTF-8') > 20 ? mb_substr($facilitie_name, 0, 20, 'UTF-8')."..." : $facilitie_name;
        $name_text .= "</span>";
        
        $court_name = $court_list[$row['court_id']];
        $court_text = "<span title='".$court_name."'>";
        $court_text .= mb_strlen($court_name, 'UTF-8') > 20 ? mb_substr($court_name, 0, 20, 'UTF-8')."..." : $court_name;
        $court_text .= "</span>";
        
	$t->echo_td($num); 
        $t->echo_td($name_text); //学校编号
        $t->echo_td($type_list[$row['type']]);
        $t->echo_td($row['phone']);
        $t->echo_td($court_text);
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