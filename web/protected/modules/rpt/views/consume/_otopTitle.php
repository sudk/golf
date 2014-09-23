<ul class="tab-label">
    <?php
    if(Yii::app()->user->checkAccess("rpt/consume/order"))
    {
    ?>
    <li <?php if($cur == 'total') echo 'class="current"';?>>
        <a href="index.php?r=rpt/consume/order">整体数据</a></li>
    <?php
    }
    if(Yii::app()->user->checkAccess("rpt/consume/orderd"))
    {
    ?>
    <li <?php if($cur == 'detail') echo 'class="current"';?>>
        <a href="index.php?r=rpt/consume/orderd">球场明细</a></li>
    <?php
    }
    
    ?>

</ul>