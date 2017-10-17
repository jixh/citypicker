package com.lljjcoder.citypickerview.utils;


import com.lljjcoder.citypickerview.model.CityModel;
import com.lljjcoder.citypickerview.model.ProvinceModel;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class XmlParserHandler extends DefaultHandler {
	/**
	 * 存储所有的解析对象
	 */
	private List<ProvinceModel> provinceList = new ArrayList<ProvinceModel>();

	public XmlParserHandler() { }

	public List<ProvinceModel> getDataList() {
		return provinceList;
	}

	@Override
	public void startDocument() throws SAXException { }

	private ProvinceModel provinceModel;
	private CityModel cityModel;
	int type = -1;

	@Override
	public void startElement(String uri, String localName, String qName,
							 Attributes attributes) throws SAXException {
		if (qName.equals("key")){
			provinceModel = new ProvinceModel();
			type = 0;
		}else if (qName.equals("array")){
			type = 1;
		}else if (qName.equals("string")){
			type = 2;
			cityModel = new CityModel();
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// 遇到结束标记的时候，会调用这个方法
		if (qName.equals("array")) {
			type = -1;
		} else if (qName.equals("string")) {
			provinceModel.getCityList().add(cityModel);
		} else if (qName.equals("key")) {
			provinceList.add(provinceModel);
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		super.characters(ch, start, length);
		if (length==0)return;
		String content = new String(ch, start, length);
		if (type == 0 && provinceModel!=null){
			provinceModel.setName(content);
			type = -1;
		}
		else if (type == 2 && cityModel!=null) {
			cityModel.setName(content);
			type = -1;
		}
	}

}
