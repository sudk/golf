<?php

/**
 *
 *会员信息管理
 * @author guohao
 */
class UinfoController extends AuthBaseController
{

    public $defaultAction = 'list';
    public $gridId = 'list';
    public $cardGridId = 'card_list';
    public $pageSize = 100;
    public $module_id = 'user';

    /**
     * 表头
     * @return SimpleGrid
     */
    private function genDataGrid()
    {
        $t = new SimpleGrid($this->gridId);
        $t->url = 'index.php?r=user/uinfo/grid';
        $t->updateDom = 'datagrid';
       
        $t->set_header('姓名', '10%', '');
        $t->set_header('电话', '8%', ''); 
        $t->set_header('账户余额', '8%', '','');
        $t->set_header('账户积分', '8%', '','');
        $t->set_header('所在城市', '8%', '','');
        $t->set_header('状态', '8%', '');
        $t->set_header('VIP状态', '8%', '');
        $t->set_header('VIP有效期', '10%', '');
        $t->set_header('会员卡号', '12%', '');
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
        //var_dump($args);
        $t = $this->genDataGrid();

        $list = User::queryList($page, $this->pageSize, $args);

        $this->renderPartial('_list', array('t' => $t, 'rows' => $list['rows'], 'cnt' => $list['total_num']));
    }

    /**
     * 列表
     */
    public function actionList()
    {
        $this->render('list');
    }
    
    
    
    
    


    public function actionEdit(){
        $id=$_GET['id'];
        $model=User::model()->findByPk($id);
        if($_POST['User']){
            
            $model->setScenario("modify");
            $model->attributes=$_POST['User'];
            $model->remark=htmlspecialchars($_POST['User']['remark']);
            
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
                'opt_name'=>'会员信息编辑',
                'opt_detail'=>"会员姓名：".$model->user_name.",".$msg['msg'],
                'opt_status'=>$msg['status'] == 0? "00":"01",
            );
            Operatorlog::addLog($log_args);

        }
        
        $this->layout = '//layouts/base';
        $this->render("edit",array('model' => $model, 'msg' => $msg));
    }

    
    public function actionResetPwd()
    {
        $id=$_POST['id'];
        $model=User::model()->findByPk($id);
        $name = $model->user_name;
        $model->passwd = "666666";
        $rs = $model->save();
        if($rs){
            $msg['status']=true;
        }else{
            $msg['status']=false;
        }
        //add log
        $log_args = array(
            'module_id'=>$this->module_id,
            'opt_name'=>'会员密码重置',
            'opt_detail'=>"会员姓名：".$name.".".($msg['status'] ? "密码重置成功。":"密码重置失败"),
            'opt_status'=>$msg['status'] ? "00":"01",
        );
        Operatorlog::addLog($log_args);
        print_r(json_encode($msg));
    }
    
    public function actionDel(){
        $id=$_POST['id'];
        $info = User::model()->findByPk($id);
        $name = $info->user_name;
        $rs=User::model()->deleteByPk($id);
        if($rs){
            $msg['status']=true;
        }else{
            $msg['status']=false;
        }
        
        //add log
        $log_args = array(
            'module_id'=>$this->module_id,
            'opt_name'=>'会员删除',
            'opt_detail'=>"会员姓名：".$name.".".($msg['status'] ? "会员删除成功。":"会员删除失败"),
            'opt_status'=>$msg['status'] ? "00":"01",
        );
        Operatorlog::addLog($log_args);
        print_r(json_encode($msg));
    }
    public function actionDetail()
    {
        $id = $_POST['id'];
        $model =  Yii::app()->db->createCommand()
            ->select("*")
            ->from("g_user st")
            ->where("st.user_id='{$id}'")
            ->queryRow();
            //var_dump(intval($model['sex']));
        $msg['status'] = true;
        if ($model) {
            $detail=array(
                'E-Mail'=>$model['email'],
                '性别'=>User::GetSex(intval($model['sex'])),
                '备注'=>$model['remark'],
                '注册时间'=>$model['record_time']
            );
            $msg['detail']=Utils::MakeDetailTable($detail);
        } else {
            $msg['status'] = false;
            $msg['detail'] = "获取会员信息失败！";
        }
        print_r(json_encode($msg));
    }
    
    
    public function actionMyCard()
    {
        $id = trim($_GET['id']);
        $name=trim($_GET['name']);
        
        $_SESSION['cur_user_id'] = $id;
        $_SESSION['cur_user_name'] = $name;
        
        $this->render('card_list');
    }
    
    /**
     * 表头
     * @return SimpleGrid
     */
    private function genCardDataGrid()
    {
        $t = new SimpleGrid($this->cardGridId);
        $t->url = 'index.php?r=user/uinfo/cardlist';
        $t->updateDom = 'datagrid';
        $t->set_header('卡号', '20%', '');
        $t->set_header('卡名称', '20%', '');   
        $t->set_header('卡图片', '40%', '');   
        $t->set_header('操作', '20%', '');
        return $t;
    }

    
    public function actionCardList()
    {
        $page = $_GET['page'] == '' ? 0 : $_GET['page']; //当前页码
        $_GET['page']=$_GET['page']+1;
        $args = $_GET['q']; //查询条件


        if ($_REQUEST['q_value'])
        {
            $args[$_REQUEST['q_by']] = $_REQUEST['q_value'];
        }

        $t = $this->genCardDataGrid();
        
        if(!isset($args['user_id']))
        {
            $args['user_id'] = $_SESSION['cur_user_id'];
        }
        
        
        //var_dump($args);
        $list = Card::queryList($page, $this->pageSize, $args);

        $this->renderPartial('_cardlist', array('t' => $t, 'rows' => $list['rows'], 'cnt' => $list['total_num'], 'curpage' => $list['page_num']));
    }
    
    
    public function actionNewCard() {
        $model = new Card('create');
        $id = trim($_REQUEST['id']);
        if ($_POST['Card']) {
            $model->attributes = $_POST['Card'];
            
            $model->record_time = date("Y-m-d H:i:s");
            $model->id = date("YmdHis").  rand(100000, 999999);

            try {
                $rs = $model->save();
                if ($rs) {
                    $msg['msg'] = "添加成功！";
                    $msg['status'] = 1;

                    $file = $_FILES['card_img'];
                    if (is_uploaded_file($file['tmp_name'])) {
                        $upload_rs = Img::uploadImg($file['tmp_name'], $file['name'], $model->id, Img::TYPE_CARD);
                        if ($upload_rs['status'] != 0) {
                            $msg['msg'] .= "图片上传失败。";
                        }
                        
                    }



                    $model = new Card('create');
                } else {
                    $msg['msg'] = "添加失败！";
                    $msg['status'] = -1;
                }
            } catch (Exception $e) {
                if ($e->errorInfo[0] == 23000) {
                    $msg['msg'] = "未知错误！";
                    $msg['status'] = -1;
                }
            }
        }else{
            $model->user_id = $id;
        }
        $this->render("new_card", array('model' => $model, 'msg' => $msg));
    }


    /**
     * 商户删除
     * 包括 
     * 商户信息
     * 球场的图片
     */
    public function actionDelCard() {
        $id = $_POST['id'];
        $model = Card::model()->findByPk($id);
        //先删除
        $rs = $model->deleteByPk($id);
        if ($rs) {
            //del img
            $img_rs = Img::delImg($id, Img::TYPE_CARD);
            $msg['status'] = true;
        } else {
            $msg['status'] = false;
        }

        print_r(json_encode($msg));
    }



}