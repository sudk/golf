<div class="title-box">
    <h1><span style="float:left;">球场图片[球场名称：<?php echo $_SESSION['cur_court_name'];?>]</span><a href="./?r=court/list" style="float:right;"><span class="ing_ico"></span><span>返回球场列表</span></a><a href="./?r=court/newPic" style="float:right;"><span class="add_ico"></span><span>球场图片添加</span></a></h1>
    <ul class="sift">
        <?php $this->renderPartial('_pic_toolBox'); ?>
    </ul>
</div>
<div id="datagrid">
    <?php $this->actionPicList(); ?>
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
            url:"index.php?r=court/delpic",
            dataType:"json",
            type:"POST",
            success:function(data){
                if(data.status){
                    alert("删除成功！");
                    itemQuery();
                }else{
                    alert("删除失败！"+data.msg);
                }
            }
        })
    }

</script>