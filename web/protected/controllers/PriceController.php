<?php
/*
 * 价格管理
 */
class PriceController extends AuthBaseController
{

    public $defaultAction = 'list';
    public $gridId = 'list';
    public $pGridId = 'policy_list';
    public $cGridId = 'custom_list';
    public $sGridId = 'special_list';
    public $pageSize = 20;
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
     
        $t->set_header('球场', '20%', '');
        $t->set_header('服务项目', '10%', '');
        $t->set_header('周一至周日', '20%', ''); 
              
        $t->set_header('状态', '10%', '');
        $t->set_header('正常报价', '10%', '');
        $t->set_header('优惠报价', '10%', '');
        $t->set_header('特殊报价', '10%', '');
        $t->set_header('有效期', '10%', '');
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
    
    private function getServiceArray($service)
    {
        if(!$service || @count($service) == 0)
        {
            return array();
        }
        //一共7个 1-7对于关系 is_green is_caddie is_car is_wardrobe is_meal is_insurance is_tip
        $list = array();
        for($i = 1; $i <= 7 ; $i++)
        {
            switch ($i) {
                case 1:
                    if(in_array($i."",$service))
                    {
                        $list['is_green'] = '1';
                    }else
                    {
                        $list['is_green'] = '0';
                    }

                    break;
                case 2:
                    if(in_array($i."",$service))
                    {
                        $list['is_caddie'] = '1';
                    }else
                    {
                        $list['is_caddie'] = '0';
                    }

                    break;
                case 3:
                    if(in_array($i."",$service))
                    {
                        $list['is_car'] = '1';
                    }else
                    {
                        $list['is_car'] = '0';
                    }

                    break;
                case 4:
                    if(in_array($i."",$service))
                    {
                        $list['is_wardrobe'] = '1';
                    }else
                    {
                        $list['is_wardrobe'] = '0';
                    }

                    break;
                case 5:
                    if(in_array($i."",$service))
                    {
                        $list['is_meal'] = '1';
                    }else
                    {
                        $list['is_meal'] = '0';
                    }

                    break;
                case 6:
                    if(in_array($i."",$service))
                    {
                        $list['is_insurance'] = '1';
                    }else
                    {
                        $list['is_insurance'] = '0';
                    }

                    break;
                case 7:
                    if(in_array($i."",$service))
                    {
                        $list['is_tip'] = '1';
                    }else
                    {
                        $list['is_tip'] = '0';
                    }

                    break;

                default:
                    break;
            }
        }
        return $list;
        
    }
    public function actionNewPolicy(){
        $court_id = trim($_GET['id']);
        $type = trim($_GET['tag']);//创建报价单类型
        $model=new Policy('create');
        $service = array('1','2','3','4','5','6','7');
        if($_POST['Policy']){
            $service = $_POST['service'];
            $service_detail = $this->getServiceArray($service);
            //用事务来处理
            $type = $_POST['Policy']['type'];
            $court_id = $model->court_id;
            //var_dump($_POST);var_dump($service_detail);exit;
            if(!$service||@count($service) == 0||@count($service_detail) == 0)
            {
                $msg['msg']="服务内容必填！";
                $msg['status']=-1;
            }else if($type == Policy::TYPE_NORMAL)
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
                            'vip_price'=>$_POST[$i.'_vip_price'],
                            'pledge_price'=>$_POST[$i.'_pledge_price'],
                            'status'=>$_POST[$i."_status"],
                            'record_time'=>date('Y-m-d H:i:s')
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
                                    'vip_price'=>$_POST[$i.'_d_vprice'][$k],
                                    'pledge_price'=>$_POST[$i.'_d_pprice'][$k],
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
                    $policy['is_green'] = $service_detail['is_green'];
                    $policy['is_caddie'] = $service_detail['is_caddie'];
                    $policy['is_car'] = $service_detail['is_car'];
                    $policy['is_wardrobe'] = $service_detail['is_wardrobe'];
                    $policy['is_meal'] = $service_detail['is_meal'];
                    $policy['is_insurance'] = $service_detail['is_insurance'];
                    $policy['is_tip'] = $service_detail['is_tip'];
                    
                    $policy['status'] = Policy::STATUS_NORMAL;
                    $policy['record_time']=date("Y-m-d H:i:s");
                    $policy['creatorid'] = Yii::app()->user->id;
                    $policy['agent_id'] = Yii::app()->user->agent_id;
                    $policy['id'] = $model->agent_id.date("YmdHis").rand(1000,9999);
                    
                    $msg = Policy::insertRecord($policy, $week_day);
                    $model = new Policy('create');
                }
            }else if($type == Policy::TYPE_FOVERABLE)
            {//优惠报价
                $week_day = array();
                  
                for($i = 1; $i <=7 ; $i++)
                {
                    $week_day[$i] = array();
                    
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
                                'vip_price'=>$_POST[$i.'_d_vprice'][$k],
                                'pledge_price'=>$_POST[$i.'_d_pprice'][$k],
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
                    $policy['is_green'] = $service_detail['is_green'];
                    $policy['is_caddie'] = $service_detail['is_caddie'];
                    $policy['is_car'] = $service_detail['is_car'];
                    $policy['is_wardrobe'] = $service_detail['is_wardrobe'];
                    $policy['is_meal'] = $service_detail['is_meal'];
                    $policy['is_insurance'] = $service_detail['is_insurance'];
                    $policy['is_tip'] = $service_detail['is_tip'];
                    
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
                $default_vip = $_POST['default_vip'];
                $default_pledge = $_POST['default_pledge'];
                if($default_price == ""||$default_vip==""){
                    $msg['msg']="请提交默认报价和vip报价！";
                    $msg['status']=-1;
                }else
                {
                    
                    $week_day['-1'][] = array(
                        'day'=>"-1",
                        'start_time'=>"",
                        'end_time'=>"",
                        'price'=>$default_price,
                        'vip_price'=>$default_vip,
                        'pledge_price'=>$default_pledge,
                        'status'=>$_POST['default_status'],
                        'record_time'=>date('Y-m-d H:i:s')
                    );
                    $policy = $_POST['Policy'];
                    $policy['is_green'] = $service_detail['is_green'];
                    $policy['is_caddie'] = $service_detail['is_caddie'];
                    $policy['is_car'] = $service_detail['is_car'];
                    $policy['is_wardrobe'] = $service_detail['is_wardrobe'];
                    $policy['is_meal'] = $service_detail['is_meal'];
                    $policy['is_insurance'] = $service_detail['is_insurance'];
                    $policy['is_tip'] = $service_detail['is_tip'];
                    
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
            
        $model->is_green = in_array('1',$service) ? '1':'0';
        $model->is_caddie = in_array('2',$service) ? '1':'0';
        $model->is_car = in_array('3',$service) ? '1':'0';
        $model->is_wardrobe = in_array('4',$service) ? '1':'0';
        $model->is_meal = in_array('5',$service) ? '1':'0';
        $model->is_insurance = in_array('6',$service) ? '1':'0';
        $model->is_tip = in_array('7',$service) ? '1':'0';
        
        $model->type = $type;//Policy::TYPE_NORMAL;
        $model->pay_type = '1';
        
        $model->court_id = $court_id;
        $model->id = "";
        
            
        $this->layout = '//layouts/base';
        $this->render("new",array('model' => $model, 'msg' => $msg));
    }

    public function actionEdit(){
        $id=$_GET['id'];
        $type = $_GET['tag'];
        $model=Policy::model()->findByPk($id);
        
        //var_dump($model->attributes);
        if($_POST['Policy']){
            
            $service = $_POST['service'];
            $service_detail = $this->getServiceArray($service);
            
            
            //用事务来处理
            $type = $_POST['Policy']['type'];
            $court_id = $_POST['Policy']['court_id'];
            if(!$service||@count($service) == 0||@count($service_detail) == 0)
            {
                $msg['msg']="服务内容必填！";
                $msg['status']=-1;
            }
            else if($type == Policy::TYPE_NORMAL)
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
                            'vip_price'=>$_POST[$i.'_vip_price'],
                            'pledge_price'=>$_POST[$i.'_pledge_price'],
                            'status'=>$_POST[$i."_status"],
                            'record_time'=>date('Y-m-d H:i:s')
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
                                    'vip_price'=>$_POST[$i.'_d_vprice'][$k],
                                    'pledge_price'=>$_POST[$i.'_d_pprice'][$k],
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
                    $policy['is_green'] = $service_detail['is_green'];
                    $policy['is_caddie'] = $service_detail['is_caddie'];
                    $policy['is_car'] = $service_detail['is_car'];
                    $policy['is_wardrobe'] = $service_detail['is_wardrobe'];
                    $policy['is_meal'] = $service_detail['is_meal'];
                    $policy['is_insurance'] = $service_detail['is_insurance'];
                    $policy['is_tip'] = $service_detail['is_tip'];
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
                                'vip_price'=>$_POST[$i.'_d_vprice'][$k],
                                'pledge_price'=>$_POST[$i.'_d_pprice'][$k],
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
                    $policy['is_green'] = $service_detail['is_green'];
                    $policy['is_caddie'] = $service_detail['is_caddie'];
                    $policy['is_car'] = $service_detail['is_car'];
                    $policy['is_wardrobe'] = $service_detail['is_wardrobe'];
                    $policy['is_meal'] = $service_detail['is_meal'];
                    $policy['is_insurance'] = $service_detail['is_insurance'];
                    $policy['is_tip'] = $service_detail['is_tip'];
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
                $default_vip = $_POST['default_vip'];
                $default_pledge = $_POST['default_pledge'];
                if($default_price == ""||$default_vip==""){
                    $msg['msg']="请提交默认报价和vip报价！";
                    $msg['status']=-1;
                }else
                {
                    
                    $week_day['-1'][] = array(
                        'day'=>"-1",
                        'start_time'=>"",
                        'end_time'=>"",
                        'price'=>$default_price,
                        'vip_price'=>$default_vip,
                        'pledge_price'=>$default_pledge,
                        'status'=>$_POST['default_status'],
                        'record_time'=>date('Y-m-d H:i:s')
                    );
                   
                    //得到所有Policy的内容
                    $policy = $_POST['Policy'];
                    $policy['is_green'] = $service_detail['is_green'];
                    $policy['is_caddie'] = $service_detail['is_caddie'];
                    $policy['is_car'] = $service_detail['is_car'];
                    $policy['is_wardrobe'] = $service_detail['is_wardrobe'];
                    $policy['is_meal'] = $service_detail['is_meal'];
                    $policy['is_insurance'] = $service_detail['is_insurance'];
                    $policy['is_tip'] = $service_detail['is_tip'];
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
    
    
    public function actionCustomDetail()
    {
        $id = $_POST['id'];
        $rows =  Yii::app()->db->createCommand()
            ->select("*")
            ->from("g_policy_detail st")
            ->where("st.policy_id='{$id}' and start_time!=''")
            ->queryAll();
            //var_dump($rows);
        $msg['status'] = true;
        $desc = "";
        $week = array(
            '1'=>'周一',
            '2'=>'周二',
            '3'=>'周三',
            '4'=>'周四',
            '5'=>'周五',
            '6'=>'周六',
            '0'=>'周日',
        );
        if($rows)
        {
            foreach($rows as $row)
            {
                $start_time = substr($row['start_time'],0,2).":".substr($row['start_time'],2,2);
                $end_time = substr($row['end_time'],0,2).":".substr($row['end_time'],2,2);
                $desc .= $week[$row['day']]."(".$start_time."-".$end_time."):".($row['price']/100)."<br/>";
            }
        }
        if ($rows) {
        
            $detail=array(
               '详细'=>$desc
               
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
        $t->set_header('开始日期', '10%', '');
        $t->set_header('结束日期', '10%', '');
        //$t->set_header('', '70', '');
        $t->set_header('服务项目', '15%', '');
        $t->set_header('预订须知', '15%', '');
        $t->set_header('取消规则', '15%', '');
        $t->set_header('周一至周日', '25%', ''); 
        $t->set_header('操作', '10%', '');

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
       
        $t->set_header('开始日期', '10%', '');
        $t->set_header('结束日期', '10%', '');
        //$t->set_header('', '70', '');
        $t->set_header('服务项目', '15%', '');
        $t->set_header('预订须知', '15%', '');
        $t->set_header('取消规则', '15%', '');
        $t->set_header('优惠报价', '25%', ''); 
        $t->set_header('操作', '10%', '');

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
        $args['type'] = Policy::TYPE_FOVERABLE;
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
        $t->set_header('开始日期', '10%', '');
        $t->set_header('结束日期', '10%', '');
        //$t->set_header('', '70', '');
        $t->set_header('服务项目', '15%', '');
        $t->set_header('预订须知', '15%', '');
        $t->set_header('取消规则', '15%', '');
        $t->set_header('默认价格', '25%', ''); 
        $t->set_header('操作', '10%', '');
        

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
        $args['type'] = Policy::TYPE_SPECIAL;
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
    
    
    public function actionTemplate()
    {
        $this->layout = '//layouts/base';
        $this->render("template",array());
    }
    
    public function actionDownTemplate()
    {
        $tag = $_GET['tag'];
        if($tag == '1')
        {
            //$this->downCustomTemplate();
        }else if($tag == '2')
        {
            $this->downSpecialTemplate();
        }else{
            $this->downNormalTemplate();
        }
    }
    
    private function downNormalTemplate()
    {
        //省份,城市,球场名称,编码,果,僮,车,柜,简餐,保险,小费,开始日期,结束日期,
        //周一,周二,周三,周四,周五,周六,周天,平日支付方式,平日支付金,假日支付方式,假日支付金,价格说明,取消说明
        $title = array(
            "球场名称",
            "编码",
            "果",
            "僮",
            "车",         
            "柜",
            "简餐",
            "保险",
            "小费",
            "开始日期",
            "结束日期",
            "周一(默认价格|vip价格|预付金)",
            "周二",
            "周三",
            "周四",
            "周五",
            "周六",
            "周日",
            "支付方式",
            "价格说明",
            "取消规则",
        );
        //PHPExcel
        $objPHPExcel = new PHPExcel ();

        $objPHPExcel->getProperties()->setCreator("Quick")
                ->setLastModifiedBy("Quick")
                ->setTitle("Office 2003 XLS Document")
                ->setSubject("Office 2003 XLS Document")
                ->setDescription("Quick")
                ->setKeywords("Quick")
                ->setCategory("Quick");

        $objPHPExcel->setActiveSheetIndex(0);
        
        //表头
        $t = ord('A');
        foreach ($title as $one) {
            $objPHPExcel->getActiveSheet()->setCellValue(chr($t) . '1', $one);
            $objPHPExcel->getActiveSheet()->getStyle(chr($t) . '1')->getFont()->setBold(true);
            $width = 20;
            if(in_array($one,array('取消规则','价格说明','球场名称','编码'))){
                $width = 40;
            }
            $objPHPExcel->getActiveSheet()->getColumnDimension(chr($t))->setWidth($width);
            $t++;
        }


        $activeSheet = $objPHPExcel->getActiveSheet();
        $activeSheet->setTitle('sheet1');
        
        
        //获取所有的球场的信息
        $court_list = Court::getCourtArray();
        $court_data = array();
        foreach($court_list as $id=>$name)
        {
            $tmp = array(
                $name,
                $id,
                '1',
                '0',
                '0',
                '0',
                '0',
                '0',
                '0',
                date('Y-m-01'),
                date('Y-m-t'),
                '1000|900|200',
                '',
                '',
                '',
                '',
                '',
                '',
                '全额预付',
                '此价格不含税，不提供发票。',
                '提前24小时取消.',
            );
            
            array_push($court_data, $tmp);
        }
        foreach($court_data as $key=>$row)
        {
            $t = ord('A');
            foreach ($row as $one) {
                $objPHPExcel->getActiveSheet()->setCellValueExplicit(chr($t) . ($key+2), $one, PHPExcel_Cell_DataType::TYPE_STRING);
                $t++;
            }
        }


        $filename = "normal_price_template.xls";
        //$filename = iconv("utf-8", 'gbk', $filename);
        $objWriter = PHPExcel_IOFactory::createWriter($objPHPExcel, 'Excel5');
        header('Content-Type: application/vnd.ms-excel');
        header("Content-Disposition: attachment;filename=$filename");
        header('Cache-Control: max-age=0');
        $objWriter->save('php://output');
        exit(0);
        
    }
    
    
	
    private function downSpecialTemplate()
    {
        $title = array(
            "球场名称",
            "编码",
            "果",
            "僮",
            "车",         
            "柜",
            "简餐",
            "保险",
            "小费",
            "开始日期",
            "结束日期",
            "报价(默认价|vip价|预付金)",
            "支付方式",
            "价格说明",
            "取消规则",
        );
        //PHPExcel
        $objPHPExcel = new PHPExcel ();

        $objPHPExcel->getProperties()->setCreator("Quick")
                ->setLastModifiedBy("Quick")
                ->setTitle("Office 2003 XLS Document")
                ->setSubject("Office 2003 XLS Document")
                ->setDescription("Quick")
                ->setKeywords("Quick")
                ->setCategory("Quick");

        $objPHPExcel->setActiveSheetIndex(0);
        
        //表头
        $t = ord('A');
        foreach ($title as $one) {
            $objPHPExcel->getActiveSheet()->setCellValue(chr($t) . '1', $one);
            $objPHPExcel->getActiveSheet()->getStyle(chr($t) . '1')->getFont()->setBold(true);
            $width = 20;
            if(in_array($one,array('取消规则','价格说明','球场名称','编码'))){
                $width = 40;
            }
            $objPHPExcel->getActiveSheet()->getColumnDimension(chr($t))->setWidth($width);
            $t++;
        }


        $activeSheet = $objPHPExcel->getActiveSheet();
        $activeSheet->setTitle('sheet1');
        
        
        //获取所有的球场的信息
        $court_list = Court::getCourtArray();
        $court_data = array();
        foreach($court_list as $id=>$name)
        {
            $tmp = array(
                $name,
                $id,
                '1',
                '0',
                '0',
                '0',
                '0',
                '0',
                '0',
                date('Y-m-01'),
                date('Y-m-01'),
                '1000|900|200',
                '全额预付',
                '',
                '',
            );
            
            array_push($court_data, $tmp);
        }
        foreach($court_data as $key=>$row)
        {
            $t = ord('A');
            foreach ($row as $one) {
                $objPHPExcel->getActiveSheet()->setCellValueExplicit(chr($t) . ($key+2), $one, PHPExcel_Cell_DataType::TYPE_STRING);
                $t++;
            }
        }


        $filename = "normal_price_template.xls";
        //$filename = iconv("utf-8", 'gbk', $filename);
        $objWriter = PHPExcel_IOFactory::createWriter($objPHPExcel, 'Excel5');
        header('Content-Type: application/vnd.ms-excel');
        header("Content-Disposition: attachment;filename=$filename");
        header('Cache-Control: max-age=0');
        $objWriter->save('php://output');
        exit(0);
    }
    
    
    
    public function actionLoadTemplate()
    {
        $tag = $_GET['tag'];
        $model = new Policy('create');
        $file = $_FILES['template_file']['tmp_name'];
        $msg = array();
        if(is_uploaded_file($file))
        {
            $tag = $_POST['tag'];
            //处理上传上来的内容
            if($tag == Policy::TYPE_NORMAL)
            {
                $msg = $this->dealUploadFile($file,Policy::TYPE_NORMAL);
            }else if($tag == Policy::TYPE_SPECIAL)
            {
                $msg = $this->dealUploadFile($file,Policy::TYPE_SPECIAL);
            }else
            {
                $msg['status'] = -1;
                $msg['msg'] = "只支持普通报价和特殊报价批量上传。";
            }
        }
        
        $this->layout = '//layouts/base';
        $this->render("load_template",array('tag'=>$tag,'msg'=>$msg,'model'=>$model));
    }
    
    /**
     * 处理批量上传的文件。
     * @param type $file
     * @param type $type
     * @return int
     */
    private function dealUploadFile($file,$type)
    {
        $msg = array('status'=>1,'msg'=>'操作成功');
        Yii::import('ext.phpexcelreader.JPhpExcelReader');
        $data=new JPhpExcelReader($file);

        $count = $data->rowcount(0);

        if($count < 2){
            $msg = array(
                'status'=>-1,
                'msg'=>'文件内容不能为空'
            );
            return $msg;
        }
        //获取所有的数据内容
        $error_msg= "";
        $pay_type_list = Trip::getPayType();
        $pay_type_v = array();
        $pay_type_vk = array();
        foreach($pay_type_list as $key=>$value)
        {
            array_push($pay_type_v, $value);
            $pay_type_vk[$value] = $key;
        }
        $success_cnt = 0;
        
        for($a=2;$a<=$count;$a++) {
            $court_name = trim($data->val($a,1,0));
            $court_id = trim($data->val($a,2,0));
            $is_green = trim($data->val($a,3,0));
            $is_caddie = trim($data->val($a,4,0));
            $is_car = trim($data->val($a,5,0));
            $is_wardrobe = trim($data->val($a,6,0));
            $is_meal = trim($data->val($a,7,0));
            $is_insurance = trim($data->val($a,8,0));
            $is_tip = trim($data->val($a,9,0));
            $start_date = trim($data->val($a,10,0));
            $end_date = trim($data->val($a,11,0));
            if($type == Policy::TYPE_NORMAL)
            {
                $price_one = trim($data->val($a,12,0));
                $price_two = trim($data->val($a,13,0));
                $price_three = trim($data->val($a,14,0));
                $price_four = trim($data->val($a,15,0));
            
                $price_five = trim($data->val($a,16,0));
                $price_six = trim($data->val($a,17,0));
                $price_seven = trim($data->val($a,18,0));
                $pay_type = trim($data->val($a,19,0));
                $remark = trim($data->val($a,20,0));
                $cancel_remark = trim($data->val($a,21,0));
            }else{
                $default_price = trim($data->val($a,12,0));
                $pay_type = trim($data->val($a,13,0));
                $remark = trim($data->val($a,14,0));
                $cancel_remark = trim($data->val($a,15,0));
            }
            //var_dump($start_date);var_dump($pay_type);print_r($pay_type_v);
            if(!$court_name||!$court_id||!$start_date||!$end_date||!$remark||!$cancel_remark)
            {
                $error_msg .= "第".$a."行的球场名称、编号、报价有效期、价格说明以及取消规则不能为空;";
                //var_dump($error_msg);
                continue;
            }
            $yon = array('1','0');
            if(!in_array($is_green,$yon)
               ||!in_array($is_caddie,$yon)
               ||!in_array($is_car,$yon)
               ||!in_array($is_wardrobe,$yon)
               ||!in_array($is_meal,$yon)
               ||!in_array($is_insurance,$yon)             
               ||!in_array($is_tip,$yon)){
                
                $error_msg .= "第".$a."行的果,僮,车,柜,简餐,保险,小费只能填0或1;";
                //var_dump($error_msg);
                continue;
            }
            
            if(!in_array($pay_type,$pay_type_v))
            {
                $error_msg .= "第".$a."行的付款方式有错误;";
                //var_dump($error_msg);
                continue;
            }
            
            if($type == Policy::TYPE_SPECIAL && $default_price=="")
            {
                $error_msg .= "第".$a."行的价格为空;";
                continue;
            }
            
            $policy = array();
            $policy['status'] = Policy::STATUS_NORMAL;
            $policy['record_time']=date("Y-m-d H:i:s");
            $policy['creatorid'] = Yii::app()->user->id;
            $policy['agent_id'] = Yii::app()->user->agent_id;
            $policy['id'] = Yii::app()->user->agent_id.date("YmdHis").rand(1000,9999);
            $policy['start_date'] = $start_date;
            $policy['end_date'] = $end_date;
            $policy['court_id'] = $court_id;
            $policy['remark'] = $remark;
            $policy['cancel_remark'] = $cancel_remark;
            $policy['type'] = $type;
            $policy['pay_type'] = $pay_type_vk[$pay_type];
            $policy['is_green'] = $is_green;
            $policy['is_caddie'] = $is_caddie;
            $policy['is_car'] = $is_car;
            $policy['is_wardrobe'] = $is_wardrobe;
            $policy['is_meal'] = $is_meal;
            $policy['is_insurance'] = $is_insurance;
            $policy['is_tip'] = $is_tip;
            //var_dump($policy);
            $week_day = array();
            if($type == Policy::TYPE_NORMAL)
            {
                for($k=1;$k<=7;$k++)
                {
                    $price = $price_one;
                    if($k == 2){
                        $price = $price_two;
                    }else if($k == 3){
                        $price = $price_three;
                    }else if($k == 4){
                        $price = $price_four;
                    }else if($k == 5){
                        $price = $price_five;
                    }else if($k == 6){
                        $price = $price_six;
                    }else if($k == 7){
                        $price = $price_seven;
                    }
                    
                    $price_a = explode("|", $price);
                    $price_size = @count($price_a);
                    $default_price = "";
                    $vip_price = "";
                    $pledge_price = "";
                    if($price_size == 1)
                    {
                        $default_price = $price_a[0];
                    }else if($price_size == 2)
                    {
                        $default_price = $price_a[0];
                        $vip_price = $price_a[1];
                    }else if($price_size == 3)
                    {
                        $default_price = $price_a[0];
                        $vip_price = $price_a[1];
                        $pledge_price = $price_a[2];
                    }
                    $week_day[$k] = array();              
                    $day = array(
                        'day'=>$k."",
                        'start_time'=>'',
                        'end_time'=>'',
                        'price'=>$price,
                        'vip_price'=>$vip_price,
                        'pledge_price'=>$pledge_price,
                        'status'=>"0",
                        'record_time'=>date('Y-m-d H:i:s')
                    );
                    array_push($week_day[$k], $day);
                }
                //var_dump($week_day);
                $rs_msg = Policy::insertRecord($policy, $week_day);//array('status'=>1);//
                //var_dump($rs_msg);
                if($rs_msg['status'] == -1){
                    $error_msg .= "第".$a."行插入失败。".$rs_msg['msg'].";";
                }else{
                    $success_cnt++;
                }
                
            }else {
                $price_a = explode("|", $default_price);
                $price_size = count($price_a);
                $d_price = "";
                $vip_price = "";
                $pledge_price = "";
                if($price_size == 1)
                {
                    $d_price = $price_a[0];
                }else if($price_size == 2)
                {
                    $d_price = $price_a[0];
                    $vip_price = $price_a[1];
                }else if($price_size == 3)
                {
                    $d_price = $price_a[0];
                    $vip_price = $price_a[1];
                    $pledge_price = $price_a[2];
                }
                $week_day = array();
                $week_day['-1'][] = array(
                        'day'=>"-1",
                        'start_time'=>"",
                        'end_time'=>"",
                        'price'=>$d_price,
                        'vip_price'=>$vip_price,
                        'pledge_price'=>$pledge_price,
                        'status'=>'0',
                        'record_time'=>date('Y-m-d H:i:s')
                    );
                $rs_msg = Policy::insertRecord($policy, $week_day);
                if($rs_msg['status'] == -1){
                    $error_msg .= "第".$a."行插入失败。".$rs_msg['msg'].";";
                }else{
                    $success_cnt++;
                }
            }
                   
            
        }
                    
        if($success_cnt > 0)
        {
            $msg['msg'] .= "成功新增报价的数量:".$success_cnt.";".$error_msg;
        }else{
            $msg['status'] = -1;
            $msg['msg'] = "报价单提交失败。".$error_msg;
        }

        
        
        return $msg;
    }

    
    /**
     * 复制上个月的报价
     */
    public function actionCopyPolicy()
    {
        $month = trim($_POST['month']);     
        //var_dump($month);
        $msg = Policy::copyLastMonthPolicy($month);
        
        print_r(json_encode($msg));
        exit;
    }
    
    
    
    
    
    
    

}