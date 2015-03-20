<div class="well row" id="well">
    <form class="form-horizontal" role="form" id="form1" action="to_submit()" method="post">
        <div class="form-group">
            <label for="card_id" class="col-xs-3 control-label text-right text-muted">地区*</label>
            <div class="col-xs-9">
                <input type="text" name="area" id="area" class="form-control" placeholder="地区">
            </div>
        </div>
        <div class="form-group">
            <label for="card_id" class="col-xs-3 control-label text-right text-muted">球场*</label>
            <div class="col-xs-9">
                <select name="court_name" id="court_name" class="form-control">
                    <option value="" selected>--请选择--</option>
                    <?php if (count($court)): ?>
                        <?php foreach ($court as $n): ?>
                            <option value="<?= $n ?>"><?= $n ?></option>
                        <?php endforeach; ?>
                    <?php endif; ?>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label for="card_id" class="col-xs-3 control-label text-right text-muted">人数*</label>
            <div class="col-xs-9">
                <input type="number" name="m_count"  id="m_count" class="form-control" placeholder="参加人数">
            </div>
        </div>
        <div class="form-group">
            <label for="card_id" class="col-xs-3 control-label text-right text-muted">&nbsp;</label>
            <div class="col-xs-9">
                <input type="number" name="d_count" class="form-control" placeholder="多少天">
            </div>
        </div>
        <div class="form-group">
            <label for="card_id" class="col-xs-3 control-label text-right text-muted">&nbsp;</label>
            <div class="col-xs-9">
                <input type="number" name="n_count" class="form-control" placeholder="多少晚">
            </div>
        </div>
        <div class="form-group">
            <label for="card_id" class="col-xs-3 control-label text-right text-muted">&nbsp;</label>
            <div class="col-xs-9">
                <input type="number" name="f_count" id="f_count"  onchange="ch_fc(this)" class="form-control" placeholder="多少球场*">
            </div>
        </div>
        <div id="tee_time">
            <div class="form-group">
                <label for="card_id" class="col-xs-3 control-label text-right text-muted">开球日期</label>
                <div class="col-xs-9">
                    <input type="text" name="tee_time[]"  data-role="datebox" data-options='{"mode":"flipbox"}' class="form-control" placeholder="开球日期">
                </div>
            </div>
        </div>
        <div class="form-group">
            <label for="card_id" class="col-xs-3 control-label text-right text-muted">酒店选择</label>
            <div class="col-xs-9">
                <select name="hotel_type" class="form-control">
                    <option value="" selected>--请选择--</option>
                    <option value="不需要">不需要</option>
                    <option value="需要">需要</option>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label for="card_id" class="col-xs-3 control-label text-right text-muted">酒店星级</label>
            <div class="col-xs-9">
                <select name="hotel_star" class="form-control">
                    <option value="" selected>--请选择--</option>
                    <option value="三星级">三星级</option>
                    <option value="四星级">四星级</option>
                    <option value="五星级">五星级</option>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label for="card_id" class="col-xs-3 control-label text-right text-muted">入住天数</label>
            <div class="col-xs-9">
                <input type="text" name="live_count" class="form-control" placeholder="入住天数">
            </div>
        </div>
        <div class="form-group">
            <label for="card_id" class="col-xs-3 control-label text-right text-muted">房间类型</label>
            <div class="col-xs-9">
                <select name="room_type" class="form-control">
                    <option value="" selected>--请选择--</option>
                    <option value="套房">套房</option>
                    <option value="大床房">大床房</option>
                    <option value="标准间">标准间</option>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label for="card_id" class="col-xs-3 control-label text-right text-muted">房间数量</label>
            <div class="col-xs-9">
                <input type="text" name="room_count" class="form-control" placeholder="房间数量">
            </div>
        </div>
        <div class="form-group">
            <label for="card_id" class="col-xs-3 control-label text-right text-muted">特殊要求</label>
            <div class="col-xs-9">
                <input type="text" name="desc" class="form-control" placeholder="特殊要求">
            </div>
        </div>
        <div class="form-group">
            <label for="card_id" class="col-xs-3 control-label text-right text-muted">用车要求</label>
            <div class="col-xs-9">
                <select name="car_require" class="form-control">
                    <option value="" selected>--请选择--</option>
                    <option value="不需要">不需要</option>
                    <option value="需要">需要</option>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label for="card_id" class="col-xs-3 control-label text-right text-muted">汽车类型</label>
            <div class="col-xs-9">
                <select name="car_type" class="form-control">
                    <option value="" selected>--请选择--</option>
                    <option value="微型轿车">微型轿车</option>
                    <option value="商务车">商务车</option>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label for="card_id" class="col-xs-3 control-label text-right text-muted">联系人*</label>
            <div class="col-xs-9">
                <input type="text" name="contact" id="contact" class="form-control" placeholder="联系人">
            </div>
        </div>
        <div class="form-group">
            <label for="card_id" class="col-xs-3 control-label text-right text-muted">电话*</label>
            <div class="col-xs-9">
                <input type="text" name="phone" id="phone" class="form-control" placeholder="联系电话">
            </div>
        </div>
        <div class="form-group">
            <label for="card_id" class="col-xs-3 control-label text-right text-muted">简短留言</label>
            <div class="col-xs-9">
                <input type="text" name="remark" id="remark" class="form-control" placeholder="简短留言">
            </div>
        </div>
        <div class="form-group">
            <div class="col-xs-offset-3 col-xs-9">
                <button type="button" onclick="to_submit(this)" data-loading-text="正在处理..."
                        class="btn btn-primary btn-lg">提交
                </button>
            </div>
        </div>
    </form>
    <div id="tee_time_h" style="display:none;">
        <div class="form-group">
            <label for="card_id" class="col-xs-3 control-label text-right text-muted">开球日期</label>
            <div class="col-xs-9">
                <input type="text" name="tee_time[]"  data-role="datebox" data-options='{"mode":"flipbox"}' class="form-control" placeholder="开球日期">
            </div>
        </div>
    </div>
</div>
<script>
    function ch_fc(obj) {
        var v=parseInt($(obj).val());
        if(v>0){
            var template=$("#tee_time_h").html();
            $("#tee_time").html("");
            if(v>5){
                v=5;
            }
            do{
                $("#tee_time").append(template);
                v=v-1;
            }while(v>0)
        }
    }
    function to_submit(obj) {
        var flag = true;
        var area = $("#area").val();
        var court_name = $("#court_name").val();
        var m_count = $("#m_count").val();
        var contact = $("#contact").val();
        var phone = $("#phone").val();
        if (!area) {
            flag = false;
            alert("请填写地区！");
            return;
        }
        if (!court_name) {
            flag = false;
            alert("请选择球场！");
            return;
        }
        if (!m_count) {
            flag = false;
            alert("请填写打球人数！");
            return;
        }
        if (!contact) {
            flag = false;
            alert("请填联系人！");
            return;
        }
        if (!phone) {
            flag = false;
            alert("请填写电话！");
            return;
        }
        $(obj).button('loading');
        if (flag) {
            $("#form1").attr("action", "<?=$_SERVER['REQUEST_URI']?>");
            $("#form1").submit();
        } else {
            $(obj).button('reset');
        }
    }
</script>