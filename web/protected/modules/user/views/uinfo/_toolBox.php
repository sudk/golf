<form name="_query_form" id="_query_form" action="javascript:itemQuery(0);">
    <li>
        <span class="sift-title">搜索：</span>
<!--        <select name="q_by">
            <option value="user_name">姓名</option>
            <option value="card_no">会员卡号</option>
        </select>
        <input name="q_value" type="text" class="grayTips" />-->
        <input type="hidden" name="q[status]" id="qstatus" value=''/>       
        <span style="float:left; margin:0 5px; margin-top:-3px;">姓名</span>
        <input name="q[user_name]" type="text" class="grayTips" maxlength="32"/>
        <span style="float:left; margin:0 5px; margin-top:-3px;">会员卡号</span>
        <input name="q[card_no]" type="text" class="grayTips" maxlength="32"/>
        <span style="float:left; margin:0 5px; margin-top:-3px;">电话</span>
        <input name="q[phone]" type="text" class="grayTips" maxlength="12"/>
        <input type="submit" value="" class="search_btn"/>
    </li>
    <li>
        <span class="sift-title">用户状态：</span>
        <?php
        echo CHtml::link('不限', '', array('class' => 'air', 'name' => 'qstatus[]', 'qvalue' =>''));
        echo CHtml::link( "正常", '', array('qvalue'=> "0", 'name' => 'qstatus[]'));
        echo CHtml::link( "冻结", '', array('qvalue'=> "-1", 'name' => 'qstatus[]'));
        
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

</script>