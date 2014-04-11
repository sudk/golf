<?php
/*
 * 订单管理
 */
class OrderController extends BaseController
{

    public $defaultAction = 'list';
    public $gridId = 'list';
   
    public $pageSize = 100;
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
        $t->set_header('订单编号', '100', '');
        $t->set_header('订单类型', '70', '');   
        $t->set_header('下单人', '60', '');
        $t->set_header('商品名称', '100', '');
        $t->set_header('订单金额', '100', '');
        $t->set_header('支付方式', '100', '');
        $t->set_header('状态', '100', '');
        $t->set_header('下单时间', '100', '');
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
        $msg['status'] = true;
        if ($model) {
            $detail=array(
               '打球时间'=>$model['tee_time'],
                '人数'=>$model['count'],
                '单价'=>$model['unitprice'],
                '必须支付'=>$model['had_pay'],
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
	

}