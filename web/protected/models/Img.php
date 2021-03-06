<?php
/**
 * --请填写模块名称--
 *
 * @author #author#
 * @copyright Copyright &copy; 2003-2009 TrunkBow Co., Inc
 */
class Img extends CActiveRecord {



    CONST TYPE_COURT_FACILITIES = '2';
    CONST TYPE_COURT_LOGO = '8';
    CONST TYPE_ADV = '7';
    CONST TYPE_CARD = '5';
    CONST TYPE_TRIP = '6';
    CONST TYPE_COMPETITION = '3';
    CONST TYPE_FLEA = '4';
    CONST TYPE_NEWS = '9';
    const IMG_PATH="http://115.28.77.119/images/picture/";
    
    
    public static function model($className=__CLASS__){
        return parent::model($className);
    }

    public function tableName(){
        return 'g_img';
    }
    
    public static function  getType($type=null)
    {
        $rs = array(
            '8'=>'球场标志',
            '0'=>'球场风景',
            '1'=>'球道',
            '2'=>'球场附近设施',
            '3'=>'赛事图片',
            '4'=>'寄卖物品',
            '5'=>'会员卡',
            '6'=>'行程',
            '7'=>'广告',
            '9'=>'新闻',
        );
        
        return $type?$rs[$type]:$rs;
    }
    
    public static function queryList($page,$pageSize,$args)
    {
        $params = array();
        $condition= "";
        if ($args['relation_id'] != ''){
            if($condition!=""){
                $condition .= " AND ";
            }
            $condition.= ' relation_id=:relation_id';
            $params['relation_id'] = $args['relation_id'];
        }
        
        if ($args['type'] != ''){
            if($condition!=""){
                $condition .= " AND ";
            }
            $condition.= ' type=:type';
            $params['type'] = $args['type'];
        }
        
        if($args['from']!="" && $args['from'] == 'court'){
            if($condition!=""){
                $condition .= " AND ";
            }
            $condition .= "type in ('0','1','8')";
        }
        
        
        
        $total_num = Img::model()->count($condition, $params); //总记录数

        $criteria = new CDbCriteria();
        $criteria->order = 'type,record_time  DESC';
    	

        $criteria->condition = $condition;
        $criteria->params = $params;

        $pages = new CPagination($total_num);
        $pages->pageSize = $pageSize;
        $pages->setCurrentPage($page);
        $pages->applyLimit($criteria);

        $rows = Img::model()->findAll($criteria);

        $rs['status'] = 0;
        $rs['desc'] = '成功';
        $rs['page_num'] = ($pages->currentPage + 1);
        $rs['total_num'] = $total_num;
        $rs['total_page'] = ceil($rs['total_num'] / $rs['page_num']);
        $rs['num_of_page'] = $pages->pageSize;
        $rs['rows'] = $rows;

        return $rs;
    }
    
    /**
     * 上传图片到服务器上
     * @param type $tmp_name
     * @param type $file_name
     * @return string
     */
    public static function uploadImg($tmp_file,$tmp_name,$relation_id,$type,$is_save_to_db=true){
        $upload_dir = Yii::app()->params['upload_dir'];
        //路径是以目录/日期/时间+rand(100,999).png
        $upload_dir .= date('Ymd');
        if(!is_dir($upload_dir))
        {
            @mkdir($upload_dir."/",0777); 
        }
        
        $name_a = explode(".", $tmp_name);
        $suffix =  $name_a[count($name_a)-1];
        
        $file = date('His').  rand(100, 9999);
        $new_file_path = $upload_dir."/".$file;
        $new_file_name = $new_file_path.".".$suffix;
        
        $rs['status'] = 0;
        $rs['msg'] = '成功';
        $rs['url'] = date('Ymd')."/".$file.".".$suffix;
        if(!move_uploaded_file($tmp_file, $new_file_name))
        {
            $rs['status'] = -1;
            $rs['msg'] = "上传图片到服务器失败";
        }else{
            //然后要生成一张缩略图
            $new_file_name_s = $new_file_path."_small.".$suffix;
            self::saveThumbnails($new_file_name, $new_file_name_s);
        }
        
        //add data into db
        if($is_save_to_db){
            $model = new Img();
            $model->relation_id = $relation_id;
            $model->type = $type;
            $model->img_url = $rs['url'];
            $model->record_time = date('Y-m-d H:i:s');
            $model->save();
        }

        return $rs;
    }
    
    /**
     * 保存头像
     * 180x180 50x50 30x30三个尺寸
     */
    public static function saveThumbnails($file_name,$file_name_small,$img_size=56)
    {
        $middle_size = $img_size;     
        $src = $file_name;
        //$suffix = strtolower($suffix);
        
        $img_array = getimagesize($file_name);
	$mime = $img_array['mime'];
        $file_mime = explode("/", $mime);
        $suffix = $file_mime[1];
        if($suffix == 'png')
        {
            $img_r = @imagecreatefrompng($src);
            
            $img_r_m = self::resizeImage($img_r,$middle_size,$middle_size,$src);
            imagepng($img_r_m,$file_name_small);
        }else if($suffix == 'gif')
        {
            $img_r  = @imagecreatefromgif($src);
            
            $img_r_m = self::resizeImage($img_r,$middle_size,$middle_size,$src);
            imagegif($img_r_m,$file_name_small);
            
        }else//jpg
        {
            $img_r = imagecreatefromjpeg($src);       
            $img_r_m = self::resizeImage($img_r,$middle_size,$middle_size,$src);
            imagejpeg($img_r_m,$file_name_small);
           
        }        
        imagedestroy($img_r);   
        imagedestroy($img_r_m);
       
        return true;
    }
    
    public static function delImg($relation_id,$type)
    {
        $upload_dir = Yii::app()->params['upload_dir'];
        
        $rs['status'] = 0;
        $rs['msg'] = "";
        $rows = Img::model()->findAll("relation_id='".$relation_id."' and type='".$type."'");
        if($rows)
        {
            foreach($rows as $row)
            {
                $img_url = $row['img_url'];
                $id = $row['id'];
                
                if(Img::model()->deleteByPk($id))
                {
                    self::delSimpleImg($img_url);
                    
                }else{
                    $rs['msg'] .= "编号为".$id."的图片删除失败";
                }
                
            }
        }
        if($rs['msg']!="")
        {
            $rs['status'] = -1;
            
        }
        
        return $rs;
    }
    
    
    /**
     *等比例缩放
     * @param <type> $im--image object
     * @param <type> $maxwidth  目标图片的宽度 例如168
     * @param <type> $maxheight  目标图片的高度 例如168
     */
    public static function resizeImage($im,$maxwidth=116,$maxheight=116,$src)
    {
        //源图片的宽、高
        $pic_width = imagesx($im);
        $pic_height = imagesy($im);
        //$pic_array = getimagesize($src);
        //$pic_width = $pic_array[0];
        //$pic_height = $pic_array[1];
        //if(($maxwidth && $pic_width > $maxwidth) || ($maxheight && $pic_height > $maxheight))
        if(true)
        {

            $newwidth = $maxwidth;//$pic_width * $ratio;
            $newheight = $maxheight;//$pic_height * $ratio;
           
            if(function_exists("imagecopyresampled"))
            {
                $newim = imagecreatetruecolor($newwidth,$newheight);
                imagecopyresampled($newim,$im,0,0,0,0,$newwidth,$newheight,$pic_width,$pic_height);
            }
            else
            {
                $newim = imagecreate($newwidth,$newheight);
               imagecopyresized($newim,$im,0,0,0,0,$newwidth,$newheight,$pic_width,$pic_height);
            }

            return $newim;

        }
        else
        {
            return $im;
        }
    }
    
    
    public static function delSimpleImg($url)
    {
        $upload_dir = Yii::app()->params['upload_dir'];
        $url_array = explode(".", $url);
        $file_small = $url_array[0]."_small.".$url_array[1];
        @unlink($upload_dir.$url);
        @unlink($upload_dir.$file_small);
        return true;
    }

    public static function GetImgs($relation_id,$type){
        $condition="relation_id=:relation_id and type=:type";
        $params=array('relation_id'=>$relation_id,'type'=>$type);

        $rows=Yii::app()->db->createCommand()
            ->select("*")
            ->from("g_img")
            ->where($condition,$params)
            ->queryAll();
        $rows_tmp=array();
        if($rows){
            $rows_tmp=array();
            foreach($rows as $row){
                $rows_tmp[]=Img::IMG_PATH.$row['img_url'];
            }
        }
        return $rows_tmp;
    }
    public static function GetImg($relation_id,$type,$no_path=false){
        $condition="relation_id=:relation_id and type=:type";
        $params=array('relation_id'=>$relation_id,'type'=>$type);

        $row=Yii::app()->db->createCommand()
            ->select("*")
            ->from("g_img")
            ->where($condition,$params)
            ->queryRow();
        if($row){
            if($no_path){
                return $row['img_url'];
            }else{
                return Img::IMG_PATH.$row['img_url'];
            }
        }else{
            return "";
        }
    }

    public static function GetBasePath($url){
        return str_replace(Img::IMG_PATH,'',$url);
    }
}


    