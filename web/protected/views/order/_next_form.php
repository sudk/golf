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
if ($msg['status']) {
    $class = Utils::getMessageType($msg['status']);
    echo "<div class='{$class}' id='msg' style='width:700px;'>{$msg['msg']}</div>";
}
$form = $this->beginWidget('SimpleForm', array(
    'id' => 'form1',
    'enableAjaxSubmit' => false,
    'ajaxUpdateId' => 'form-container',
    'focus' => array($model,'order_id'),
));

echo $form->activeHiddenField($model, 'order_id', array(), '');
echo $form->activeHiddenField($model, 'status', array(), '');
echo $form->activeHiddenField($model, 'next_status', array(), '');
?>
<table class="formList">
    
    
    <tr>
        <td class="maxname">操作内容：</td>
        <td class="mivalue">
            <?php 
            $status_list =  Order::getStatus();
            echo $status_list[$model->next_status];
            ?>
        </td>
       
    </tr>
    <?php
    if($model->status == Order::STATUS_WAIT_REFUND && $model->next_status == Order::STATUS_REFUND)
    {
    ?>
    <tr>
        <td class="maxname">退款金额：</td>
        <td class="mivalue">
            <?php echo $form->activeTextField($model, 'refund',  array('title' => '本项必选', 'class' => 'input_text'), 'required&number'); ?>
        </td>
       
    </tr>
    <?php
    }
    ?>
    <tr>
        <td class="maxname">备注：</td>
        <td class="mivalue">
            <?php echo $form->activeTextArea($model, 'desc',  array('title' => '本项必选', 'rows'=>'2','cols'=>'20','id'=>'desc','style'=>'width:200px;'), 'required'); ?>
        </td>
       
    </tr>
    <tr class="btnBox">
        <td colspan="2">
            <span class="sBtn">
                <a class="left" href="javascript:formSubmit();">保存</a><a class="right"></a>
            </span>
            <span class="sBtn-cancel">
                <a class="left" href="javascript:formReset();">重置</a><a class="right"></a>
            </span>
        </td>
    </tr>
</table>
<?php $this->endWidget();?>
<script type="text/javascript" src="js/JQdate/WdatePicker.js"></script>
<script type="text/javascript">
    
    
    var flag = true;
    function formSubmit() {
        checkMyForm();
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
    }
    function checkMyForm(){
        //checkPassword();
        var desc_obj = jQuery("#desc");
        var ti = "备注的内容不超过128个字符";
        var desc = desc_obj.val();
        if(desc.length > 512){
            desc_obj.addClass('input_error iptxt');
            desc_obj.showTip({flagInfo:ti});
            desc_obj.focus();
            flag = false;
        }else{
            flag = true;
        }
    }
    
    <?php if ($msg['status']) {
        echo "setTimeout(hideMsg,3000);
        ";
        echo "parent.itemQuery();";
    }
    ?>
</script>