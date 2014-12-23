<?php
$t->echo_grid_header();
if (is_array($rows))
{
	$j = 1;
        $status_list = Adv::getStatus();
        $type_list = Adv::getType();
        //var_dump($rows);
    foreach ($rows as $i => $row)
    {

        $num = ($curpage-1)*$this->pageSize + $j++;
        //$t->begin_row("onclick","getDetail(this,'{$row['id']}','{$row['recordtime']}');");

        $link='';
        $link .= CHtml::link('编辑',"javascript:itemEdit('{$row['id']}')", array());
        $link .= CHtml::link('删除',"javascript:itemDelete('{$row['id']}')", array());
            
        $img = "无";
        $img_info = Img::model()->find("relation_id='{$row['id']}' and type='".Img::TYPE_ADV."'");
        if($img_info)
        {
            $url = "index.php?r=court/loadpic&name=".$img_info['img_url'];
            $img = '<img src="'.$url.'" style="width:50px;height:50px;"/>';
        }
        
        
        $link_url = htmlspecialchars($row['link_url']);
        
        $link_url_txt = $link_url;//"<span title='".$link_url."'>[查看]</span>";
        
	$t->echo_td($num); 
        $t->echo_td($row['order']); //学校编号
        $t->echo_td($type_list[$row['type']]);
        $t->echo_td($img);
        $t->echo_td($row['start_time']."至".$row['end_time']);
        $t->echo_td($link_url_txt);
        $t->echo_td($status_list[$row['status']]);
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