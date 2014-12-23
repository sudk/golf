<ul class="tab-label">
    <?php
    if(Yii::app()->user->checkAccess("rpt/consume/agent"))
    {
    ?>
    <li <?php if($cur == 'total') echo 'class="current"';?>>
        <a href="index.php?r=rpt/consume/agent">整体数据</a></li>
    <?php
    }
    if(Yii::app()->user->checkAccess("rpt/consume/agentd"))
    {
    ?>
    <li <?php if($cur == 'detail') echo 'class="current"';?>>
        <a href="index.php?r=rpt/consume/agentd">球场明细</a></li>
    <?php
    }
    
    ?>

</ul>