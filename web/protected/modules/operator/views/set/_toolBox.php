<form name="_query_form" id="_query_form" action="javascript:itemQuery(0);">
</form>
<script type="text/javascript" src="js/JQdate/WdatePicker.js"></script>
<script type="text/javascript">
 		

    var itemQuery = function(){
        var length=arguments.length;
        if(length==1){
            <?=$this->gridId?>.page = arguments[0];
        }
        var objs = document.getElementById("_query_form").elements;
        var i = 0;
        var cnt = objs.length;
        var obj;
        var url = '';
        for (i = 0; i < cnt; i++) {
            obj = objs.item(i);
            url += '&' + obj.name + '=' + obj.value;
        }
<?php echo $this->gridId; ?>.condition = url;
<?php echo $this->gridId; ?>.refresh();
    }

</script>