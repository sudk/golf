<?php

/**
 * 消费记录报表
 *
 * @author guohao
 */
class ConsumeController extends AuthBaseController
{

    public $defaultAction = 'list';
    public $gridId = 'list';
    public $sGridId = 'summary_list';
    public $aGridId = 'agent_list';
    public $adGridId = 'agent_detail_list';
    
    public $oGridId = 'order_list';
    public $odGridId = 'order_detail_list';
    
    public $uGridId = 'user_list';
    public $udGridId = 'user_detail_list';
    
    public $bGridId = 'balance_list';
    public $bdGridId = 'balance_detail_list';
    
    public $pageSize = 20;

    /**
     * 表头
     * @return SimpleGrid
     */
    private function genDataGrid()
    {
        $t = new SimpleGrid($this->gridId);
        $t->url = 'index.php?r=rpt/consume/grid';
        $t->updateDom = 'datagrid';
        $t->set_header('序号', '5%', '');
        $t->set_header('交易类型', '10%', '');
        $t->set_header('订单类型', '10%', '');
        $t->set_header('付款类型', '10%', '');
        $t->set_header('流水号', '10%', '');
        $t->set_header('交易金额', '10%', '');
        $t->set_header('关联流水号', '10%', '');
        $t->set_header('客户', '10%', '');
        $t->set_header('交易状态', '10%', '');
        $t->set_header('记录时间', '10%', '');
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

        if(!$args['startdate']){
            $args['startdate']=date("Y-m-d");
        }

        if(!$args['enddate']){
            $args['enddate']=date("Y-m-d");
        }
        
        if($args['user_isdn'] == '用户手机号'){
            $args['user_isdn'] = "";
        }
        //print_r($args);
        $t = $this->genDataGrid();
        $this->saveUrl();

        $list = TransRecord::queryList($page, $this->pageSize, $args);

        $this->renderPartial('_list', array('t' => $t, 'rows' => $list['rows'], 'cnt' => $list['total_num'], 'curpage' => $list['page_num']));
    }

    /**
     * 保存查询链接
     */
    private function saveUrl()
    {
        $a = Yii::app()->session['list_url'];
        $a['rpt/school'] = str_replace("r=rpt/consume/grid", "r=rpt/consume/list", $_SERVER["QUERY_STRING"]);
        Yii::app()->session['list_url'] = $a;
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
        $recordtime= $_POST['recordtime'];
        $request =  SystemlogReq::GetReqByPk($id,$recordtime);
        $msg['status'] = true;
        if ($request) {
            $request=json_decode($request);
            //print_r($request);
            //$str=self::toStr($request);
            //$msg['detail']=$model;
            $msg['detail'] = $request;
        } else {
            $msg['status'] = false;
            $msg['detail'] = "获取操作明细失败！";
        }
        print_r(json_encode($msg));
    }

    private function toStr($request){

        try{
            foreach($request as $k => $v){
                try{
                    $str.=$k.":".$v."<br>";
                }catch (Exception $e){
                    $str.=self::toStr($v);
                }
            }
        }catch (Exception $e){
            print_r($e);
        }
        return $str;
    }
    
    
    
    /**
     * 表头
     * @return SimpleGrid
     */
    private function genSDataGrid()
    {
        $t = new SimpleGrid($this->sGridId);
        $t->url = 'index.php?r=rpt/consume/sgrid';
        $t->updateDom = 'datagrid';
        $t->set_header('代理商', '5%', '');
        
        $t->set_header('订单类型', '10%', '');
        $t->set_header('付款类型', '10%', '');   
        $t->set_header('交易类型', '10%', '');
        $t->set_header('收入金额', '10%', '');
        $t->set_header('交易笔数', '10%', '');
        return $t;
    }

    /**
     * 查询
     */
    public function actionSGrid()
    {
        $page = $_GET['page'] == '' ? 0 : $_GET['page']; //当前页码
        $_GET['page']=$_GET['page']+1;
        $args = $_GET['q']; //查询条件


        if ($_REQUEST['q_value'])
        {
            $args[$_REQUEST['q_by']] = $_REQUEST['q_value'];
        }

        if(!$args['startdate']){
            $args['startdate']=date("Y-m-d");
        }

        if(!$args['enddate']){
            $args['enddate']=date("Y-m-d");
        }
        
        if($args['user_isdn'] == '用户手机号'){
            $args['user_isdn'] = "";
        }
        //print_r($args);
        $t = $this->genSDataGrid();
        
        $list = TransRecord::querySummary($page, $this->pageSize, $args);

        $this->renderPartial('_summary', array('t' => $t, 'list' => $list));
    }

    

    /**
     * 列表
     */
    public function actionSummary()
    {
        $this->render('summary');
    }
    
    
    /**
     * 表头
     * @return SimpleGrid
     */
    private function genADataGrid()
    {
        $t = new SimpleGrid($this->aGridId);
        $t->url = 'index.php?r=rpt/consume/agrid';
        $t->updateDom = 'datagrid';
        $t->set_header('已完成订单', '15%', '');   
        $t->set_header('已订人数', '15%', '');
        $t->set_header('等待付款', '15%', '');   
        $t->set_header('平日成交额', '15%', '');   
        $t->set_header('假期成交额', '15%', '');   
        $t->set_header('总成交额', '15%', '');   
        return $t;
    }

    /**
     * 查询
     */
    public function actionAGrid()
    {
        $page = $_GET['page'] == '' ? 0 : $_GET['page']; //当前页码
        $_GET['page']=$_GET['page']+1;
        $args = $_GET['q']; //查询条件


        if ($_REQUEST['q_value'])
        {
            $args[$_REQUEST['q_by']] = $_REQUEST['q_value'];
        }

        if(!$args['startdate']){
            $args['startdate']=date("Y-m-01");
        }

        if(!$args['enddate']){
            $args['enddate']=date("Y-m-d");
        }
        
        //print_r($args);
        $t = $this->genADataGrid();
        
        $list = TransRecord::queryAgent($page, $this->pageSize, $args);

        $this->renderPartial('_agent', array('t' => $t, 'list' => $list));
    }

    

    /**
     * 列表
     */
    public function actionAgent()
    {
        $this->render('agent');
    }
    
    
    /**
     * 表头
     * @return SimpleGrid
     */
    private function genAdDataGrid()
    {
        $t = new SimpleGrid($this->adGridId);
        $t->url = 'index.php?r=rpt/consume/adgrid';
        $t->updateDom = 'datagrid';
        $t->set_header('球场', '16%', '');   
        $t->set_header('已完成订单', '14%', '');   
        $t->set_header('已订人数', '14%', '');
        $t->set_header('等待付款', '14%', '');   
        $t->set_header('平日成交额', '14%', '');   
        $t->set_header('假期成交额', '14%', '');   
        $t->set_header('总成交额', '14%', '');   
        return $t;
    }

    /**
     * 查询
     */
    public function actionAdGrid()
    {
        $page = $_GET['page'] == '' ? 0 : $_GET['page']; //当前页码
        $_GET['page']=$_GET['page']+1;
        $args = $_GET['q']; //查询条件

        if(!$args['startdate']){
            $args['startdate']=date("Y-m-01");
        }

        if(!$args['enddate']){
            $args['enddate']=date("Y-m-d");
        }
        
        //print_r($args);
        $t = $this->genAdDataGrid();
        
        $list = TransRecord::queryAgentD($page, $this->pageSize, $args);

        $this->renderPartial('_agentd', array('t' => $t, 'list' => $list));
    }

    

    /**
     * 列表
     */
    public function actionAgentd()
    {
        $this->render('agent_detail');
    }
    
    
    /**
     * 表头
     * @return SimpleGrid
     */
    private function genODataGrid()
    {
        $t = new SimpleGrid($this->oGridId);
        $t->url = 'index.php?r=rpt/consume/ogrid';
        $t->updateDom = 'datagrid';
        $t->set_header('完成消费', '9%', '');   
        $t->set_header('已付款', '9%', '');
        $t->set_header('已取消', '9%', '');   
        $t->set_header('等待确认', '9%', '');   
        $t->set_header('等待付款', '9%', '');   
        $t->set_header('等待撤销', '9%', '');  
        $t->set_header('已撤销', '9%', '');  
        $t->set_header('订单总数', '9%', '');  
        $t->set_header('平日', '9%', '');  
        $t->set_header('假日', '9%', ''); 
        $t->set_header('完成消费人数', '10%', '');  
        return $t;
    }

    /**
     * 查询
     */
    public function actionOGrid()
    {
        $page = $_GET['page'] == '' ? 0 : $_GET['page']; //当前页码
        $_GET['page']=$_GET['page']+1;
        $args = $_GET['q']; //查询条件


        if(!$args['startdate']){
            $args['startdate']=date("Y-m-01");
        }

        if(!$args['enddate']){
            $args['enddate']=date("Y-m-d");
        }
        
        //print_r($args);
        $t = $this->genODataGrid();
        
        $list = TransRecord::queryOrder($page, $this->pageSize, $args);

        $this->renderPartial('_order', array('t' => $t, 'list' => $list));
    }

    

    /**
     * 列表
     */
    public function actionOrder()
    {
        $this->render('order');
    }
    
    
    /**
     * 表头
     * @return SimpleGrid
     */
    private function genOdDataGrid()
    {
        $t = new SimpleGrid($this->odGridId);
        $t->url = 'index.php?r=rpt/consume/odgrid';
        $t->updateDom = 'datagrid';
        $t->set_header('球场', '12%', '');   
        $t->set_header('完成消费', '8%', '');   
        $t->set_header('已付款', '8%', '');
        $t->set_header('已取消', '8%', '');   
        $t->set_header('等待确认', '8%', '');   
        $t->set_header('等待付款', '8%', '');   
        $t->set_header('等待退款', '8%', '');  
        $t->set_header('退款中', '8%', '');  
        $t->set_header('订单总数', '8%', '');  
        $t->set_header('平日', '8%', '');  
        $t->set_header('假日', '8%', ''); 
        $t->set_header('完成消费人数', '9%', '');   
        return $t;
    }

    /**
     * 查询
     */
    public function actionOdGrid()
    {
        $page = $_GET['page'] == '' ? 0 : $_GET['page']; //当前页码
        $_GET['page']=$_GET['page']+1;
        $args = $_GET['q']; //查询条件

        if(!$args['startdate']){
            $args['startdate']=date("Y-m-01");
        }

        if(!$args['enddate']){
            $args['enddate']=date("Y-m-d");
        }
        
        //print_r($args);
        $t = $this->genOdDataGrid();
        
        $list = TransRecord::queryOrderD($page, $this->pageSize, $args);

        $this->renderPartial('_orderd', array('t' => $t, 'list' => $list));
    }

    

    /**
     * 列表
     */
    public function actionOrderd()
    {
        $this->render('order_detail');
    }
    
    
    /**
     * 表头
     * @return SimpleGrid
     */
    private function genUDataGrid()
    {
        $t = new SimpleGrid($this->uGridId);
        $t->url = 'index.php?r=rpt/consume/ugrid';
        $t->updateDom = 'datagrid';
        $t->set_header('完成订单', '9%', '');   
        $t->set_header('已订人数', '9%', '');
        $t->set_header('平日', '9%', '');   
        $t->set_header('假日', '9%', '');    
        return $t;
    }

    /**
     * 查询
     */
    public function actionUGrid()
    {
        $page = $_GET['page'] == '' ? 0 : $_GET['page']; //当前页码
        $_GET['page']=$_GET['page']+1;
        $args = $_GET['q']; //查询条件


        if(!$args['startdate']){
            $args['startdate']=date("Y-m-01");
        }

        if(!$args['enddate']){
            $args['enddate']=date("Y-m-d");
        }
        
        //print_r($args);
        $t = $this->genUDataGrid();
        
        $list = TransRecord::queryUser($page, $this->pageSize, $args);

        $this->renderPartial('_user', array('t' => $t, 'list' => $list));
    }

    

    /**
     * 列表
     */
    public function actionUser()
    {
        $this->render('user');
    }
    
    
    /**
     * 表头
     * @return SimpleGrid
     */
    private function genUdDataGrid()
    {
        $t = new SimpleGrid($this->udGridId);
        $t->url = 'index.php?r=rpt/consume/udgrid';
        $t->updateDom = 'datagrid';
        $t->set_header('客户', '12%', '');   
        $t->set_header('已定场次', '8%', '');   
        $t->set_header('平日场', '8%', '');
        $t->set_header('假日场', '8%', '');   
        $t->set_header('预订人数', '8%', '');   
        $t->set_header('所订球场', '8%', '');   
        $t->set_header('交易金额', '8%', '');  
        return $t;
    }

    /**
     * 查询
     */
    public function actionUdGrid()
    {
        $page = $_GET['page'] == '' ? 0 : $_GET['page']; //当前页码
        $_GET['page']=$_GET['page']+1;
        $args = $_GET['q']; //查询条件

        if(!$args['startdate']){
            $args['startdate']=date("Y-m-01");
        }

        if(!$args['enddate']){
            $args['enddate']=date("Y-m-d");
        }
        
        //print_r($args);
        $t = $this->genUdDataGrid();
        
        $list = TransRecord::queryUserD($page, $this->pageSize, $args);

        $this->renderPartial('_userd', array('t' => $t, 'list' => $list));
    }

    

    /**
     * 列表
     */
    public function actionUserd()
    {
        $this->render('user_detail');
    }
    
    
    /**
     * 表头
     * @return SimpleGrid
     */
    private function genBDataGrid()
    {
        $t = new SimpleGrid($this->bGridId);
        $t->url = 'index.php?r=rpt/consume/bgrid';
        $t->updateDom = 'datagrid';
        $t->set_header('订单总数', '8%', ''); 
        $t->set_header('完成订单', '8%', '');   
        $t->set_header('撤销订单', '8%', '');
        $t->set_header('已定人数', '8%', '');   
        $t->set_header('撤销人数', '8%', '');   
        $t->set_header('收款金额', '8%', '');   
        $t->set_header('退款金额', '8%', '');   
        $t->set_header('收款合计', '8%', '');   
        $t->set_header('银行手续费', '8%', '');  
        $t->set_header('完成消费人数', '8%', '');   
        $t->set_header('完成消费佣金', '9%', '');   
        $t->set_header('应结金额', '8%', '');   
        return $t;
    }

    /**
     * 查询
     */
    public function actionBGrid()
    {
        $page = $_GET['page'] == '' ? 0 : $_GET['page']; //当前页码
        $_GET['page']=$_GET['page']+1;
        $args = $_GET['q']; //查询条件


        if(!$args['startdate']){
            $args['startdate']=date("Y-m-01");
        }

        if(!$args['enddate']){
            $args['enddate']=date("Y-m-d");
        }
        
        //print_r($args);
        $t = $this->genBDataGrid();
        
        $list = TransRecord::queryBalance($page, $this->pageSize, $args);

        $this->renderPartial('_balance', array('t' => $t, 'list' => $list));
    }

    

    /**
     * 列表
     */
    public function actionBalance()
    {
        $this->render('balance');
    }
    
    
    /**
     * 表头
     * @return SimpleGrid
     */
    private function genBdDataGrid()
    {
        $t = new SimpleGrid($this->bdGridId);
        $t->url = 'index.php?r=rpt/consume/bdgrid';
        $t->updateDom = 'datagrid';
        $t->set_header('结算日期', '12%', '');   
        $t->set_header('订单总数', '9%', ''); 
        $t->set_header('完成订单', '9%', '');   
        $t->set_header('撤销订单', '9%', '');
        $t->set_header('已定人数', '9%', '');   
        $t->set_header('撤销人数', '9%', '');   
        $t->set_header('收款金额', '9%', '');   
        $t->set_header('退款金额', '9%', '');   
        $t->set_header('结余金额', '9%', '');   
        
        return $t;
    }

    /**
     * 查询
     */
    public function actionBdGrid()
    {
        $page = $_GET['page'] == '' ? 0 : $_GET['page']; //当前页码
        $_GET['page']=$_GET['page']+1;
        $args = $_GET['q']; //查询条件

        if(!$args['startdate']){
            $args['startdate']=date("Y-m-01");
        }

        if(!$args['enddate']){
            $args['enddate']=date("Y-m-d");
        }
        
        //print_r($args);
        $t = $this->genBdDataGrid();
        
        $list = TransRecord::queryBalanceD($page, $this->pageSize, $args);

        $this->renderPartial('_balanced', array('t' => $t, 'list' => $list));
    }

    

    /**
     * 列表
     */
    public function actionBalanced()
    {
        $this->render('balance_detail');
    }

}