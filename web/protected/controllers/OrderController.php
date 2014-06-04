<?php
/*
 * 订单管理
 */
class OrderController extends AuthBaseController
{

    public $defaultAction = 'list';
    public $gridId = 'list';
    public $lGridId = 'log_list';
   
    public $pageSize = 20;
    public $module_id = 'order';
    
    

    /**
     * 表头
     * @return SimpleGrid
     */
    private function genDataGrid()
    {
        $t = new SimpleGrid($this->gridId);
        $t->url = 'index.php?r=order/grid';
        $t->updateDom = 'datagrid';
        $t->set_header('订单编号', '15%', '');
        $t->set_header('订单类型', '10%', '');   
        $t->set_header('下单人', '8%', '');
        $t->set_header('商品名称', '15%', '');
        $t->set_header('订单金额', '8%', '');
        $t->set_header('支付方式', '8%', '');
        $t->set_header('状态', '8%', '');
        $t->set_header('下单时间', '8%', '');
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
        
        if($args['order_id'] == "订单编号"){
            $args['order_id'] = "";
        }

        $t = $this->genDataGrid();
        
        if(Yii::app()->user->type == Operator::TYPE_AGENT)
        {
            $args['agent_id'] = Yii::app()->user->agent_id;
        }

        $list = Order::queryList($page, $this->pageSize, $args);

        $this->renderPartial('_list', array('t' => $t, 'rows' => $list['rows'], 'cnt' => $list['total_num'], 'curpage' => $list['page_num']));
    }

    /**
     * 列表
     */
    public function actionList()
    {
        $this->render('list');
    }

    
    public function actionDetail()
    {
        $id = $_POST['id'];
        $model =  Yii::app()->db->createCommand()
            ->select("*")
            ->from("g_order st")
            ->where("st.order_id='{$id}'")
            ->queryRow();
            
        $court = Yii::app()->db->createCommand()
                ->select('phone')
                ->from('g_court t')
                ->where("t.court_id='{$model['relation_id']}'")
                ->queryRow();
                
        
         
        $msg['status'] = true;
        if ($model) {
            $detail=array(
                '球场电话'=>$court['phone'],
                '客户电话'=>$model['phone'],
               '打球时间'=>$model['tee_time'],
                '人数'=>$model['count'],
                '单价'=>(intval($model['unitprice'])/100)."元",
                '实付'=>(intval($model['had_pay'])/100)."元",
                '支付渠道'=>$model['pay_method']?Order::getPayMethod($model['pay_method']):"",
                '备注'=>$model['desc'],
                    
            );
            //还要把状态修改的过程展示在这里
            $msg['detail']=Utils::MakeDetailTable($detail);
        } else {
            $msg['status'] = false;
            $msg['detail'] = "获取球场信息失败！";
        }
        print_r(json_encode($msg));
    }
    
    
    public function actionEdit()
    {
        $id = trim($_GET['id']);
        
        $model=Order::model()->findByPk($id);
        
        if($_POST['Order'])
        {
            $model->attributes = $_POST['Order'];
            $model->amount = intval($_POST['Order']['amount'])*100;
            $model->contact = trim($_POST['Order']['contact']);
            $model->phone = trim($_POST['Order']['phone']);
            $model->count = trim($_POST['Order']['count']);
            $model->tee_time = trim($_POST['Order']['tee_time']);
            
            $rs = $model->save();
            if($rs){
                $msg['msg']="修改成功！";
                $msg['status']=1;
                //修改完订单内容，需要记录日志
                $serial_number = Utils::GenerateSerialNumber();
                
                OrderLog::Add($model->order_id, $serial_number);
                //$model=new Staff('modify');
            }else{
                $msg['msg']="修改失败！";
                $msg['status']=0;
            }
        }
        $model->amount = intval($model->amount)/100;
        $model->unitprice = intval($model->unitprice)/100;
        
        $this->layout = '//layouts/base';
        $this->render("edit",array('model' => $model, 'msg' => $msg));
    }
    
    
    /**
     * 表头
     * @return SimpleGrid
     */
    private function genLDataGrid()
    {
        $t = new SimpleGrid($this->lGridId);
        $t->url = 'index.php?r=order/lgrid';
        $t->updateDom = 'datagrid';
        $t->set_header('编号', '5%', '');
        $t->set_header('记录时间', '20%', '');
        //$t->set_header('订单编号', '15%', '');
        $t->set_header('状态', '15%', '');   
//        $t->set_header('操作人', '20%', '');
//        $t->set_header('操作类型', '20%', '');
        $t->set_header('流水号', '20%', '');
        $t->set_header('备注', '40%', '');
        
        return $t;
    }

    /**
     * 查询
     */
    public function actionLGrid()
    {
        $page = $_GET['page'] == '' ? 0 : $_GET['page']; //当前页码
        $_GET['page']=$_GET['page']+1;
        $args = $_GET['q']; //查询条件


        if ($_REQUEST['q_value'])
        {
            $args[$_REQUEST['q_by']] = $_REQUEST['q_value'];
        }
        
        if(isset($_SESSION['cur_order_log']))
        {
            $args['order_id'] = $_SESSION['cur_order_log'];
        }
        //var_dump($args);
        $t = $this->genLDataGrid();

        $list = OrderLog::queryList($page, $this->pageSize, $args);

        $this->renderPartial('_loglist', array('t' => $t, 'rows' => $list['rows'], 'cnt' => $list['total_num'], 'curpage' => $list['page_num']));
    }

    /**
     * 列表
     */
    public function actionLog()
    {
        unset($_SESSION['cur_order_log']);
        $order_id = trim($_GET['id']);
        $_SESSION['cur_order_log'] = $order_id;
        $this->render('log_list',array('order_id'=>$order_id));
    }
    
    
    public function actionNextStatus()
    {
        $id=trim($_REQUEST['id']);
        $now_status = trim($_REQUEST['ns']);
        $next_status = trim($_REQUEST['s']);
        $pay_type = trim($_REQUEST['type']);
        $model = Order::model()->findByPk($id);
        $model->next_status = $next_status;
        if(isset($_POST['Order']))
        {
            $model->attributes = $_POST['Order'];
            
            $now_status = $_POST['Order']['status'];
            $next_status = $_POST['Order']['next_status'];
            
            if($now_status == Order::STATUS_WAIT_REFUND && $next_status == Order::STATUS_REFUND)
            {
                //走退款流程  
                
                $order_id = $_POST['Order']['order_id'];
                
                //$sn = "";

                $refund = intval($_POST['Order']['refund'])*100;
                //var_dump($sn);var_dump($_POST['Order']['order_id']);var_dump($refund);exit;
                $rs = Order::Refund($order_id,$refund);
                //var_dump($rs);
                if($rs['status'] == 0){

                    $msg['msg']="操作成功！";
                    $msg['status']=1;
                    //add log
                    //OrderLog::Add($order_id, $sn);
                }else{
                    $msg['msg']="操作失败！".$rs['desc'];
                    $msg['status']=-1;
                }
                
                
            }else
            {
                //其他情况下，直接修改order 订单内容，然后 添加日志。
                $model->status = $next_status;
                $model->desc = $_POST['Order']['desc'];
                $rs = $model->save();
                
                if($rs){
                    $msg['msg']="操作成功！";
                    $msg['status']=1;
                    //修改完订单内容，需要记录日志
                    $serial_number = Utils::GenerateSerialNumber();

                    OrderLog::Add($model->order_id, $serial_number);
                    //$model=new Staff('modify');
                }else{
                    $msg['msg']="操作失败！";
                    $msg['status']=-1;
                }
            }
        }
        $this->layout = '//layouts/base';
        $this->render("next_status",array('model' => $model, 'msg' => $msg));
    }
    
    /**
     * 修改订单状态
     * 主要是只修改状态，不用填写新的信息。
     */
    public function actionConfirmStatus()
    {
        $id=trim($_POST['id']);
        $now_status = trim($_POST['ns']);
        $next_status = trim($_POST['s']);
        $pay_type = trim($_POST['type']);
        
        
        $rs = Order::dealOrderStatus($id,$now_status,$next_status,$pay_type);
        
       
        print_r(json_encode($rs));
    }
    
    /**
     * 保留删除功能
     */
    public function actionDel()
    {
        $id=$_POST['id'];
        $rs = true;
        if($rs){
            $msg['status']=true;
        }else{
            $msg['status']=false;
        }
       
        print_r(json_encode($msg));
    }
	

}