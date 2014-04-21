<?php
/*
 * 价格管理
 */
class PriceController extends BaseController
{

    public $defaultAction = 'list';
    public $gridId = 'list';
    public $pGridId = 'policy_list';
    public $cGridId = 'custom_list';
    public $sGridId = 'special_list';
    public $pageSize = 100;
    public $module_id = 'price';
    
    

    /**
     * 表头
     * @return SimpleGrid
     */
    private function genDataGrid()
    {
        $t = new SimpleGrid($this->gridId);
        $t->url = 'index.php?r=price/grid';
        $t->updateDom = 'datagrid';
     
        $t->set_header('球场', '100', '');
        $t->set_header('服务项目', '70', '');
        $t->set_header('周一至周日', '150', ''); 
              
        $t->set_header('状态', '50', '');
        $t->set_header('正常日报价', '70', '');
        $t->set_header('优惠报价', '70', '');
        $t->set_header('特殊日报价', '70', '');
        $t->set_header('有效期', '70', '');
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
        $month = $args['month']==null?date('Y-m'):$args['month'];
        $t = $this->genDataGrid();

        $list = Court::queryList($page, $this->pageSize, $args);

        $this->renderPartial('_list', array('t' => $t, 'rows' => $list['rows'], 'cnt' => $list['total_num'], 'month' => $month));
    }

    /**
     * 列表
     */
    public function actionList()
    {
        $this->render('list');
    }
    
    
    public function actionNewPolicy(){
        $court_id = trim($_GET['id']);
        $type = trim($_GET['tag']);//创建报价单类型
        $model=new Policy('create');
        if($_POST['Policy']){
            
            //用事务来处理
            $type = $_POST['Policy']['type'];
            $court_id = $model->court_id;
            if($type == Policy::TYPE_NORMAL)
            {//普通报价
                //var_dump("insert".$type);
                $one_price = $_POST['1_price'];
                $two_price = $_POST['2_price'];
                $three_price = $_POST['3_price'];
                $four_price = $_POST['4_price'];
                $five_price = $_POST['5_price'];
                $sex_price = $_POST['6_price'];
                $seven_price = $_POST['7_price'];
                
                if($one_price==""||$two_price==""||$three_price==""||$four_price==""||$five_price==""||$sex_price==""||$seven_price==""){
                    $msg['msg']="每天的默认价格必填！";
                    $msg['status']=-1;
                }else{
                    $week_day = array();
                  
                    for($i = 1; $i <=7 ; $i++)
                    {
                        $week_day[$i] = array();
                        $day = array(
                            'day'=>$i."",
                            'start_time'=>'',
                            'end_time'=>'',
                            'price'=>$_POST[$i.'_price'],
                            'status'=>$_POST[$i."_status"]
                        );
                        array_push($week_day[$i], $day);
                        if(isset($_POST[$i.'_start_time']))
                        {
                            //print_r($_POST[$i.'_start_time']);
                            foreach($_POST[$i.'_start_time'] as $k=>$row)
                            {
                                $tmp = array(
                                    'day'=>$i."",
                                    'start_time'=>$row,
                                    'end_time'=>$_POST[$i."_end_time"][$k],
                                    'price'=>$_POST[$i.'_d_price'][$k],
                                    'status'=>$_POST[$i.'_status'],
                                    'record_time'=>date('Y-m-d H:i:s')
                                );
                                //var_dump($tmp);
                                array_push($week_day[$i],$tmp);
                            }
                        }
                    }
                    //得到所有Policy的内容
                    $policy = $_POST['Policy'];
                    $policy['status'] = Policy::STATUS_NORMAL;
                    $policy['record_time']=date("Y-m-d H:i:s");
                    $policy['creatorid'] = Yii::app()->user->id;
                    $policy['agent_id'] = Yii::app()->user->agent_id;
                    $policy['id'] = $model->agent_id.date("YmdHis").rand(1000,9999);
                    //var_dump($week_day);
                    $msg = Policy::insertRecord($policy, $week_day);
                    $model = new Policy('create');
                }
            }else if($type == Policy::TYPE_FOVERABLE)
            {//优惠报价
                $week_day = array();
                  
                for($i = 1; $i <=7 ; $i++)
                {
                    $week_day[$i] = array();
                    if($_POST[$i."_price"] !="")
                    {
                        $day = array(
                            'day'=>$i."",
                            'start_time'=>'',
                            'end_time'=>'',
                            'price'=>$_POST[$i.'_price'],
                            'status'=>$_POST[$i."_status"]
                        );
                        array_push($week_day[$i], $day);
                    }
                    if(isset($_POST[$i.'_start_time']))
                    {
                        //print_r($_POST[$i.'_start_time']);
                        foreach($_POST[$i.'_start_time'] as $k=>$row)
                        {
                            $tmp = array(
                                'day'=>$i."",
                                'start_time'=>$row,
                                'end_time'=>$_POST[$i."_end_time"][$k],
                                'price'=>$_POST[$i.'_d_price'][$k],
                                'status'=>$_POST[$i.'_status'],
                                'record_time'=>date('Y-m-d H:i:s')
                            );
                            //var_dump($tmp);
                            array_push($week_day[$i],$tmp);
                        }
                    }
                }
                if(@count($week_day) == 0)
                {
                    $msg['msg']="请提交详细报价！";
                    $msg['status']=-1;
                }else{
                    //得到所有Policy的内容
                    $policy = $_POST['Policy'];
                    $policy['status'] = Policy::STATUS_NORMAL;
                    $policy['record_time']=date("Y-m-d H:i:s");
                    $policy['creatorid'] = Yii::app()->user->id;
                    $policy['agent_id'] = Yii::app()->user->agent_id;
                    $policy['id'] = $model->agent_id.date("YmdHis").rand(1000,9999);
                    //var_dump($week_day);
                    $msg = Policy::insertRecord($policy, $week_day);
                }
                           
                $model = new Policy('create');
            }else if($type == Policy::TYPE_SPECIAL)
            {
                //特殊报价
                $week_day = array();
                $default_price = $_POST['default_price'];
                if($default_price == ""){
                    $msg['msg']="请提交详细报价！";
                    $msg['status']=-1;
                }else
                {
                    
                    $week_day['-1'][] = array(
                        'day'=>"-1",
                        'start_time'=>"",
                        'end_time'=>"",
                        'price'=>$default_price,
                        'status'=>$_POST['default_status'],
                        'record_time'=>date('Y-m-d H:i:s')
                    );
                    $policy = $_POST['Policy'];
                    $policy['status'] = Policy::STATUS_NORMAL;
                    $policy['record_time']=date("Y-m-d H:i:s");
                    $policy['creatorid'] = Yii::app()->user->id;
                    $policy['agent_id'] = Yii::app()->user->agent_id;
                    $policy['id'] = $model->agent_id.date("YmdHis").rand(1000,9999);
                    //var_dump($week_day);
                    $msg = Policy::insertRecord($policy, $week_day);
                    $model = new Policy('create');
                }
            }else
            {
                $msg['msg']="添加失败，报价单类型不存在！";
                $msg['status']=-1;
            }
            //var_dump($_POST);//exit;
            
        }
            
        $model->is_green = '1';
        $model->is_caddie = '1';
        $model->is_car = '1';
        $model->is_wardrobe = '1';
        $model->is_meal = '1';
        $model->is_insurance = '1';
        $model->is_tip = '1';
        $model->type = $type;//Policy::TYPE_NORMAL;
        $model->pay_type = '1';
        
        $model->court_id = $court_id;
        
            
        $this->layout = '//layouts/base';
        $this->render("new",array('model' => $model, 'msg' => $msg));
    }

    public function actionEdit(){
        $id=$_GET['id'];
        $type = $_GET['tag'];
        $model=Policy::model()->findByPk($id);
        
        //var_dump($model->attributes);
        if($_POST['Policy']){
            
            //用事务来处理
            $type = $_POST['Policy']['type'];
            $court_id = $_POST['Policy']['court_id'];
            if($type == Policy::TYPE_NORMAL)
            {//普通报价
                //var_dump("insert".$type);
                $one_price = $_POST['1_price'];
                $two_price = $_POST['2_price'];
                $three_price = $_POST['3_price'];
                $four_price = $_POST['4_price'];
                $five_price = $_POST['5_price'];
                $sex_price = $_POST['6_price'];
                $seven_price = $_POST['7_price'];
                
                if($one_price==""||$two_price==""||$three_price==""||$four_price==""||$five_price==""||$sex_price==""||$seven_price==""){
                    $msg['msg']="每天的默认价格必填！";
                    $msg['status']=-1;
                }else{
                    $week_day = array();
                  
                    for($i = 1; $i <=7 ; $i++)
                    {
                        $week_day[$i] = array();
                        $day = array(
                            'day'=>$i."",
                            'start_time'=>'',
                            'end_time'=>'',
                            'price'=>$_POST[$i.'_price'],
                            'status'=>$_POST[$i."_status"]
                        );
                        array_push($week_day[$i], $day);
                        if(isset($_POST[$i.'_start_time']))
                        {
                            //print_r($_POST[$i.'_start_time']);
                            foreach($_POST[$i.'_start_time'] as $k=>$row)
                            {
                                $tmp = array(
                                    'day'=>$i."",
                                    'start_time'=>$row,
                                    'end_time'=>$_POST[$i."_end_time"][$k],
                                    'price'=>$_POST[$i.'_d_price'][$k],
                                    'status'=>$_POST[$i.'_status'],
                                    'record_time'=>date('Y-m-d H:i:s')
                                );
                                //var_dump($tmp);
                                array_push($week_day[$i],$tmp);
                            }
                        }
                    }
                    //得到所有Policy的内容
                    $policy = $_POST['Policy'];
                    
                    //var_dump($week_day);var_dump($policy);
                    $msg = Policy::updateRecord($policy, $week_day);
                    $model = Policy::model()->findByPk($policy['id']);
                }
            }else if($type == Policy::TYPE_FOVERABLE)
            {//优惠报价
                $week_day = array();
                  
                for($i = 1; $i <=7 ; $i++)
                {
                    $week_day[$i] = array();
                    if($_POST[$i."_price"] !="")
                    {
                        $day = array(
                            'day'=>$i."",
                            'start_time'=>'',
                            'end_time'=>'',
                            'price'=>$_POST[$i.'_price'],
                            'status'=>$_POST[$i."_status"]
                        );
                        array_push($week_day[$i], $day);
                    }
                    if(isset($_POST[$i.'_start_time']))
                    {
                        //print_r($_POST[$i.'_start_time']);
                        foreach($_POST[$i.'_start_time'] as $k=>$row)
                        {
                            $tmp = array(
                                'day'=>$i."",
                                'start_time'=>$row,
                                'end_time'=>$_POST[$i."_end_time"][$k],
                                'price'=>$_POST[$i.'_d_price'][$k],
                                'status'=>$_POST[$i.'_status'],
                                'record_time'=>date('Y-m-d H:i:s')
                            );
                            //var_dump($tmp);
                            array_push($week_day[$i],$tmp);
                        }
                    }
                }
                if(@count($week_day) == 0)
                {
                    $msg['msg']="请提交详细报价！";
                    $msg['status']=-1;
                }else{
                    //得到所有Policy的内容
                    $policy = $_POST['Policy'];
                    
                    //var_dump($week_day);
                    $msg = Policy::updateRecord($policy, $week_day);
                }
                //var_dump($week_day);var_dump($policy);        
                $model = Policy::model()->findByPk($policy['id']);
            }else if($type == Policy::TYPE_SPECIAL)
            {
                //特殊报价
                $week_day = array();
                $default_price = $_POST['default_price'];
                if($default_price == ""){
                    $msg['msg']="请提交详细报价！";
                    $msg['status']=-1;
                }else
                {
                    
                    $week_day['-1'][] = array(
                        'day'=>"-1",
                        'start_time'=>"",
                        'end_time'=>"",
                        'price'=>$default_price,
                        'status'=>$_POST['default_status'],
                        'record_time'=>date('Y-m-d H:i:s')
                    );
                   
                    //var_dump($week_day);var_dump($policy);
                    $msg = Policy::updateRecord($policy, $week_day);
                    //$model = new Policy('create');
                    $model = Policy::model()->findByPk($policy['id']);
                }
            }else
            {
                $msg['msg']="更新失败，报价单类型不存在！";
                $msg['status']=-1;
            }
            
          

        }
        //获取所有的Detail
        //获取Policy的内容，然后根据type的内容，填充
        $condition = "policy_id='{$id}'";
        $detail_rows = PolicyDetail::model()->findAll($condition);
        
       //var_dump($msg);
        $this->layout = '//layouts/base';
        $this->render("edit",array('model' => $model, 'msg' => $msg,'detail_rows'=>$detail_rows));
    }


  
    /**
     * 报价单删除
     * 包括 
     * 球场信息
     * 球场的图片
     * 球场的评论
     * 球场的附近设施
     * 暂时没实现
     */
    public function actionDel(){
        $id=$_POST['id'];
        $rs = Policy::deleteRecord($id);
         
        print_r(json_encode($rs));
    }
    
    public function actionDelPolicyDetail()
    {
        $id = $_POST['id'];
        if($id == null || $id == "")
        {
            print_r('-1');
            exit;
        }
        $rs = PolicyDetail::model()->deleteByPk($id);
        if($rs){
            echo 0;
            exit;
        }
        echo -1;
        exit;
    }
    
    
    public function actionDetail()
    {
        $id = $_POST['id'];
        $model =  Yii::app()->db->createCommand()
            ->select("*")
            ->from("g_policy st")
            ->where("st.id='{$id}'")
            ->queryRow();
        $msg['status'] = true;
        if ($model) {
            $yorn = Policy::getYesOrNot();
            $detail=array(
               '包含18洞果岭'=>$yorn[$model['is_green']],
                '包含球童'=>$yorn[$model['is_caddie']],
                '包含球车'=>$yorn[$model['is_car']],
                '包含衣柜'=>$yorn[$model['is_wardrobe']],
                '包含简餐'=>$yorn[$model['is_meal']],
                '包含保险'=>$yorn[$model['is_insurance']],
                '包含小费'=>$yorn[$model['is_tip']],
                '取消时间'=>$model['cancel_remark'],
                '标识'=>$yorn[$model['remark']],
               
                '创建者'=> $model['creatorid'],
                '提交时间'=> $model['record_time'],
              
               
            );
            $msg['detail']=Utils::MakeDetailTable($detail);
        } else {
            $msg['status'] = false;
            $msg['detail'] = "获取报价单信息失败！";
        }
        print_r(json_encode($msg));
    }
    
    /**
     * 表头
     * @return SimpleGrid
     */
    private function genPDataGrid()
    {
        $t = new SimpleGrid($this->pGridId);
        $t->url = 'index.php?r=price/policygrid';
        $t->updateDom = 'datagrid';
        $t->set_header('开始日期', '100', '');
        $t->set_header('结束日期', '100', '');
        //$t->set_header('', '70', '');
        $t->set_header('服务项目', '70', '');
        $t->set_header('预订须知', '100', '');
        $t->set_header('取消规则', '100', '');
        $t->set_header('周一至周日', '150', ''); 
        $t->set_header('操作', '150', '');

        return $t;
    }

    /**
     * 查询
     */
    public function actionPolicyGrid($id,$month)
    {
        $page = $_GET['page'] == '' ? 0 : $_GET['page']; //当前页码
        $_GET['page']=$_GET['page']+1;
        $args = $_GET['q']; //查询条件

        $args['agent_id'] = Yii::app()->user->agent_id;
        $args['type'] = Policy::TYPE_NORMAL;
        $args['court_id'] = $id;
        $args['month'] = $month;
        
        //var_dump($args);
        $t = $this->genPDataGrid();

        $list = Policy::queryList($page, $this->pageSize, $args);

        $this->renderPartial('_policylist', array('t' => $t, 'rows' => $list['rows'], 'cnt' => $list['total_num'], 'month' => $month));
    }

    /**
     * 列表
     */
    public function actionPolicy()
    {
        $id = trim($_GET['id']);
        $month = trim($_GET['month']);
        $this->render('policy_list',array('relation_id'=>$id,'month'=>$month));
    }
    
    
    /**
     * 表头
     * @return SimpleGrid
     */
    private function genCDataGrid()
    {
        $t = new SimpleGrid($this->cGridId);
        $t->url = 'index.php?r=price/cusotmgrid';
        $t->updateDom = 'datagrid';
        $t->set_header('开始日期', '100', '');
        $t->set_header('结束日期', '100', '');
        //$t->set_header('', '70', '');
        $t->set_header('服务项目', '70', '');
        $t->set_header('预订须知', '100', '');
        $t->set_header('取消规则', '100', '');
        $t->set_header('周一至周日', '150', ''); 
        $t->set_header('操作', '150', '');

        return $t;
    }

    /**
     * 查询
     */
    public function actionCustomGrid($id,$month)
    {
        $page = $_GET['page'] == '' ? 0 : $_GET['page']; //当前页码
        $_GET['page']=$_GET['page']+1;
        $args = $_GET['q']; //查询条件

        $args['agent_id'] = Yii::app()->user->agent_id;
        $args['type'] = Policy::TYPE_NORMAL;
        $args['court_id'] = $id;
        $args['month'] = $month;
        
        //var_dump($args);
        $t = $this->genCDataGrid();

        $list = Policy::queryList($page, $this->pageSize, $args);

        $this->renderPartial('_customlist', array('t' => $t, 'rows' => $list['rows'], 'cnt' => $list['total_num'], 'month' => $month));
    }

    /**
     * 列表
     */
    public function actionCustom()
    {
        $id = trim($_GET['id']);
        $month = trim($_GET['month']);
        $this->render('custom_list',array('relation_id'=>$id,'month'=>$month));
    }
    
    
    /**
     * 表头
     * @return SimpleGrid
     */
    private function genSDataGrid()
    {
        $t = new SimpleGrid($this->sGridId);
        $t->url = 'index.php?r=price/specialgrid';
        $t->updateDom = 'datagrid';
        $t->set_header('开始日期', '100', '');
        $t->set_header('结束日期', '100', '');
        //$t->set_header('', '70', '');
        $t->set_header('服务项目', '70', '');
        $t->set_header('预订须知', '100', '');
        $t->set_header('取消规则', '100', '');
        $t->set_header('周一至周日', '150', ''); 
        $t->set_header('操作', '150', '');

        return $t;
    }

    /**
     * 查询
     */
    public function actionSpecialGrid($id,$month)
    {
        $page = $_GET['page'] == '' ? 0 : $_GET['page']; //当前页码
        $_GET['page']=$_GET['page']+1;
        $args = $_GET['q']; //查询条件

        $args['agent_id'] = Yii::app()->user->agent_id;
        $args['type'] = Policy::TYPE_NORMAL;
        $args['court_id'] = $id;
        $args['month'] = $month;
        
        //var_dump($args);
        $t = $this->genSDataGrid();

        $list = Policy::queryList($page, $this->pageSize, $args);

        $this->renderPartial('_speciallist', array('t' => $t, 'rows' => $list['rows'], 'cnt' => $list['total_num'], 'month' => $month));
    }

    /**
     * 列表
     */
    public function actionSpecial()
    {
        $id = trim($_GET['id']);
        $month = trim($_GET['month']);
        $this->render('special_list',array('relation_id'=>$id,'month'=>$month));
    }
	

}