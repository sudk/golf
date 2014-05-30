<?php
$t->echo_grid_header();
if (is_array($rows)) {
    $j = 1;
    $stutus_list = Flea::GetStatus();
    $city_list = CityCode::getCity();
    foreach ($rows as $i => $row) {

        $num = ($curpage - 1) * $this->pageSize + $j++;

        $link = CHtml::link('详细',"javascript:itemEdit('{$row['id']}')", array());
        $id_pre = substr($row['id'],0,1);
        if(Yii::app()->user->type == Operator::TYPE_AGENT && Yii::app()->user->agent_id == '1' && $id_pre == 'O')
        {
            $l_title = htmlspecialchars($row['title']);
            $link .= CHtml::link('删除',"javascript:itemDelete('{$row['id']}','{$l_title}')", array());
        }
        
        $price = intval($row['price']);
        $price = $price==0?0:floatval($price/100);
        $price .= "元";
        
        $title = htmlspecialchars($row['title']);
        $title_text = "<span title='".$title."'>";
        $title_text .= mb_strlen($title, 'UTF-8') > 20 ? mb_substr($title, 0, 20, 'UTF-8')."..." : $title;
        $title_text .= "</span>";
        
        $t->echo_td($num);
        $t->echo_td($title_text); //学校编号
        $t->echo_td($price);
        
        $t->echo_td($row['city']?$city_list[$row['city']]:"");
        $t->echo_td($row['record_time']);
        $t->echo_td($stutus_list[$row['status']]);
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