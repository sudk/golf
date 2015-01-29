<style type="text/css">
    .city_list {
        text-align:left;
        list-style-type:none;
        width:260px;
    }
    .city_list li {
        display:inline-block;
        list-style-type:none;
        padding: 3px;
    }
</style>
<?php
if (isset($msg['status'])) {
    $class = Utils::getMessageType($msg['status']);
    echo "<div class='{$class}' id='msg' style='width:700px;'>{$msg['msg']}</div>";
}
$form = $this->beginWidget('SimpleForm', array(
    'id' => 'form1',
    'enableAjaxSubmit' => false,
    'ajaxUpdateId' => 'form-container',
    'focus' => array($model,'id'),
));

echo $form->activeHiddenField($model, 'order_id', array(), '');
?>
<table class="formList">
    <tr>
        <td class="maxname">订单联系人：</td>
        <td class="mivalue">
            <?php echo $form->activeTextField($model, 'contact',  array('title' => '本项必选', 'class' => 'input_text'), 'required'); ?>
        </td>
    </tr>
    <tr>
        <td class="maxname">联系人电话：</td>
        <td class="mivalue">
            <?php echo $form->activeTextField($model, 'phone', array('title' => '本项必选', 'class' => 'input_text'), 'required&phone'); ?>
        </td>
    </tr>
    <tr>
        <td class="maxname">打球时间：</td>
        <td class="mivalue">
            <?php echo $form->activeTextField($model, 'tee_time',  array('title' => '本项必选',  'class' => 'Wdate input_text',"onfocus"=>"WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen',errDealMode:0})"), 'required'); ?>
        </td>
    </tr>
    <tr>
        <td class="maxname">打球人数：</td>
        <td class="mivalue">
            <?php 
            echo $form->activeTextField($model, 'count',  array('title' => '本项必选', 'class' => 'input_text','id'=>'user_cnt'), 'required');
            echo $form->activeHiddenField($model, 'unitprice',  array('title' => '本项必选', 'class' => 'input_text','id'=>'unitprice'), 'required');
            ?>
        </td>
    </tr>
    <tr>
        <td class="maxname">应付金额：</td>
        <td class="mivalue">
            <?php echo $form->activeTextField($model, 'amount',  array('title' => '本项必选', 'class' => 'input_text','id'=>'amount'), 'required'); ?>
        </td>
    </tr>
    <?php $status_ar=explode("`",$_GET['status_str']); if($status_ar[1]==Order::STATUS_TOBE_PAID):?>
        <?php
        $last_pay_time=date("Y-m-d H:i:s",strtotime("+1 hours"));
        if(strtotime($model->tee_time)>strtotime("+26 hours")){
            $last_pay_time=date("Y-m-d H:i:s",strtotime($model->tee_time)-60*60*24);
        }
        $model->last_pay_time=$last_pay_time;
        ?>
        <tr>
            <td class="maxname">最晚付款时间：</td>
            <td class="mivalue">
                <?php echo $form->activeTextField($model, 'last_pay_time',  array('title' => '本项必选', 'class' => 'Wdate input_text',"onfocus"=>"WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen',errDealMode:0})"), ''); ?>
            </td>
        </tr>
    <?php endif;?>
    <?php if(!isset($msg['status'])):?>
    <tr class="btnBox">
        <td colspan="2">
            <span class="sBtn">
                <a class="left" href="javascript:formSubmit();">确认订单</a><a class="right"></a>
            </span>
            <span class="sBtn-cancel">
                <a class="left" href="javascript:formReset();">重置</a><a class="right"></a>
            </span>
        </td>
    </tr>
    <?php endif;?>
</table>
<?php $this->endWidget();?>
<script type="text/javascript" src="js/JQdate/WdatePicker.js"></script>
<script type="text/javascript">
    jQuery("#user_cnt").blur(function(){
       var v = jQuery("#unitprice").val();
       var cnt = jQuery(this).val();
       jQuery("#amount").val(v*cnt);
    }).keypress(function(){
        var v = jQuery("#unitprice").val();
       var cnt = jQuery(this).val();
       jQuery("#amount").val(v*cnt);
    }).keyup(function(){
        var v = jQuery("#unitprice").val();
       var cnt = jQuery(this).val();
       jQuery("#amount").val(v*cnt);
    }).keydown(function(){
        var v = jQuery("#unitprice").val();
       var cnt = jQuery(this).val();
       jQuery("#amount").val(v*cnt);
    });
    
    var flag = true;
    function formSubmit() {
        //checkMyForm();
        if (flag)
            $("form:first").submit();
        else
            flag=true;
    }
    function formReset() {
        document.getElementById("form1").reset();
    }
    function hideMsg() {
        $("#msg").hide("slow");
        parent.$("#windown-close").click();
    }
    function checkMyForm(){
        //checkPassword();
    }
    
    <?php if ($msg['status']) {
        echo "setTimeout(hideMsg,3000);
        ";
        echo "parent.itemQuery();";
    }
    ?>
</script>