<div id="content">
    <div class="title-box">
        <h1><span style="float:left;">行程详细信息</span><a href="./?r=service/route/list" style="float:right;"><span class="ing_ico"></span><span>返回列表</span></a></h1>
    </div>
    <div class="tab-main" id="form-container">
            

<table class="formList">
    <tr>
        <td class="maxname">行程名称：</td>
        <td class="mivalue">
           <?php
                echo htmlspecialchars($model->trip_name);
                //echo $form->activeTextField($model, 'facilitie_name', array('title' => '本项必填', 'class' => 'input_text', 'maxlength' => 20,'onblur'=>$checkId), 'required');         
            ?>
        </td>
        <td class="maxname">所在球场：</td>
        <td class="mivalue"><?php 
        $court_list = Court::getCourtArray();
        //var_dump($court_list);var_dump($model->court_id);
        echo $court_list[$model->court_id]; 
        ?></td>  
    </tr>
    
    <tr>
        <td class="maxname">所在城市：</td>
        <td class="mivalue">
           <?php
          $city_list = CityCode::getCity();
          echo $city_list[$model->city];
                  ?>
        </td>
        <td class="maxname">平日价格：</td>
        <td class="mivalue"><?php echo intval($model->normal_price)/100; ?>元</td>  
    </tr>
    <tr>
        <td class="maxname">假日价格：</td>
        <td class="mivalue">
           <?php
                echo intval($model->holiday_price)/100;         
            ?>
            元
        </td>
        <td class="maxname">包括平日和假日的价格：</td>
        <td class="mivalue"><?php echo intval($model->other_price)/100; ?>元</td>  
    </tr>
    
    <tr>
        <td class="maxname">支付方式：</td>
        <td class="mivalue">
           <?php
                //echo $model->pay_type;
                echo Trip::getPayType($model->pay_type);
                ?>
        </td>
        <td class="maxname">是否需要确认：</td>
        <td class="mivalue"><?php echo Trip::getIsCheck($model->is_check); ?></td>  
    </tr>
    
    <tr>
        <td class="maxname">开始日期：</td>
        <td class="mivalue">
           <?php
                echo $model->start_date;
                ?>
        </td>
        <td class="maxname">结束日期：</td>
        <td class="mivalue"><?php echo $model->end_date; ?></td>  
    </tr>
    
    <tr>
        
        <td class="maxname">备注：</td>
        <td class="mivalue" colspan="3"><?php echo $model->desc;?></td>
    
    
    <?php
    //var_dump($model->id);
    $row = Img::model()->find("relation_id='".$model->id."' and type='".Img::TYPE_TRIP."'");
    //var_dump($row);
    if($row!=null)
    {
    ?>
    <tr>
        
        <td class="maxname">图片：</td>
        <td class="mivalue" colspan="3">
            <img src="index.php?r=court/loadpic&name=<?php echo $row['img_url'];?>" style="width:100px;"/>
        </td>
    </tr>
    <?php
    }
    ?>
   
</table>


    </div>
</div>