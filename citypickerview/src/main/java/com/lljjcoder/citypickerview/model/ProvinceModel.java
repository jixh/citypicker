package com.lljjcoder.citypickerview.model;

import android.util.Log;

import com.lljjcoder.city_20170724.bean.CityBean;
import com.lljjcoder.city_20170724.bean.ProvinceBean;

import java.util.ArrayList;
import java.util.List;

public class ProvinceModel {
	private String name;
	private List<CityModel> cityList = new ArrayList<>();
	
	public ProvinceModel() {
		super();
	}

	public ProvinceModel(String name, List<CityModel> cityList) {
		super();
		this.name = name;
		this.cityList = cityList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		Log.e("XmlParserHandler","setName="+name);
	}

	public List<CityModel> getCityList() {
		return cityList;
	}

	public void setCityList(List<CityModel> cityList) {
		this.cityList = cityList;
	}

	@Override
	public String toString() {
		return "ProvinceModel [name=" + name + ", cityList=" + cityList + "]";
	}

	public ProvinceBean toProvinceBean(){
		ProvinceBean provinceBean = new ProvinceBean();
		provinceBean.setName(name);

		ArrayList<CityBean> cityBeans = new ArrayList<>();
		for (CityModel cm:cityList) {
			CityBean cityBean = new CityBean();
			cityBean.setName(cm.getName());
			cityBeans.add(cityBean);
		}
		provinceBean.setCityList(cityBeans);

		return provinceBean;
	}
}
