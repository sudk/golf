<?php

/**
 * 特约商品推荐
 *
 * @author guohao
 */
class GoodsController extends AuthBaseController
{

    public $defaultAction = 'list';
    public $gridId = 'list';
    public $pageSize = 100;

    /**
     * 表头
     * @return SimpleGrid
     */
    private function genDataGrid()
    {
        $t = new SimpleGrid($this->gridId);
        $t->url = 'index.php?r=service/goods/grid';
        $t->updateDom = 'datagrid';
        $t->set_header('序号', '5%', '');
        $t->set_header('标题', '30%', '');
        $t->set_header('价格', '10%', '');
        
        $t->set_header('所在城市', '10%', '');
        $t->set_header('发布时间', '15%', '');
        $t->set_header('状态', '10%', '');
        $t->set_header('操作', '10%', '');
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

        if($args['title']=='标题')
        {
            $args['title'] = "";
        }
        
        if(Yii::app()->user->type == Operator::TYPE_AGENT  && Yii::app()->user->agent_id == '1')
        {
            $args['user_id'] = Yii::app()->user->agent_id;
        }
        

        $t = $this->genDataGrid();
        $this->saveUrl();

        $list = Flea::queryList($page, $this->pageSize, $args);

        $this->renderPartial('_list', array('t' => $t, 'rows' => $list['rows'], 'cnt' => $list['total_num'], 'curpage' => $list['page_num']));
    }

    /**
     * 保存查询链接
     */
    private function saveUrl()
    {
        $a = Yii::app()->session['list_url'];
        $a['rpt/school'] = str_replace("r=service/goods/grid", "r=service/goods/list", $_SERVER["QUERY_STRING"]);
        Yii::app()->session['list_url'] = $a;
    }

    /**
     * 列表
     */
    public function actionList()
    {
        $this->render('list');
    }
    
    
    public function actionAudit()
    {
        $id = trim($_POST['id']);
        //var_dump($id);
        $rs = Flea::audit($id);
        
        //var_dump($rs);
        //exit;
        if($rs==true)
        {
            $msg = array(
                'status'=>0,
                'msg'=>'审核成功'
            );
        }else{
            $msg = array(
                'status'=>-1,
                'msg'=>'审核失败，请重新尝试'
            );
        }
        
        print_r(json_encode($msg));exit;
    }
    
    
    public function actionDetail()
    {
        $id = trim($_GET['id']);
        
        $model = Flea::model()->findByPk($id);
        
        $this->render('detail',array('model'=>$model));
    }
    
    
    
    public function actionDel()
    {
        $id = trim($_POST['id']);
        
        $rs = Flea::model()->deleteByPk($id);
        if($rs)
        {
            //del img
            $img_rs = Img::delImg($id, Img::TYPE_FLEA);
            echo  json_encode(array('status'=>0));exit;
        }
        
        echo  json_encode(array('status'=>-1));exit;
    }
    
    private function checkName($name)
    {
        $cnt=  Flea::model()->count("title='".trim($name)."'");
            //print_r($operator);
        if($cnt > 0){
            return false;
        }else{
            return true;
        }
    }
    public function actionNewGoods(){
        $model=new Flea('create');
        if($_POST['Flea']){
            $model->attributes=$_POST['Flea'];
            
            //先判断name是否重复
            if(!$this->checkName($_POST['Flea']['title']))
            {
                $msg['msg']="添加失败！新闻名称重复";
                $msg['status']=-1;
            }else
            {
                $agent_id = Yii::app()->user->agent_id ;
                $agent_info = Agent::model()->find("id='{$agent_id}'");
                if(!$agent_info)
                {
                    $msg['msg']="添加失败！";
                    $msg['status']=-1;
                }else
                {
                    $model->title = $_POST['Flea']['title'];
                    $model->price = intval($_POST['Flea']['price'])*100;
                    $model->desc = $_POST['Flea']['desc'];
                    $model->record_time=date("Y-m-d H:i:s");
                    $model->status = '0';
                    $model->phone = $agent_info['phone'];
                    $model->contact = $agent_info['contactor'];
                    $model->user_id = Yii::app()->user->agent_id;
                    $model->check_id = Yii::app()->user->id;
                    $model->check_time=date("Y-m-d H:i:s");
                    $id = "O".date('YmdHis').rand(100000,999999);//官方商品
                    $model->id = $id;
                    //var_dump($_POST['Flea']);var_dump($model->attributes);exit;
                    try{
                        $rs=$model->save();
                        if($rs){
                            $msg['msg']="添加成功！";
                            $msg['status']=1;

                            $upload_img_msg = $this->uploadImgOfGoods($_FILES['upfile'],$id);
                            $msg['msg'] .=$upload_img_msg;

                            $model=new Flea('create');
                        }else{
                            $msg['msg']="添加失败！";
                            $msg['status']=-1;
                        }
                    }catch (Exception $e){
                        if($e->errorInfo[0]==23000){
                            $msg['msg']="未知错误！";
                            $msg['status']=-1;
                        }

                    }
                }
                

                
            }
        }
        $this->render("new",array('model' => $model, 'msg' => $msg));
    }
    
    /**
     * 上传赛事图片
     * @param type $files
     * @param type $relation_id
     * @return string
     */
    private  function uploadImgOfGoods($files,$relation_id)
    {
            $succ_num = 0;
            $false_num = 0;
            $up_msg = "";
            //$files = $_FILES['upfile'];
            $msg = "";
            if(isset($files))
            {
                //var_dump($files['error']);
                foreach($files['error'] as $k=>$v)
                {
                    if($v == 0)
                    {
                        //sleep(1);
                        //可以上传
                        $rs = Img::uploadImg($files['tmp_name'][$k],$files['name'][$k],$relation_id,Img::TYPE_FLEA);
                        //var_dump($rs);
                        if($rs['status'] == 0)
                        {
                            $up_msg .="第".($k+1)."张图片上传成功.";
                            $succ_num++;
                            
                        }else{
                            $up_msg .= "第".($k+1)."张图片上传失败.";
                            Img::delSimpleImg($rs['url']);
                            $false_num++;
                        }
                    }  else 
                    {
                        $up_msg .= "第".($k+1)."张图片上传失败.";
                        $false_num++;
                    }
                }
                
                $msg = $succ_num>0?"上传成功。":"上传失败。";
                $msg .= $succ_num>0?"成功数量:".$succ_num.",":"";
                $msg .= $false_num>0?"失败数量:".$false_num.",":"";
                $msg .= $up_msg;
                
            }
            return $msg;
    }
    
    public function actionCheckid(){
        $id=$_GET['id'];
        $data['status']=true;
        if($id){
            $cnt=  Flea::model()->count("title='".trim($id)."'");
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

}