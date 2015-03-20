<?php
/*
 * 定制行程
 */
class CtripController extends CController
{

    public $layout='//layouts/mbase';
    public $defaultAction = 'list';
    public $gridId = 'list';
    public $pageSize = 20;
    

    /**
     * 表头
     * @return SimpleGrid
     */
    private function genDataGrid()
    {
        $t = new SimpleGrid($this->gridId);
        $t->url = 'index.php?r=trip/ctrip/grid';
        $t->updateDom = 'datagrid';
        $t->set_header('编号', '15%', '');
        $t->set_header('地区', '10%', '');
        $t->set_header('球场', '8%', '');
        $t->set_header('联系人', '15%', '');
        $t->set_header('联系电话', '8%', '');
        $t->set_header('参加人数', '8%', '');
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

        $list = TripCustom::queryList($page, $this->pageSize, $args);

        $this->renderPartial('_list', array('t' => $t, 'rows' => $list['rows'], 'cnt' => $list['total_num'], 'curpage' => $list['page_num']));
    }

    /**
     * 列表
     */
    public function actionList()
    {
        $this->layout='//layouts/main';
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
                '最晚付款时间'=>$model['last_pay_time'],
                '特别说明'=>$model['special_node'],
                    
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
            $model->last_pay_time = trim($_POST['Order']['last_pay_time']);
            
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

    public function actionAdd(){
        $this->layout = '//layouts/mbase';
        if($_POST['court_name']){
            $msg=array('desc'=>'行程添加成功，等确认！','status'=>'0');
            if(!trim($_GET['u_id'])){
                $msg=array('desc'=>'缺少参数','status'=>'2');
            }else{
                $model=new TripCustom();
                $model->id=uniqid("ctrip");
                $model->court_name=$_POST['court_name'];
                $model->area=$_POST['area'];
                $model->m_count=$_POST['m_count'];
                $model->d_count=$_POST['d_count'];
                $model->n_count=$_POST['n_count'];
                $model->f_count=$_POST['f_count'];
                $model->hotel_type=$_POST['hotel_type'];
                $model->hotel_star=$_POST['hotel_star'];
                $model->live_count=$_POST['live_count'];
                $model->room_type=$_POST['room_type'];
                $model->room_count=$_POST['room_count'];
                $model->desc=$_POST['desc'];
                $model->car_require=$_POST['car_require'];
                $model->car_type=$_POST['car_type'];
                $model->contact=$_POST['contact'];
                $model->phone=$_POST['phone'];
                $model->remark=$_POST['remark'];
                $model->tee_time=implode("|",$_POST['tee_time']);
                $model->user_id=$_GET['u_id'];
                $rs=$model->save();
                if(!$rs){
                    $msg['status']=1;
                    $msg['desc']="行程添加失败请重试！";
                }
            }

            $this->render("add_rs",array('msg'=>$msg));
        }else{
            $court=Court::getCourtArray();
            $this->render("add",array('court'=>$court));
        }
    }

}