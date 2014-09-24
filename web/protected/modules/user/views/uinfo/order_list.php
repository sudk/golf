<div class="title-box">
    <h1><span style="float:left;">我的订单[用户手机号：<?php echo $_SESSION['cur_user_isdn'];?>]</span>
        <a href="./?r=user/uinfo/list" style="float:right;margin-left: 15px;"><span class="ing_ico"></span><span>返回用户列表</span></a>
        
    </h1>
    <ul class="sift">
        <?php $this->renderPartial('_o_toolBox'); ?>
    </ul>
</div>
<div id="datagrid">
    <?php $this->actionOGrid(); ?>
</div>
<style type="text/css">
    #row_desc span{
        color: #808080;
    }
</style>
<script type="text/javascript">
var showDetail = function (obj, desc, show) {
        $("#row_desc").remove();
        if (c_Note) {
            $(c_Note).removeClass("towfocus");
        }
        if (show && c_Note == obj) {
            c_Note = null;
            return;
        }
        $(obj).after("<tr id='row_desc' class='towfocus' ><td colspan='"+obj.cells.length+"'>" + desc + "</td></tr>");
        c_Note = obj;
        $(c_Note).addClass("towfocus");
    }
    var c_Note=null;
    var datainfo={};
    var getDetail=function(obj,objid){
        if(datainfo[objid]){
            showDetail(obj,datainfo[objid],true);
            return;
        }
        var detail="";
        $.ajax({
            data:{id:objid},
            url:"./?r=order/detail",
            type:"POST",
            dataType:"json",
            beforeSend:function(){
                detail="正在获取数据...";
                showDetail(obj,detail,false);
            },
            success:function(data){
                detail=data.detail
                if(data.status){
                    datainfo[objid]=detail;
                }
                showDetail(obj,detail,false);
            }
        })
    }
</script>