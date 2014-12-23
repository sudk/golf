<?php
/*
 * 球场管理
 */
class CommentController extends AuthBaseController
{

    public $defaultAction = 'list';
    public $gridId = 'commentDDD_list';
    
    public $pageSize = 20;
    public $module_id = 'court';
    

    
    
    public function actionList()
    {
        $this->render('list');
    }
    
    /**
     * 表头
     * @return SimpleGrid
     */
    private function genDataGrid()
    {
        
        $t = new SimpleGrid($this->gridId);
        $t->url = 'index.php?r=comment/grid';
        $t->updateDom = 'datagrid';
        $t->set_header('球场名称', '20%', '');
        $t->set_header('服务', '10%', '');   
        $t->set_header('设计', '10%', '');   
        $t->set_header('设施', '10%', '');   
        $t->set_header('草坪', '10%', '');        
        $t->set_header('评分人', '10%', '');  
        $t->set_header('评分时间', '15%', '');  
        $t->set_header('备注', '15%', '');  
        return $t;
    }

    
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
        //var_dump($args);
        $list = Comment::queryList($page, $this->pageSize, $args);
        
        if($list['rows'])
        {
            $court_list = Court::getCourtArray();
            foreach($list['rows'] as $key=>$row)
            {
                $court_id = $row['court_id'];
                $list['rows'][$key]['court_name'] = $court_list[$court_id];
            }
        }
        //var_dump($list);
        $this->renderPartial('_list', array('t' => $t, 'rows' => $list['rows'], 'cnt' => $list['total_num'], 'curpage' => $list['page_num']));
    }
    

}