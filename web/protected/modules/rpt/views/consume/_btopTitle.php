<ul class="tab-label">
    <?php
    if(Yii::app()->user->checkAccess("rpt/consume/balance"))
    {
    ?>
    <li <?php if($cur == 'total') echo 'class="current"';?>>
        <a href="index.php?r=rpt/consume/balance">整体数据</a></li>
    <?php
    }
    if(Yii::app()->user->checkAccess("rpt/consume/balanced"))
    {
    ?>
    <li <?php if($cur == 'detail') echo 'class="current"';?>>
        <a href="index.php?r=rpt/consume/balanced">每日明细</a></li>
    <?php
    }
    
    ?>

</ul>