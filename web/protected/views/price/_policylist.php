<?php
$t->echo_grid_header();
if (is_array($rows))
{
    $type_list = Img::getType();
    foreach ($rows as $i => $row)
    {
        //$t->begin_row("onclick","getDetail(this,'{$row['court_id']}');");
	$service_text = "";
        $price_text = "";
        $mon_price = "";
        $tun_price = "";
        $wed_price = "";
        $thu_price = "";
        $fri_price = "";
        $sat_price = "";
        $sun_price = "";
        
        //服务项目
        $service_text .= $row['is_green']==Policy::IS_YES?"18洞果岭,":"";
        $service_text .= $row['is_caddie']==Policy::IS_YES?"球童,":"";
        $service_text .= $row['is_car']==Policy::IS_YES?"球车,":"";
        $service_text .= $row['is_wardrobe']==Policy::IS_YES?"衣柜,":"";
        $service_text .= $row['is_insurance']==Policy::IS_YES?"小费,":"";

        //获取周的每天的默认报价
        $policy_id = $row['id'];
        $sql = "policy_id='".$policy_id."' and start_time='' and end_time=''";

        $mon_sql = $sql." and day='1'";
        $mon_row = PolicyDetail::model()->find($mon_sql);
        if($mon_row)
        {
            $mon_price = $mon_row['price']/100;
        }

        $tun_sql = $sql." and day='2'";
        $tun_row = PolicyDetail::model()->find($tun_sql);
        if($tun_row)
        {
            $tun_price = $tun_row['price']/100;
        }
        $wed_sql = $sql." and day='3'";
        $wed_row = PolicyDetail::model()->find($wed_sql);
        if($wed_row)
        {
            $wed_price = $wed_row['price']/100;
        }
        $thu_sql = $sql." and day='4'";
        $thu_row = PolicyDetail::model()->find($thu_sql);
        if($thu_row)
        {
            $thu_price = $thu_row['price']/100;
        }
        $fri_sql = $sql." and day='5'";
        $fri_row = PolicyDetail::model()->find($fri_sql);
        if($fri_row)
        {
            $fri_price = $fri_row['price']/100;
        }
        $sat_sql = $sql." and day='6'";
        $sat_row = PolicyDetail::model()->find($sat_sql);
        if($sat_row)
        {
            $sat_price = $sat_row['price']/100;
        }
        $sun_sql = $sql." and day='0'";
        $sun_row = PolicyDetail::model()->find($sun_sql);
        if($sun_row)
        {
            $sun_price = $sun_row['price']/100;
        }
        $price_text = $mon_price.",".$tun_price.",".$wed_price.",".$thu_price.",".$fri_price.",".$sat_price.",".$sun_price;
        
        $link = "";
        $link .= CHtml::link('编辑',"javascript:itemEdit('{$row['id']}','0');", array());
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