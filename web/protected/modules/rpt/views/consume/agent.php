<div id="content">
    <div class="tab">
        <?php
        $this->renderPartial('_topTitle',array('cur'=>'total'));
        ?>
        <ul class="sift">
            <?php $this->renderPartial('_a_toolBox'); ?>
        </ul>
    </div>

    <div id="datagrid">
        <?php $this->actionAGrid(); ?>
    </div>
</div>
    <script type="text/javascript">
        var tostr=function(obj){
            var json_str="";
            if(typeof obj=="object"){
                for(k in obj)
                {
                    if(typeof obj[k] =="object"){
                        var str=tostr(obj[k]);
                    }else{
                        var str=k+"="+obj[k]+"<br>";
                    }
                    json_str+=str;
                }
                return  json_str;
            }else {
                return  obj;
            }
        }
    </script>