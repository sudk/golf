<?php

/**
 * 学校报表
 *
 * @author sudk
 */
class TranslogController extends AuthBaseController
{

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
        $t->url = 'index.php?r=log/translog/grid';
        $t->updateDom = 'datagrid';
        $t->set_header('序号', '5%', '');
        $t->set_header('商户编号', '20%', '');
        $t->set_header('清算日期', '15%', '');
        $t->set_header('日志描述', '35%', '');
        $t->set_header('类型', '10%', '');
        $t->set_header('记录时间', '15%', '');
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
            $args['startdate']=date("Y-m-d",strtotime("-1 day"));
        }

        if(!$args['enddate']){
            $args['enddate']=date("Y-m-d",strtotime("-1 day"));
        }

        $t = $this->genDataGrid();
        $this->saveUrl();

        $list = Translog::queryList($page, $this->pageSize, $args);

        $this->renderPartial('_list', array('t' => $t, 'rows' => $list['rows'], 'cnt' => $list['total_num'], 'curpage' => $list['page_num']));
    }

    /**
     * 保存查询链接
     */
    private function saveUrl()
    {
        $a = Yii::app()->session['list_url'];
        $a['rpt/school'] = str_replace("r=log/translog/grid", "r=log/translog/list", $_SERVER["QUERY_STRING"]);
        Yii::app()->session['list_url'] = $a;
    }

    /**
     * 列表
     */
    public function actionList()
    {
        $this->render('list');
    }

}