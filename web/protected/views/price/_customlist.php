<?php
$t->echo_grid_header();
if (is_array($rows))
{
    $type_list = Img::getType();
    foreach ($rows as $i => $row)
    {
        $t->begin_row("onclick","getDetail(this,'{$row['id']}');");
	$service_text = "";
        
        $price_text = "";
        $policy_id = $row['id'];
        $sql = "policy_id='".$policy_id."' and start_time!=''";
        $random_row = PolicyDetail::model()->find($sql);
        //var_dump($random_row);
        $week = PolicyDetail::getWeek();
        if($random_row)
        {
            $start_time = $random_row['start_time'];//substr($random_row['start_time'],0,2).":".substr($random_row['start_time'],2,2);
            $end_time = $random_row['end_time'];//substr($random_row['end_time'],0,2).":".substr($random_row['end_time'],2,2);
            $price_text = $week[$random_row['day']]."(".$start_time."-".$end_time."):".($random_row['price']/100)."...";;
        }
        
        //服务项目
        $service_text .= $row['is_green']==Policy::IS_YES?"18洞果岭,":"";
        $service_text .= $row['is_caddie']==Policy::IS_YES?"球童,":"";
        $service_text .= $row['is_car']==Policy::IS_YES?"球车,":"";
        $service_text .= $row['is_wardrobe']==Policy::IS_YES?"衣柜,":"";
        $service_text .= $row['is_insurance']==Policy::IS_YES?"小费,":"";

        
        $link = "";
        $link .= CHtml::link('编辑',"javascript:itemEdit('{$row['id']}','1');", array());
        $link .= CHtml::link('删除',"javascript:itemDelete('{$row['id']}');", array());
        
        
        if($service_text!="")
        {
            $service_txt = "<span title='".$service_text."'>";
            $service_txt .= mb_strlen($service_text,'UTF-8')>8? mb_substr($service_text, 0,8,'UTF-8')."...":$service_text;
            $service_txt .= "</span>";
            
            $service_text = $service_txt;
        }
        
        $remark = htmlspecialchars($row['remark']);
        $remark_txt = "<span title='".$remark."'>";
        $remark_txt .= mb_strlen($remark, 'UTF-8')>20 ? mb_substr($remark, 0, 20, 'UTF-8')."..." : $remark;
        $remark_txt .= "</span>";
        
        $cancel_remark = htmlspecialchars($row['cancel_remark']);
        $cancel_remark_txt = "<span title='".$cancel_remark."'>";
        $cancel_remark_txt .= mb_strlen($cancel_remark, 'UTF-8')>20 ? mb_substr($cancel_remark, 0, 20, 'UTF-8')."..." : $cancel_remark;
        $cancel_remark_txt .= "</span>";
        
        
        $t->echo_td($row['start_date']);
        $t->echo_td($row['end_date']); //
        $t->echo_td($service_text);
        $t->echo_td($remark_txt); //
        $t->echo_td($cancel_remark_txt);
        $t->echo_td($price_text); //
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