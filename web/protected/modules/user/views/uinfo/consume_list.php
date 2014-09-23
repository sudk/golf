<div class="title-box">
    <h1><span style="float:left;">我的消费记录[用户姓名：<?php echo $_SESSION['cur_user_name'];?>]</span>
        <a href="./?r=user/uinfo/list" style="float:right;margin-left: 15px;"><span class="ing_ico"></span><span>返回用户列表</span></a>
        
    </h1>
    <ul class="sift">
        <?php $this->renderPartial('_consume_toolBox'); ?>
    </ul>
</div>
<div id="datagrid">
    <?php $this->actionCGrid(); ?>
</div>
<style type="text/css">
    #row_desc span{
        color: #808080;
    }
</style>
<script type="text/javascript">
    
</script>