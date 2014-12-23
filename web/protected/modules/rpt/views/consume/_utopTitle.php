<ul class="tab-label">
    <?php
    if(Yii::app()->user->checkAccess("rpt/consume/user"))
    {
    ?>
    <li <?php if($cur == 'total') echo 'class="current"';?>>
        <a href="index.php?r=rpt/consume/user">整体数据</a></li>
    <?php
    }
    if(Yii::app()->user->checkAccess("rpt/consume/userd"))
    {
    ?>
    <li <?php if($cur == 'detail') echo 'class="current"';?>>
        <a href="index.php?r=rpt/consume/userd">客户明细</a></li>
    <?php
    }
    
    ?>

</ul>