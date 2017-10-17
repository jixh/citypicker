package com.lljjcoder.citypickerview.utils;

import com.lljjcoder.city_20170724.bean.CityBean;
import com.lljjcoder.city_20170724.bean.ProvinceBean;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class XmlParserHandler extends DefaultHandler {
	/**
	 * 存储所有的解析对象
	 */
	private List<ProvinceBean> provinceList = new ArrayList();

	public XmlParserHandler() { }

	public List<ProvinceBean> getDataList() {
		return provinceList;
	}

	@Override
	public void startDocument() throws SAXException { }

	private ProvinceBean provinceBean;
	private CityBean cityBean;
	int type = -1;

	@Override
	public void startElement(String uri, String localName, String qName,
							 Attributes attributes) throws SAXException {
		if (qName.equals("key")){
			provinceBean = new ProvinceBean();
			type = 0;
		}else if (qName.equals("array")){
			type = 1;
		}else if (qName.equals("string")){
			type = 2;
			cityBean = new CityBean();
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// 遇到结束标记的时候，会调用这个方法
		if (qName.equals("array")) {
			type = -1;
		} else if (qName.equals("string")) {
			provinceBean.getCityList().add(cityBean);
		} else if (qName.equals("key")) {
			provinceList.add(provinceBean);
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		super.characters(ch, start, length);
		if (length==0)return;
		String content = new String(ch, start, length);
		if (type == 0 && provinceBean !=null){
			provinceBean.setName(content);
			type = -1;
		}
		else if (type == 2 && cityBean !=null) {
			cityBean.setName(content);
			type = -1;
		}
	}

}
