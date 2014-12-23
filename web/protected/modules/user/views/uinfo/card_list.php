<div class="title-box">
    <h1><span style="float:left;">我的卡片[用户姓名：<?php echo $_SESSION['cur_user_name'];?>]</span>
        <a href="./?r=user/uinfo/list" style="float:right;margin-left: 15px;"><span class="ing_ico"></span><span>返回用户列表</span></a>
        <?php
        if(Yii::app()->user->type == Operator::TYPE_SYS)
        {
        ?>
        <a href="./?r=user/uinfo/newcard&id=<?php echo $_SESSION['cur_user_id'];?>" style="float:right;margin-left: 15px;"><span class="add_ico"></span><span>卡片添加</span></a>
        <?php
        }
        ?>
    </h1>
    <ul class="sift">
        <?php $this->renderPartial('_c_toolBox'); ?>
    </ul>
</div>
<div id="datagrid">
    <?php $this->actionCardList(); ?>
</div>
<style type="text/css">
    #row_desc span{
        color: #808080;
    }
</style>
<script type="text/javascript">
    
   
    var itemDelete = function(id){
        if(!confirm("确认要删除图片吗？")){return ;}
        $.ajax({
            data:{id:id},
            url:"index.php?r=user/uinfo/delcard",
            dataType:"json",
            type:"POST",
            success:function(data){
                if(data.status==1){
                    alert("删除成功！");
                    itemQuery();
                }else{
                    alert("删除失败！"+data.msg);
                }
            }
        })
    }

</script>