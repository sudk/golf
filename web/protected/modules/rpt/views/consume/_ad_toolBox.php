<form name="_query_form" id="_query_form" action="javascript:itemQuery(0);">
    <li>
        <span class="sift-title">搜索：</span>
        <?php
        if(Yii::app()->user->type == Operator::TYPE_SYS)
        {
            ?>
        <span style="float:left; margin:0 3px; margin-top:-3px;">&nbsp;</span>
            <select name="q[agent_id]">
            
            <option value="">--选择代理商--</option>
            <?php
            $agent = Agent::getAgentList();
            foreach($agent as $key=>$value)
            {
                echo '<option value="',$key,'">',$value,'</option>';
            }
            ?>
           
        </select>
            <?php
        }else
        {
            ?>
            <input type="hidden" name="q[agent_id]" value="<?php echo Yii::app()->user->agent_id;?>"/>
                <?php
        }
        ?>
        <span style="float:left; margin:0 3px; margin-top:-3px;">开球日期从</span>
        <input id="startdate" class="Wdate" type="text" name="q[startdate]" size="14" value="<?php echo date("Y-m-01")?>" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',skin:'whyGreen',errDealMode:0})" >
        <span style="float:left; margin:0 5px; margin-top:-3px;">到</span>
        <input id="enddate" class="Wdate" type="text" name="q[enddate]"  size="14"  value="<?php echo date("Y-m-d")?>" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',skin:'whyGreen',errDealMode:0})" >
        
        <input type="submit" value="" class="search_btn" />
    </li>
</form>
<script type="text/javascript" src="js/JQdate/WdatePicker.js"></script>
<script type="text/javascript">
 jQuery(document).ready(function(){

  }); 

    var itemQuery = function(){
        var length=arguments.length;
        if(length==1){
            <?=$this->adGridId?>.page = arguments[0];
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
<?php echo $this->adGridId; ?>.condition = url;
<?php echo $this->adGridId; ?>.refresh();
    }

</script>