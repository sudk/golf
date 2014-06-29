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
    'focus' => array($model,'id'),
));
?>
<table class="formList">
    <tr>
        <td class="maxname"><?php 
        echo $model->desc;
        echo $form->activeHiddenField($model,'id',array());
        ?>：</td>
        <td class="mivalue"><?php 
        if($model->id == SysSetting::VIP_RIGHT_DESC){
            
            echo $form->activeTextArea($model,'value',array('title' => '本项必填','rows'=>'3','cols'=>'40' ),'required');
        }else{
            echo $form->activeTextField($model, 'value', array('title' => '本项必填', 'class' => 'input_text'), 'required&number'); 
        }
        ?></td>
        
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
    }
    
    
    <?php if ($msg['status']) {
        echo "setTimeout(hideMsg,3000);
        ";
        echo "parent.itemQuery();";
    }
    ?>
</script>