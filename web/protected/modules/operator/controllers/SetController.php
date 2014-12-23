<?php

/**
 *
 *系统设置
 * @author guohao
 */
class SetController extends AuthBaseController
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
        $t->url = 'index.php?r=operator/set/grid';
        $t->updateDom = 'datagrid';
        
        $t->set_header('参数名称', '20%', '');
        $t->set_header('参数内容', '70%', '');
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

        $list = SysSetting::queryList($page, $this->pageSize, $args);

        $this->renderPartial('_list', array('t' => $t, 'rows' => $list['rows'], 'cnt' => $list['total_num'], 'curpage' => $list['page_num']));
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
        $model=  SysSetting::model()->findByPk($id);
        if($_POST['SysSetting']){
            
            $model->setScenario("modify");
            $model->attributes=$_POST['SysSetting'];
            $model->id = trim($_POST['SysSetting']['id']);
            $model->value = trim($_POST['SysSetting']['value']);
            $rs=$model->save();
            if($rs){
                $msg['msg']="修改成功！";
                $msg['status']=1;
                //$model=new Staff('modify');
            }else{
                $msg['msg']="修改失败！";
                $msg['status']=0;
            }

        }
        
        $this->layout = '//layouts/base';
        $this->render("edit",array('model' => $model, 'msg' => $msg));
    }

   


}