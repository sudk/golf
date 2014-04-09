<?php
/*
 * 球场管理
 */
class CourtController extends BaseController
{

    public $defaultAction = 'list';
    public $gridId = 'list';
    public $picGridId = 'pic_list';
    public $cGridId = 'comment_list';
    public $pageSize = 100;
    public $module_id = 'court';

    /**
     * 表头
     * @return SimpleGrid
     */
    private function genDataGrid()
    {
        $t = new SimpleGrid($this->gridId);
        $t->url = 'index.php?r=court/grid';
        $t->updateDom = 'datagrid';
        $t->set_header('球场名称', '100', '');
        $t->set_header('球场模式', '70', '');   
        $t->set_header('球场电话', '60', '');
        $t->set_header('球场地址', '100', '');
        $t->set_header('操作', '100', '');
        return $t;
    }

    /**
     * 查询
     */
    public function actionGrid()
    {
        $page = $_GET['page'] == '' ? 0 : $_GET['page']; //当前页码
        $_GET['page']=$_GET['page']+1;
        $args = $_GET['q']; //查询条件


        if ($_REQUEST['q_value'])
        {
            $args[$_REQUEST['q_by']] = $_REQUEST['q_value'];
        }

        $t = $this->genDataGrid();

        $list = Court::queryList($page, $this->pageSize, $args);

        $this->renderPartial('_list', array('t' => $t, 'rows' => $list['rows'], 'cnt' => $list['total_num'], 'curpage' => $list['page_num']));
    }

    /**
     * 列表
     */
    public function actionList()
    {
        $this->render('list');
    }

    private function checkName($name)
    {
        $cnt=Court::model()->count("name='".trim($name)."'");
            //print_r($operator);
        if($cnt > 0){
            return false;
        }else{
            return true;
        }
    }
    public function actionNew(){
        $model=new Court('create');
        if($_POST['Court']){
            $model->attributes=$_POST['Court'];
            //先判断name是否重复
            if(!$this->checkName($_POST['Court']['name']))
            {
                $msg['msg']="添加失败！球场名称重复";
                $msg['status']=-1;
            }else
            {
                $model->record_time=date("Y-m-d H:i:s");
                $model->remark=htmlspecialchars($_POST['Court']['remark']);
                $model->facilities=htmlspecialchars($_POST['Court']['facilities']);
                $model->creatorid = Yii::app()->user->id;
                $coords = $this->getAddrPoint($_POST['Court']['addr']);
                //var_dump($coords);
                $model->lon = $coords['lon'];
                $model->lat = $coords['lat'];
                $model->court_id = Yii::app()->user->id.date("YmdHis");
                //var_dump($_POST['Court']);var_dump($model->attributes);exit;
                try{
                    $rs=$model->save();
                    if($rs){
                        $msg['msg']="添加成功！";
                        $msg['status']=1;
                        $model=new Court('create');
                    }else{
                        $msg['msg']="添加失败！";
                        $msg['status']=-1;
                    }
                }catch (Exception $e){
                    if($e->errorInfo[0]==23000){
                        $msg['msg']="球场编号重复！";
                        $msg['status']=-1;
                    }

                }

                //add log
                $log_args = array(
                    'module_id'=>$this->module_id,
                    'opt_name'=>'球场添加',
                    'opt_detail'=>"球场编号：".$model->court_id."球场名称：".$model->name.".".$msg['msg'],
                    'opt_status'=>$msg['status']==1 ? "00":"01",
                );
                Operatorlog::addLog($log_args);
            }
        }
        $this->render("new",array('model' => $model, 'msg' => $msg));
    }

    public function actionEdit(){
        $id=$_GET['id'];
        $model=Court::model()->findByPk($id);
        if($_POST['Court']){
            
            $model->setScenario("modify");
            $model->attributes=$_POST['Court'];
            $model->desc=htmlspecialchars($_POST['Court']['desc']);
            
            $rs=$model->save();
            if($rs){
                $msg['msg']="修改成功！";
                $msg['status']=1;
                //$model=new Staff('modify');
            }else{
                $msg['msg']="修改失败！";
                $msg['status']=0;
            }
            
            //add log
            $log_args = array(
                'module_id'=>$this->module_id,
                'opt_name'=>'会员卡编辑',
                'opt_detail'=>"会员卡号：".$model->id.".".$msg['msg'],
                'opt_status'=>$msg['status']== 1 ? "00":"01",
            );
            Systemlog::addLog($log_args);

        }
       //var_dump($msg);
        $this->layout = '//layouts/base';
        $this->render("edit",array('model' => $model, 'msg' => $msg));
    }

 
    public function actionCheckid(){
        $id=$_GET['id'];
        $data['status']=true;
        if($id){
            $cnt=Court::model()->count("name='".trim($id)."'");
            //print_r($operator);
            if($cnt > 0){
                $data['msg']=2;
            }else{
                $data['msg']=0;
            }
        }else{
            $data['status']=false;
        }
        print_r(json_encode($data));
    }

    public function actionGetCity()
    {
        $pid = trim($_GET['pid']);
        
        $list = CityCode::getCity($pid);
        
        if(@count($list)>0)
        {
            foreach($list as $key=>$value)
            {
                echo '<option value="',$key,'">',$value,'</option>';
            }
        }else
        {
            echo '<option value="">--选择--</option>';
        }
        exit;
    }
  
    
    public function actionDel(){
        $id=$_POST['id'];
        $cnt = User::model()->count(" card_no='".$id."'");
        if($cnt > 0)
        {
            $msg['status']=false;
            $msg['msg'] = "卡片已使用";
            print_r(json_encode($msg));
            exit;
        }
        $rs=Card::model()->deleteByPk($id);
        if($rs){
            $msg['status']=true;
        }else{
            $msg['status']=false;
        }
        
        //add log
        $log_args = array(
            'module_id'=>$this->module_id,
            'opt_name'=>'会员卡删除',
            'opt_detail'=>"会员卡号：".$id.".".($msg['status'] ? "会员卡删除成功。":"会员卡删除失败"),
            'opt_status'=>$msg['status'] ? "00":"01",
        );
        Systemlog::addLog($log_args);
        
        print_r(json_encode($msg));
    }
    public function actionDetail()
    {
        $id = $_POST['id'];
        $model =  Yii::app()->db->createCommand()
            ->select("*")
            ->from("g_court st")
            ->where("st.court_id='{$id}'")
            ->queryRow();
        $msg['status'] = true;
        if ($model) {
            $detail=array(
               '所在城市'=>$model['city'],
                '建立年代'=>$model['create_year'],
                '面积'=>$model['area'],
                '果岭草种'=>$model['green_grass'],
                '球场数据'=>$model['court_data'],
                '设计师'=>$model['designer'],
                '球道长度'=>$model['fairway_length'],
                '球道草种'=>$model['fairway_grass'],
                '球场设施'=>$model['facilities'],
                '球场简介'=>$model['remark'],
                '创建者'=> $model['creatorid'],
                '球场评价'=>'97分，8人点评，【设计:99,草坪:99,设施:97,服务:97】',
               
            );
            $msg['detail']=Utils::MakeDetailTable($detail);
        } else {
            $msg['status'] = false;
            $msg['detail'] = "获取球场信息失败！";
        }
        print_r(json_encode($msg));
    }
    
    
    private  function getAddrPoint($address)
    {
        if (!is_string($address))die("All Addresses must be passed as a string"); 
        //0MrPZA7fWlZeGSU1DPTCvolb
        $_url = sprintf('http://api.map.baidu.com/geocoder/v2/?address=%s&output=json&ak=0MrPZA7fWlZeGSU1DPTCvolb&callback=showLocation',rawurlencode($address)); 
        //var_dump($_url);
        $_result = false; 
        if($_result = file_get_contents($_url)) { 
            $_result = str_replace(array('showLocation&&showLocation(',')'), "", $_result);
            $result = (array)  json_decode($_result);
            //var_dump($result);
            if($result['status'] != 0) return false;
            $_match = $result['result']->location;
            $_coords['lat'] = $_match->lat; 
            $_coords['lon'] = $_match->lng; 
        } 
        return $_coords; 
    }
    
    public function actionShowPoint()
    {
        $addr = "广东省增城市永和镇余家庄水库";
        $coords = $this->getAddrPoint($addr);
        
        print_r($coords);
    }
    
    
    public function actionShowPic()
    {
        $id = trim($_GET['id']);
        $name=trim($_GET['name']);
        
        $_SESSION['cur_court_id'] = $id;
        $_SESSION['cur_court_name'] = $name;
        
        $this->render('pic_list');
    }
    
    /**
     * 表头
     * @return SimpleGrid
     */
    private function genPicDataGrid()
    {
        $t = new SimpleGrid($this->picGridId);
        $t->url = 'index.php?r=court/piclist';
        $t->updateDom = 'datagrid';
        $t->set_header('图片类型', '100', '');
        $t->set_header('图片', '100', '');   
        $t->set_header('操作', '100', '');
        return $t;
    }

    
    public function actionPicList()
    {
        $page = $_GET['page'] == '' ? 0 : $_GET['page']; //当前页码
        $_GET['page']=$_GET['page']+1;
        $args = $_GET['q']; //查询条件


        if ($_REQUEST['q_value'])
        {
            $args[$_REQUEST['q_by']] = $_REQUEST['q_value'];
        }

        $t = $this->genPicDataGrid();

        $list = Img::queryList($page, $this->pageSize, $args);

        $this->renderPartial('_piclist', array('t' => $t, 'rows' => $list['rows'], 'cnt' => $list['total_num'], 'curpage' => $list['page_num']));
    }
    
    
    public function actionNewPic()
    {
        $model=new Img('create');
        
        if($_POST['Img']){
            $model->attributes=$_POST['Img'];
            //先判断name是否重复
            if(!$this->checkName($_POST['Court']['name']))
            {
                $msg['msg']="添加失败！球场名称重复";
                $msg['status']=-1;
            }else
            {
                $model->record_time=date("Y-m-d H:i:s");
                
                try{
                    $rs=$model->save();
                    if($rs){
                        $msg['msg']="添加成功！";
                        $msg['status']=1;
                        $model=new Img('create');
                    }else{
                        $msg['msg']="添加失败！";
                        $msg['status']=-1;
                    }
                }catch (Exception $e){
                    if($e->errorInfo[0]==23000){
                        $msg['msg']="球场编号重复！";
                        $msg['status']=-1;
                    }

                }

                //add log
                $log_args = array(
                    'module_id'=>$this->module_id,
                    'opt_name'=>'球场图片添加',
                    'opt_detail'=>"球场编号：".$_SESSION['cur_court_id']."球场名称：".$_SESSION['cur_court_name'].".".$msg['msg'],
                    'opt_status'=>$msg['status']==1 ? "00":"01",
                );
                Operatorlog::addLog($log_args);
            }
            $model->relation_id = $_SESSION['cur_rout_id'];
        }
        $this->render("new_pic",array('model' => $model, 'msg' => $msg));
    }


}