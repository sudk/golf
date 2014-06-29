<div class="title-box">
    <h1><span style="float:left;">系统设置</span></h1>
<!--    <ul class="sift">
        <?php //$this->renderPartial('_toolBox'); ?>
    </ul>-->
</div>
<div id="datagrid">
    <?php $this->actionGrid(); ?>
</div>
<style type="text/css">
    #row_desc span{
        color: #808080;
    }
</style>
<script type="text/javascript">
    var itemEdit = function (id) {
        tipsWindown(
            "编辑代理商信息", // title：窗口标题
            "iframe:index.php?r=operator/set/edit&id=" + id, // Url：弹窗所加截的页面路径
            "900", // width：窗体宽度
            "520", // height：窗体高度
            "true", // drag：是否可以拖动（ture为是,false为否）
            "", // time：自动关闭等待的时间，为空代表不会自动关闭
            "true", // showbg：设置是否显示遮罩层（false为不显示,true为显示）
            "text"    // cssName：附加class名称
        );
    }
  
</script>