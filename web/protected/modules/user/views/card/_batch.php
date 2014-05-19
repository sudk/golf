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
    'htmlOptions'=>array('enctype'=>'multipart/form-data'),
    'ajaxUpdateId' => 'form-container',
    'focus' => array($model,'upfile'),
));

?>
<table class="formList">
    <tr>
        <td class="maxname">上传文件：</td>
        <td class="mivalue">
            <input type="file" name="upfile" value="" id="upfile"/>
        </td>
    </tr>
   
    <tr>
        
        <td class="maxname">&nbsp;</td>
        <td class="mivalue" colspan="3">
            <p>请点击<a href="index.php?r=user/card/downtemplate" target="_blank">[模板下载]</a>，根据模板填写内容</p>
            <p>内容：会员卡号必填</p>
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
        alert("aa");
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
        var desc_obj = jQuery("#upfile");
        var ti = "请选择上传的文件";
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