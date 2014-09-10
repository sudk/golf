<form name="_query_form" id="_query_form" action="javascript:itemQuery();">
    <li>
        <span class="sift-title">搜索：</span>
       
        <span style="float:left; margin:0 3px; margin-top:-3px;">球场名称</span>
        <select name="q[court_id]">
            <option value="">--选择--</option>
            <?php
            $court_list = Court::getCourtArray();
            if(@count($court_list) > 0 )
            {
                foreach($court_list as $key=>$value)
                {
                    echo '<option value="'.$key.'">'.$value.'</option>';
                }
            }
            ?>
        </select>
      
       <span style="float:left; margin:0 3px; margin-top:-3px;">查询日期</span>
        <input id="begintime"  type="text" name="q[begin_date]" size="14" class="Wdate" value="" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',skin:'whyGreen',errDealMode:0})" >
        <span style="float:left; margin:0 5px; margin-top:-3px;">到</span>
        <input id="endtime"  type="text" name="q[end_date]"  size="14" class="Wdate"  value="" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',skin:'whyGreen',errDealMode:0})" >
       
        <input type="submit" value="" class="search_btn"/>
    </li>
    
</form>
<script type="text/javascript" src="js/JQdate/WdatePicker.js"></script>
<script type="text/javascript">
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