<div id="content">
    <div class="title-box">
        <h1><span style="float:left;">添加卡片</span><a href="./?r=user/uinfo/mycard&id=<?php echo $_SESSION['cur_user_id'];?>&name=<?php echo $_SESSION['cur_user_name']?>" style="float:right;"><span class="ing_ico"></span><span>返回卡片列表</span></a></h1>
    </div>
    <div class="tab-main" id="form-container">
            <?php $this->renderPartial('_cardform', array('model' => $model, 'msg' => $msg));?>
    </div>
</div>