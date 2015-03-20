<form name="_query_form" id="_query_form" action="javascript:itemQuery(0);">
    <li>
        <span class="sift-title">订单状态：</span>
        <?php
        echo CHtml::link('不限', '', array('class' => 'air', 'name' => 'qstatus[]', 'qvalue' =>''));
        $status_list = Order::getStatus();
            if(@count($status_list) > 0 )
            {
                foreach($status_list as $key=>$value)
                {
                    echo CHtml::link($value, '', array('qvalue'=> $key, 'name' => 'qstatus[]'));
                }
            }
        
        ?>
    </li>
    <li>
        <span class="sift-title">搜索：</span>
        <?php
        if(Yii::app()->user->type == Operator::TYPE_SYS){
            ?>
            <span style="float:left; margin:0 3px; margin-top:-3px;">代理商</span>
        <select name="q[agent_id]">
            <option value="">--选择--</option>
            <?php
            $agent_list = Agent::getAgentList();
            if(@count($agent_list) > 0 )
            {
                foreach($agent_list as $key=>$value)
                {
                    echo '<option value="'.$key.'">'.$value.'</option>';
                }
            }
            ?>
        </select>    
            <?php
        }
        ?>
        <span style="float:left; margin:0 3px; margin-top:-3px;">订单类型</span>
        <select name="q[order_type]">
            <option value="">--选择--</option>
            <?php
            $type_list = Order::getOrderType();
            if(@count($type_list) > 0 )
            {
                foreach($type_list as $key=>$value)
                {
                    echo '<option value="'.$key.'">'.$value.'</option>';
                }
            }
            ?>
        </select>
        
        <span style="float:left; margin:0 5px; margin-top:-3px;">&nbsp;</span>
        
        <input name="q[order_id]" type="text" class="grayTips" value="订单编号"/>
        <input type="hidden" name="q[status]" id="qstatus" value=''/>
        <input type="submit" value="" class="search_btn"/>
        <?php
        if(Yii::app()->user->checkAccess("order/export"))
        {
        ?>
        <span>
            <a href="javascript:void(0);" style="margin-left: 10px;margin-top: -3px;" onclick="javascript:exportOrder();">导出订单</a>
        </span>
        <?php
        }
        ?>
    </li>
    
</form>
<script type="text/javascript" src="js/JQdate/WdatePicker.js"></script>
<script type="text/javascript">
    jQuery(document).ready(function(){
           //当前月份
             jQuery($("a[name='qstatus[]']")).click(function () {
                 var qvalue = jQuery(this).attr("qvalue");
                 if (qvalue != '') {
                     jQuery("#qstatus").attr("value", qvalue);
                 } else {
                     jQuery("#qstatus").attr("value", "");
                 }
                 jQuery($("a[name='qstatus[]']")).removeClass('air');
                 jQuery(this).addClass('air');
                 itemQuery(0);
             });  
  }); 

    var itemQuery = function(){
        var length=arguments.length;
        if(length==1){
            <?=$this->gridId?>.page = arguments[0];
        }
        var objs = document.getElementById("_query_form").elements;
        var i = 0;
        var cnt = objs.length;
        var obj;
        var url = '';
        for (i = 0; i < cnt; i++) {
            obj = objs.item(i);
            url += '&' + obj.name + '=' + obj.value;
        }
<?php echo $this->gridId; ?>.condition = url;
<?php echo $this->gridId; ?>.refresh();
    }
    
    
    var exportOrder = function()
    {
        var objs = document.getElementById("_query_form").elements;
        var i = 0;
        var cnt = objs.length;
        var obj;
        var url = 'index.php?r=order/export';
        for (i = 0; i < cnt; i++) {
            obj = objs.item(i);
            url += '&' + obj.name + '=' + obj.value;
        }
        //window.location.target = '_blank';
        //window.location.href = url;
        window.open(url,"_blank");
        
    }

</script>