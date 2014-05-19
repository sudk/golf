<?php

/**
 *
 *实体卡管理
 * @author guohao
 */
class CardController extends AuthBaseController
{

    public $defaultAction = 'list';
    public $gridId = 'list';
    public $pageSize = 100;
    public $module_id = 'user';

    /**
     * 表头
     * @return SimpleGrid
     */
    private function genDataGrid()
    {
        $t = new SimpleGrid($this->gridId);
        $t->url = 'index.php?r=user/card/grid';
        $t->updateDom = 'datagrid';
        $t->set_header('序号', '5%', '');
        $t->set_header('卡号', '45%', '');
        $t->set_header('卡名称', '30%', '');
        $t->set_header('操作', '20%', '');
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

        $list = Card::queryList($page, $this->pageSize, $args);

        $this->renderPartial('_list', array('t' => $t, 'rows' => $list['rows'], 'cnt' => $list['total_num'], 'curpage' => $list['page_num']));
    }

    /**
     * 列表
     */
    public function actionList()
    {
        $this->render('list');
    }

    public function actionNew(){
        $model=new Card('create');
        if($_POST['Card']){
            $model->attributes=$_POST['Card'];
            
            $model->record_time=date("Y-m-d H:i:s");
            $model->desc=htmlspecialchars($_POST['Card']['desc']);
            
            try{
                $rs=$model->save();
                if($rs){
                    $msg['msg']="添加成功！";
                    $msg['status']=1;
                    $model=new Card('create');
                }else{
                    $msg['msg']="添加失败！";
                    $msg['status']=-1;
                }
            }catch (Exception $e){
                if($e->errorInfo[0]==23000){
                    $msg['msg']="会员卡号重复！";
                    $msg['status']=-1;
                }
                
            }
            
            //add log
            $log_args = array(
                'module_id'=>$this->module_id,
                'opt_name'=>'会员卡添加',
                'opt_detail'=>"会员卡号：".$model->id.".".$msg['msg'],
                'opt_status'=>$msg['status']==1 ? "00":"01",
            );
            Operatorlog::addLog($log_args);
        }
        $this->render("new",array('model' => $model, 'msg' => $msg));
    }

    public function actionEdit(){
        $id=$_GET['id'];
        $model=Card::model()->findByPk($id);
        if($_POST['Card']){
            
            $model->setScenario("modify");
            $model->attributes=$_POST['Card'];
            $model->desc=htmlspecialchars($_POST['Card']['desc']);
            
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
            Operatorlog::addLog($log_args);

        }
       //var_dump($msg);
        $this->layout = '//layouts/base';
        $this->render("edit",array('model' => $model, 'msg' => $msg));
    }

 
    public function actionCheckid(){
        $id=$_GET['id'];
        $data['status']=true;
        if($id){
            $model=Card::model()->findByPk($id);
            //print_r($operator);
            if($model){
                $data['msg']=2;
            }else{
                $data['msg']=0;
            }
        }else{
            $data['status']=false;
        }
        print_r(json_encode($data));
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
        Operatorlog::addLog($log_args);
        
        print_r(json_encode($msg));
    }
    public function actionDetail()
    {
        $id = $_POST['id'];
        $model =  Yii::app()->db->createCommand()
            ->select("desc,record_time")
            ->from("g_card st")
            ->where("st.id='{$id}'")
            ->queryRow();
        $msg['status'] = true;
        if ($model) {
            $detail=array(
               
                '备注'=>$model['desc'],
                '导入时间'=>$model['record_time']
            );
            $msg['detail']=Utils::MakeDetailTable($detail);
        } else {
            $msg['status'] = false;
            $msg['detail'] = "获取会员卡信息失败！";
        }
        print_r(json_encode($msg));
    }
    
    
    
    public function actionBatch(){
        $model=new Card('create');
        $tmp_file = $_FILES['upfile'];
        if(isset($tmp_file)){
            
            $data = new JPhpExcelReader($tmp_file['tmp_name']);
            $count = $data->rowcount(0);
            $succ_cnt = 0;
            $error_cnt = 0;
            $err_msg = "";
            for ($i = 3; $i <= $count; $i++) {
                $card = trim($data->val($i, 1, 0));
                $name = trim($data->val($i,2,0));
               

                
                if($card==""){
                    continue;
                }
                
                $model = new Card();
                $model->id = $card;
                $model->card_name = $name;
                $model->record_time = date('Y-m-d H:i:s');
                $rs = $model->save();
                if($rs)
                {
                    $succ_cnt++;
                }else{
                    $error_cnt++;
                    $err_msg .= "卡号为".$card."添加失败。";
                }
                
            }
            $msg['msg'] = "会员卡添加成功数量：".$succ_cnt.";失败数量:".$error_cnt.";".$err_msg;
            if($succ_cnt > 0)
            {
                $msg['status'] = 1;
            }else{
                $msg['status'] = -1;
            }
          
        }
        $this->render("batch",array('model' => $model, 'msg' => $msg));
    }
    
    /**
     * 下载模板
     */
    public function actionDownTemplate()
    {
        $_file = Yii::app()->params['template_dir']."user_card_template.xls";
        $file = fopen($_file,"r");
        Header("Content-type: application/octet-stream");
        Header("Accept-Ranges: bytes");
        Header("Accept-Length: ".filesize($_file));
        Header("Content-Disposition: attachment; filename=" . basename($_file));
        echo fread($file,filesize($_file));
        fclose($file);
    }



}