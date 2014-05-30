<div id="content">
    <div class="title-box">
        <h1><span style="float:left;">寄卖商品信息管理</span>
            <?php
        
        if(Yii::app()->user->type == Operator::TYPE_AGENT  && Yii::app()->user->agent_id == '1')
        {
        ?>
            <a href="./?r=service/goods/newgoods" style="float:right;margin-left: 15px;"><span class="add_ico"></span><span>商品添加</span></a>
            <?php
        }
        ?>
        </h1>
        <ul class="sift">
            <?php $this->renderPartial('_toolBox'); ?>
        </ul>
    </div>

    <div id="datagrid">
        <?php $this->actionGrid(); ?>
    </div>
</div>
<style type="text/css">
    #row_desc span{
        color: #808080;
    }
</style>
<script type="text/javascript">
    var itemEdit = function (id) {
        window.location.href='index.php?r=service/goods/detail&id='+id;
    }
    
    var itemDelete = function(id,name){
        if(!confirm("确认要删除:"+name+"吗？")){return ;}
        $.ajax({
            data:{id:id},
            url:"index.php?r=service/goods/del",
            dataType:"json",
            type:"POST",
            success:function(data){
                if(data.status){
                    alert("删除成功！");
                    itemQuery();
                }else{
                    alert("删除失败！");
                }
            }
        })
    }

</script>