<?php
$t->echo_grid_header();
if (is_array($rows))
{
	$j = 1;
    foreach ($rows as $i => $row)
    {
        $t->begin_row("onclick","getDetail(this,'{$row['court_id']}');");
		$num = ($curpage-1)*$this->pageSize + $j++;
        $link = CHtml::link('球场图片',"javascript:itemPic('{$row['court_id']}','{$row['name']}')", array());
        $link .= CHtml::link('球场评价',"javascript:itemComment('{$row['court_id']}','{$row['name']}')", array());
        if(Yii::app()->user->type == Operator::TYPE_SYS)
        {
            $link .= CHtml::link('编辑',"javascript:itemEdit('{$row['court_id']}')", array());
            //$link .= CHtml::link('删除',"javascript:itemDelete('{$row['court_id']}','{$row['name']}')", array());
        }
        
        $name = $row['name'];
        $addr = $row['addr'];
        
        $name_text = "<span title='".$name."'>";
        $name_len = mb_strlen($name, 'UTF-8');
        $name_text .= $name_len>40?mb_substr($name, 0,40 , 'UTF-8')."..." : $name;
        $name_text .= "<span>";
        
        $addr_text = "<span title='".$addr."'>";
        $addr_len = mb_strlen($addr, 'UTF-8');
        $addr_text .= $addr_len>40?mb_substr($addr, 0,40 , 'UTF-8')."..." : $addr;
        $addr_text .= "<span>";
        
        
        $t->echo_td($name_text);
        $t->echo_td($row['model']); //
        $t->echo_td($row['phone']); //
        $t->echo_td($addr_text); //
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