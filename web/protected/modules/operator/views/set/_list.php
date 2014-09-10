<?php
$t->echo_grid_header();
if (is_array($rows))
{
	$j = 1;
    foreach ($rows as $i => $row)
    {
        //$t->begin_row("onclick","getDetail(this,'{$row['id']}');");
	
        $link = CHtml::link('编辑',"javascript:itemEdit('{$row['id']}')", array());
        
        $value = $row['value'];
        
        
        $t->echo_td($row['desc']); //
        $t->echo_td($value);
        $t->echo_td($link);
        $t->end_row();
    }
}
$t->echo_grid_floor();

?>
<div class="alternate-rule" style="display: none;"></div>