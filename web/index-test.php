<?php
$ar=array(
    'a'=>"中国人2aaa",
    'c'=>432,
    'b'=>443
);
ksort($ar);
$str="";
foreach($ar as $k=>$v){
    $str.="{$k}=".urldecode($v)."&";
}
echo $str;