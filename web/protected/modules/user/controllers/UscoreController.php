<?php

/**
 *
 *
 * @author sudk
 */
class UscoreController extends BaseController
{

    public $defaultAction = 'list';
    public $gridId = 'list';
    public $dGridId = 'detail_list';
    public $pageSize = 20;

    /**
     * 表头
     * @return SimpleGrid
     */
    private function genDataGrid()
    {
        $t = new SimpleGrid($this->gridId);
        $t->url = 'index.php?r=user/uscore/grid';
        $t->updateDom = 'datagrid';
        $t->set_header('序号', '5%', '');   
        $t->set_header('用户手机号', '10%', '');
        $t->set_header('所在球场', '15%', '');
        $t->set_header('洞数', '10%', '');
        $t->set_header('开球时间', '10%', '');
        $t->set_header('是否保密', '10%', '');
        $t->set_header('队友', '15%', '');
        $t->set_header('差点', '10%', '');
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

        $t = $this->genDataGrid();

        $list = Score::queryList($page, $this->pageSize, $args);

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
            ->from("g_operator st")
            ->where("st.id='{$id}'")
            ->queryRow();
        $msg['status'] = true;
        if ($model) {
            $detail=array(
                'E-Mail'=>$model['email'],
                'QQ'=>$model['qq'],
                '创建人'=>$model['creator'],
                '备注'=>$model['remark'],
            );
            $msg['detail']=Utils::MakeDetailTable($detail);
        } else {
            $msg['status'] = false;
            $msg['detail'] = "获取人员信息失败！";
        }
        print_r(json_encode($msg));
    }
    
    /**
     * 表头
     * @return SimpleGrid
     */
    private function genDDataGrid()
    {
        $t = new SimpleGrid($this->dGridId);
        $t->url = 'index.php?r=user/uscore/dgrid';
        $t->updateDom = 'datagrid';
        $t->set_header('编号', '5%', '');
        $t->set_header('洞号', '15%', '');
        $t->set_header('Tee台', '15%', '');   
        $t->set_header('标准杆', '15%', '');
        $t->set_header('长杆', '15%', '');
        $t->set_header('推杆', '15%', '');
        $t->set_header('记录时间', '15%', '');
        
        return $t;
    }

    /**
     * 查询
     */
    public function actionDGrid()
    {
        $page = $_GET['page'] == '' ? 0 : $_GET['page']; //当前页码
        $_GET['page']=$_GET['page']+1;
        $args = $_GET['q']; //查询条件

        
        if(isset($_SESSION['cur_score_detail']))
        {
            $args['score_id'] = $_SESSION['cur_score_detail'];
        }
        //var_dump($args);
        $t = $this->genDDataGrid();

        $list = ScoreDetail::queryList($page, $this->pageSize, $args);

        $this->renderPartial('_detaillist', array('t' => $t, 'rows' => $list['rows'], 'cnt' => $list['total_num'], 'curpage' => $list['page_num']));
    }
    public function actionScoreDetail()
    {
        unset($_SESSION['cur_score_detail']);
        $score_id = trim($_GET['id']);
        $_SESSION['cur_score_detail'] = $score_id;
        $this->render('detail_list',array('score_id'=>$score_id));
    }
    
    
    public function actionExport()
    {
        
        $args = $_GET['q']; //查询条件


        if ($_REQUEST['q_value'])
        {
            $args[$_REQUEST['q_by']] = $_REQUEST['q_value'];
        }
        
        
        $page = 0;
        $list = Score::queryList($page, $this->pageSize, $args);
        //导出csv格式
        $str = "成绩编号,所在球场,用户手机号,洞数,开球时间,队友,是否保密,差点,洞号,Tee台,标准杆,长杆,推杆,记录时间\n";   
        $str = iconv('utf-8','gb2312',$str); 
        $is_show = Score::getIsShow();
        $court_list = Court::getCourtArray();
        $tee_array = ScoreDetail::getTee();
        if($list['rows'])
        {
            foreach($list['rows'] as $row)
            {
                $tmp_score = $row['id'].",";
                $tmp_score .= $court_list[$row['court_id']].",";
                $tmp_score .= $row['user_phone'].",";
                $tmp_score .= $row['holes'].",";
                $tmp_score .= $row['fee_time'].",";
                $tmp_score .= str_replace(",", "|", $row['team_menbers']).",";        
                $tmp_score .= $is_show[$row['is_show']].",";
                $tmp_score .= ($row['handicap']?$row['handicap']:"--").",";
                
                
                
                $detail_rows = ScoreDetail::model()->findAll("score_id='{$row['id']}'");
                if($detail_rows)
                {
                    
                    foreach($detail_rows as $detail)
                    {
                        $tmp_str = "";
                        $tmp_str .= $tmp_score;
                        $tmp_str .= $detail['hole_no'].",";
                        $tmp_str .= $tee_array[$row['tee']].",";
                        $tmp_str .= $detail['standard_bar'].",";
                        $tmp_str .= $detail['lang_bar'].",";
                        $tmp_str .= $detail['push_bar'].",";
                        $tmp_str .= $detail['record_time']."\n";
                        
                        $str .= iconv('utf-8','gb2312',$tmp_str);
                    }
                }else
                {
                    $tmp_str = $tmp_score.",,,,,\n";
                    $str .= iconv('utf-8','gb2312',$tmp_str);
                }
               
            }
        }
        while(++$page < intval($list['total_page']))
        {
            $list = Score::queryList($page, $this->pageSize, $args);
            if($list['rows'])
            {
                foreach($list['rows'] as $row)
                {
                    $tmp_score = $row['id'].",";
                    $tmp_score .= $court_list[$row['court_id']].",";
                    $tmp_score .= $row['user_phone'].",";
                    $tmp_score .= $row['holes'].",";
                    $tmp_score .= $row['fee_time'].",";
                    $tmp_score .= str_replace(",", "|", $row['team_menbers']).",";        
                    $tmp_score .= $is_show[$row['is_show']].",";
                    $tmp_score .= ($row['handicap']?$row['handicap']:"--").",";



                    $detail_rows = ScoreDetail::model()->findAll("score_id='{$row['id']}'");
                    if($detail_rows)
                    {

                        foreach($detail_rows as $detail)
                        {
                            $tmp_str = "";
                            $tmp_str .= $tmp_score;
                            $tmp_str .= $detail['hole_no'].",";
                            $tmp_str .= $tee_array[$row['tee']].",";
                            $tmp_str .= $detail['standard_bar'].",";
                            $tmp_str .= $detail['lang_bar'].",";
                            $tmp_str .= $detail['push_bar'].",";
                            $tmp_str .= $detail['record_time']."\n";

                            $str .= iconv('utf-8','gb2312',$tmp_str);
                        }
                    }else
                    {
                        $tmp_str = $tmp_score.",,,,,\n";
                        $str .= iconv('utf-8','gb2312',$tmp_str);
                    }
                }
            }
        }
        //var_dump($str);
        $filename = "user_score_".date('Ymd').'.csv'; //设置文件名   
        //exit;
        $this->export_csv($filename,$str); //导出   
        
    }
    
    
    private function export_csv($filename,$data)   
    {   
        header("Content-type:text/csv");   
        header("Content-Disposition:attachment;filename=".$filename);   
        header('Cache-Control:must-revalidate,post-check=0,pre-check=0');   
        header('Expires:0');   
        header('Pragma:public');   
        echo $data;   
    }  



}