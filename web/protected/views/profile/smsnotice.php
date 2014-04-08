<?php
/**
 * Created by JetBrains PhpStorm.
 * User: wangdy
 * Date: 12-2-6
 * Time: 下午2:32
 * To change this template use File | Settings | File Templates.
 */

?>
<?php $form = $this->beginWidget('SimpleForm', array(
    'id'=>'form1',
    'enableAjaxSubmit'=>false,
    'ajaxUpdateId'=>'form-container',
    'focus'=>array($model,name),
    'htmlOptions'=>array("enctype2"=>"multipart/form-data"),
)); ?>

<ul class="subnav dialog" style="top:0; left:0;">
    <li><a href="index.php?r=site/passwd"><span>修改密码</span></a></li>
    <li class="current"><a href="index.php?r=profile/smsnotice"><span>短信接收设置</span></a></li>
</ul>
<h1 style="display: block;border-bottom: 1px #ccc solid;"></h1>
<div style="padding: 20px 20px 20px 20px;">
    <div><?php if($message!='') echo '<span class="informationMessage">'.$message.'</span>';?></div>
    <table class="formList">
        <tr>
            <td class="name" style="width:30%;">
                <?php echo $form->error($model,'notice2sms'); ?>
                <?php echo $form->label('公告短信提醒:','notice2sms'); ?>
            </td>
            <td style="text-align: left;">
                <?php
                $notice2smsRs = Staff::notice2smsOptions();
                foreach ($notice2smsRs as $i => $type) {
                    if ($i == $model->notice2sms) {
                        echo '&nbsp;<input type="radio" validator="required" checked="checked"  id="Staff_notice2sms_'.$i.'" value="'.$i.'" name="Staff[notice2sms]">';
                        echo '&nbsp;<label for="Staff_notice2sms_'.$i.'">'.$type.'</label>';
                    }
                    else {
                        echo '&nbsp;<input type="radio" validator="required" id="Staff_notice2sms_'.$i.'" value="'.$i.'" name="Staff[notice2sms]">';
                        echo '&nbsp;<label for="Staff_notice2sms_'.$i.'">'.$type.'</label>';
                    }
                }
                ?>
            </td>
        </tr>
        <tr>
            <td class="name">
                <?php echo $form->error($model,'remark2sms'); ?>
                <?php echo $form->label('德育短信提醒:','remark2sms'); ?>
            </td>
            <td style="text-align: left;">
                <?php
                $remark2smsRs = Staff::Remark2smsOptions();
                foreach ($remark2smsRs as $i => $type) {
                    if ($i == $model->remark2sms) {
                        echo '&nbsp;<input type="radio" validator="required" checked="checked"  id="Staff_remark2sms_'.$i.'" value="'.$i.'" name="Staff[remark2sms]">';
                        echo '&nbsp;<label for="Staff_remark2sms_'.$i.'">'.$type.'</label>';
                    }
                    else {
                        echo '&nbsp;<input type="radio" validator="required" id="Staff_remark2sms_'.$i.'" value="'.$i.'" name="Staff[remark2sms]">';
                        echo '&nbsp;<label for="Staff_remark2sms_'.$i.'">'.$type.'</label>';
                    }
                }
                ?>

            </td>
        </tr>
        <tr>
            <td class="name">
                <?php echo $form->error($model,'attendance2sms'); ?>
                <?php echo $form->label('考勤短信提醒:','attendance2sms'); ?>
            </td>
            <td style="text-align: left;">
                <?php
                $attendance2smsRs = Staff::Attendance2smsOptions();
                foreach ($attendance2smsRs as $i => $type) {
                    if ($i == $model->attendance2sms) {
                        echo '&nbsp;<input type="radio" validator="required" checked="checked"  id="Staff_attendance2sms_'.$i.'" value="'.$i.'" name="Staff[attendance2sms]">';
                        echo '&nbsp;<label for="Staff_attendance2sms_'.$i.'">'.$type.'</label>';
                    }
                    else {
                        echo '&nbsp;<input type="radio" validator="required" id="Staff_attendance2sms_'.$i.'" value="'.$i.'" name="Staff[attendance2sms]">';
                        echo '&nbsp;<label for="Staff_attendance2sms_'.$i.'">'.$type.'</label>';
                    }
                }
                ?>
            </td>
        </tr>
        <tr class="btnBox">
            <td colspan="2">
                            <span class="sBtn" id="btnSubmit">
                                <a class="left">修改</a><a class="right"></a>
                            </span>
                            <span class="sBtn-cancel">
                                <a class="left" href="" id="btnClose">关闭</a><a class="right"></a>
                            </span>
            </td>
        </tr>
    </table>
</div>
<?php $this->endWidget(); ?>

<script type="text/javascript">
    jQuery(document).ready(function(){
        jQuery("#btnClose").click(function(){
            parent.jQuery("#windown-close").click();
        })
        jQuery("#btnSubmit").click(function() {
            jQuery("#form1").submit();

        });
    });
</script>