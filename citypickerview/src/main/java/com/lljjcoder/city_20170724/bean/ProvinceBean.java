package com.lljjcoder.city_20170724.bean;

import java.util.ArrayList;

public class ProvinceBean {

  private String name;

  private ArrayList<CityBean> cityList;

  public String getName() {
    return name == null ? "" : name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ArrayList<CityBean> getCityList() {
    if (cityList == null)cityList = new ArrayList<>();
    return cityList;
  }

  public void setCityList(ArrayList<CityBean> cityList) {
    this.cityList = cityList;
  }

  @Override
  public String toString() {
    return  name ;
  }
}
