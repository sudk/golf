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
    'htmlOptions'=>array('enctype'=>'multipart/form-data'),
    'focus' => array($model,'name'),
));
if($__model__!="edit"){
    $checkId="checkId(this);";
}else{
    $readonly=false;
    echo $form->activeHiddenField($model,'court_id');
}
?>
<table class="formList">
    <tr>
        
        <td class="maxname">卡号：</td>
        <td class="mivalue" colspan="3">
            <?php
            echo $form->activeTextField($model, 'card_no', array('title' => '本项必填','maxlength'=>'32'), 'required&number'); 
            echo $form->activeHiddenField($model,'user_id',array(),'required');
            ?></td>  
    </tr>
    <tr>
        <td class="maxname">卡名称：</td>
        <td class="mivalue" colspan="3">
            <?php
            echo $form->activeTextField($model, 'card_name', array('title' => '本项必填','maxlength'=>'32'), 'required'); 
          
            ?></td>  
    </tr>
    <tr>
        
        <td class="maxname">描述：</td>
        <td class="mivalue" colspan="3">
            <?php
            echo $form->activeTextField($model, 'desc', array('title' => '本项必填','maxlength'=>'128'), ''); 
           
            ?></td>  
    </tr>
    
    <tr>
        
        <td class="maxname">卡片图片：</td>
        <td class="mivalue" colspan="3">
            <input type="file" name="card_img" value="" class="input_text" id="card_img"/>
        </td>
    </tr>
    
    <tr class="btnBox">
        <td colspan="4">
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
        var desc_obj = jQuery("#card_img");
        var ti = "请上传图片";
        var desc = desc_obj.val();
        if(desc.length == 0){
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