<?php
$t->echo_grid_header();
if (is_array($rows))
{
	$j = 1;
        //var_dump($month);
    foreach ($rows as $i => $row)
    {
        //通过球场ID，代理商ID，type＝0，1，2来确定
        $court_id = $row['court_id'];
        $agent_id = Yii::app()->user->agent_id;
        $normal_row = Policy::model()->find("court_id='".$court_id."' and agent_id='".$agent_id."' and type='0' and date_format(start_date,'%Y-%m')='".$month."'");
        $f_row = Policy::model()->find("court_id='".$court_id."' and agent_id='".$agent_id."' and type='1' and date_format(start_date,'%Y-%m')='".$month."'");
        $holiday_row = Policy::model()->find("court_id='".$court_id."' and agent_id='".$agent_id."' and type='2' and date_format(start_date,'%Y-%m')='".$month."'");
        //var_dump($court_id);var_dump($agent_id);var_dump($month);
        $service_text = "";
        $price_text = "";
        $mon_price = "";
        $tun_price = "";
        $wed_price = "";
        $thu_price = "";
        $fri_price = "";
        $sat_price = "";
        $sun_price = "";
        $status_text = "";
        
        $holiday_price = "";
        $valid_time = "";
        
        if($normal_row)
        {
            //服务项目
            $service_text .= $normal_row['is_green']==Policy::IS_YES?"18洞果岭,":"";
            $service_text .= $normal_row['is_caddie']==Policy::IS_YES?"球童,":"";
            $service_text .= $normal_row['is_car']==Policy::IS_YES?"球车,":"";
            $service_text .= $normal_row['is_wardrobe']==Policy::IS_YES?"衣柜,":"";
            $service_text .= $normal_row['is_insurance']==Policy::IS_YES?"小费,":"";
            
            //获取周的每天的默认报价
            $policy_id = $normal_row['id'];
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
            $status_text = Policy::GetStatus($normal_row['status']);
            $valid_time = $normal_row['start_date']."至".$normal_row['end_date'];
            
        }
        //有节假日内容的
        if($holiday_row)
        {
            $holiday_price .= "[";
            $start_date = $holiday_row['start_date'];
            $end_date = $holiday_row['end_date'];
            $start_date = substr($start_date, 5);
            $end_date = substr($end_date,5);
            if($start_date == $end_date)
            {
                $holiday_price .= $start_date;
            }else{
                $holiday_price .= $start_date."至".$end_date;
            }
            $policy_id = $holiday_row['id'];
            
            $sql = "policy_id='".$policy_id."' and start_time='' and end_time=''";
            
            $h_row = PolicyDetail::model()->find($sql);
            if($h_row)
            {
                $holiday_price .= ":".($h_row['price']/100);
            }else
            {
                $holiday_price .= "未报价";
            }
            $holiday_price .= "]<br>";
        }
        
        $link = "";
        if($normal_row)
        {
            $link .= CHtml::link('编辑',"javascript:itemEdit('{$normal_row['id']}','0')", array());
            $link .= "<br/>";
            $link.= CHtml::link('报价列表',"javascript:itemDetail('{$row['court_id']}','{$month}');", array());
        }  else {
            $link .= CHtml::link('新增',"javascript:itemNew('{$row['court_id']}','0');", array());
            $link .= "<br/>";
            $link.= CHtml::link('报价列表',"javascript:itemDetail('{$row['court_id']}','');", array());
        }
        
        $f_link = "";
        if($f_row)
        {
            $f_link .= CHtml::link('编辑',"javascript:itemEdit('{$f_row['id']}','1')", array());
            $f_link .= "<br/>";
            $f_link .= CHtml::link('报价列表',"javascript:itemFoverDetail('{$row['court_id']}','{$month}');", array());
        }else{
            $f_link .= CHtml::link('新增',"javascript:itemNew('{$row['court_id']}','1')", array());
            $f_link .= "<br/>";
            $f_link .= CHtml::link('报价列表',"javascript:itemFoverDetail('{$row['court_id']}','');", array());
        }
        
        //$t->begin_row("onclick","getDetail(this,'{$row['court_id']}');");
	
        $h_link = "";
        if($holiday_row)
        {
            $h_link .= CHtml::link('编辑',"javascript:itemEdit('{$holiday_row['id']}','2')", array());
            $h_link .= "<br/>";
            $h_link .=CHtml::link('报价列表',"javascript:itemCustomDetail('{$row['court_id']}','{$month}');", array());
        
        }else{
            $h_link .= CHtml::link('新增',"javascript:itemNew('{$row['court_id']}','2')", array());
            $h_link .= "<br/>";
            $h_link .=CHtml::link('报价列表',"javascript:itemCustomDetail('{$row['court_id']}','');", array());
        
        }
        
        $court_name = htmlspecialchars($row['name']);
        $court_text = "<span title='".$court_name."'>";
        $court_text .= mb_strlen($court_name, 'UTF-8')>20 ? mb_substr($court_name, 0, 20, 'UTF-8')."..." : $court_name;
        $court_text .= "</span>";
        
        
        if($service_text!="")
        {
            $service_txt = "<span title='".$service_text."'>";
            $service_txt .= mb_strlen($service_text,'UTF-8')>8? mb_substr($service_text, 0,8,'UTF-8')."...":$service_text;
            $service_txt .= "</span>";
            
            $service_text = $service_txt;
        }
        
        $t->echo_td($court_text);
        $t->echo_td($service_text); //
        $t->echo_td($price_text); //      
        $t->echo_td($status_text); //
        $t->echo_td($link);
        $t->echo_td($f_link);
        $t->echo_td($holiday_price.$h_link); //
        $t->echo_td($valid_time);
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