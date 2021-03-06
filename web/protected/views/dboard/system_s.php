<script type="text/javascript" src="<?php echo Yii::app()->request->baseUrl; ?>/js/jquery.qtip-1.0.0-rc3.min.js"></script>
<div class="clearfix" id='msg' style="width: 100%">
    <div class="index_c"  style="width:49%;">
        <div class="index_c_main">
            <h1 style="width: auto;"><a href="index.php?r=msg/inbox/list"><img src="images/icons/index_1.png" />消息中心 </a></h1>
            <div class="clear"></div>
            <ul class="list" style="overflow-y:auto;height:181px;">
                <?php
                $args['listenerid']=Yii::app()->user->id;
                $args['type'] = MsgListener::TYPE_MSG;
                $list = MsgListener::queryList(1, 5, $args);
                if($list['rows'])
                {
                    foreach($list['rows'] as $row)
                    {
                        echo '<li>',$row['title'],'</li>';
                    }
                }
                ?>
                    
            </ul>
        </div>
         
    </div>
    
    <div class="index_c ml10" style="width: 50%;">
        <div class="index_c_main" >
            <h1 style="width:auto;"><a href="index.php?r=msg/inbox/list"><img src="images/icons/index_3.png" />公告中心 </a></h1>
            <div class="clear"></div>
            <ul class="list" style="overflow-y:auto;height:181px;">
                <?php
                $args['listenerid']=Yii::app()->user->id;
                $args['type'] = MsgListener::TYPE_NOTICE;
                $list = MsgListener::queryList(1, 5, $args);
                if($list['rows'])
                {
                    foreach($list['rows'] as $row)
                    {
                        echo '<li><a href="index.php?r=msg/inbox/list">',$row['title'],'</a></li>';
                    }
                }
                ?>
            </ul>
        </div>
    </div>
</div>
<div class="index_c_big">
	
	<div class="index_c_big_main">
		<ul>
            <li>
                <img src="images/48/01.png" />
                <h1><a href="./?r=service/route/list">套餐管理</a></h1>
            </li>
            <li>
                <img src="images/48/01.png" />
                <h1><a href="./?r=service/mcht/list">特约商户管理</a></h1>
            </li>
            
            <li>
                <img src="images/48/01.png" />
                <h1><a href="./?r=service/competition/list">赛事管理</a></h1>
            </li>
            <li>
                <img src="images/48/01.png" />
                <h1><a href="./?r=rpt/consume/list">消费记录</a></h1>
            </li>
 
        </ul>
    </div>
</div>
<script type="text/javascript">
    // Create the tooltips only on document load
    $(document).ready(function()
    {
        // Use the each() method to gain access to each elements attributes
        $('#msg a[rel]').each(function()
        {
            $(this).qtip(
                {
                    content: {
                        // Set the text to an image HTML string with the correct src URL to the loading image you want to use
                        text: '<img  src="./images/loading.gif" alt="Loading..." />',
                        url: $(this).attr('rel'), // Use the rel attribute of each element for the url to load
                        title: {
                            text: '标题 - ' + $(this).text(), // Give the tooltip a title using each elements text
                            button: 'Close' // Show a close link in the title
                        }
                    },
                    position: {
                        corner: {
                            target: 'bottomMiddle', // Position the tooltip above the link
                            tooltip: 'topMiddle'
                        },
                        adjust: {
                            screen: true // Keep the tooltip on-screen at all times
                        }
                    },
                    show: {
                        when: 'click',
                        solo: true // Only show one tooltip at a time
                    },
                    hide: 'unfocus',
                    style: {
                        tip: true, // Apply a speech bubble tip to the tooltip at the designated tooltip corner
                        border: {
                            width: 0,
                            radius: 4
                        },
                        name: 'light', // Use the default light style
                        width: 570 // Set the tooltip width
                    }
                })
        });
    });
</script>