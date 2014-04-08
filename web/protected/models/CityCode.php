<?php
/**
 * --请填写模块名称--
 *
 * @author #author#
 * @copyright Copyright &copy; 2003-2009 TrunkBow Co., Inc
 */
class CityCode extends CActiveRecord {

    public static function model($className=__CLASS__){
        return parent::model($className);
    }

    public function tableName(){
        return 'base_city_code';
    }
    public static function GetCityCode($m_city_code=null){
        $codes=CityCode::model()->findAll();
        $tmp=array();
        foreach ($codes as $value) {
            $tmp[$value->mcity_code] = $value->city_code;
        }
        return $m_city_code?$tmp[$m_city_code]:$tmp;
    }

     public static function GetCityName($m_city_code=null){
        $codes=CityCode::model()->findAll();
        $tmp=array(''=>'--请选择--');
        foreach ($codes as $value) {
            $tmp[$value->mcity_code] = $value->city_name;
        }
        return $m_city_code?$tmp[$m_city_code]:$tmp;
    }
}


    