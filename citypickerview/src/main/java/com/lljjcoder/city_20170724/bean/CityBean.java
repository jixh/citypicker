package com.lljjcoder.city_20170724.bean;

/**
 * @2Do:
 * @Author M2
 * @Version v ${VERSION}
 * @Date 2017/7/7 0007.
 */
public class CityBean  {

    private String name; /*东城区*/

    public String getName() {
        return name == null ? "" : name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return  name ;
    }
}
