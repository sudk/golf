<div class="title-box">
    <h1><span style="float:left;">订单列表</span></h1>
    <ul class="sift">
        <?php $this->renderPartial('_toolBox'); ?>
    </ul>
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
            "编辑订单信息", // title：窗口标题
            "iframe:index.php?r=order/edit&id=" + id, // Url：弹窗所加截的页面路径
            "700", // width：窗体宽度
            "400", // height：窗体高度
            "true", // drag：是否可以拖动（ture为是,false为否）
            "", // time：自动关闭等待的时间，为空代表不会自动关闭
            "true", // showbg：设置是否显示遮罩层（false为不显示,true为显示）
            "text"    // cssName：附加class名称
        );
    }

    var itemConfirm = function(id,status_str) {
        tipsWindown(
            "编辑订单信息", // title：窗口标题
            "iframe:index.php?r=order/confirm&id=" + id+"&status_str="+status_str, // Url：弹窗所加截的页面路径
            "700", // width：窗体宽度
            "400", // height：窗体高度
            "true", // drag：是否可以拖动（ture为是,false为否）
            "", // time：自动关闭等待的时间，为空代表不会自动关闭
            "true", // showbg：设置是否显示遮罩层（false为不显示,true为显示）
            "text"    // cssName：附加class名称
        );
    }

    /**
     * 处理订单的状态
     * @param {type} id
     * @param {type} now_status
     * @param {type} next_status
     * @param {type} pay_type
     * @param {type} title
     * @returns {undefined}
     */
    var itemNextStatus = function(id,now_status,next_status,pay_type,title)
    {
        //next_status=='1'||next_status == '5'
        if(next_status=='1'||next_status=='5'||next_status=='4'){
            if(!confirm("确定这么做吗？")){return ;}
            $.ajax({
                data:{id:id,ns:now_status,s:next_status,type:pay_type},
                url:"index.php?r=order/confirmstatus",
                dataType:"json",
                type:"POST",
                success:function(data){
                    if(data.status){
                        alert("操作成功！");
                        itemQuery();
                    }else{
                        alert("操作失败！"+data.msg);
                    }
                }
            });
        }else{
            tipsWindown(
                ""+title, // title：窗口标题
                "iframe:index.php?r=order/nextstatus&id=" + id+"&ns="+now_status+"&s="+next_status+"&type="+pay_type, // Url：弹窗所加截的页面路径
                "500", // width：窗体宽度
                "300", // height：窗体高度
                "true", // drag：是否可以拖动（ture为是,false为否）
                "", // time：自动关闭等待的时间，为空代表不会自动关闭
                "true", // showbg：设置是否显示遮罩层（false为不显示,true为显示）
                "text"    // cssName：附加class名称
            );
        }
    }
    
    
    var itemLog = function (id) {
        window.location.href = "index.php?r=order/log&id="+id;
    }
    
   
    var itemDelete = function(id){
        if(!confirm("确认要删除订单:"+id+"吗？")){return ;}
        $.ajax({
            data:{id:id},
            url:"index.php?r=order/del",
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