package com.qian.xmldemo.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import android.os.Environment;
import android.test.AndroidTestCase;
import android.util.Log;
import android.util.Xml;

public class TestCase extends AndroidTestCase {

	
	public void test() {
		
		//writeXmlToLocal();
		List<Person> personList = parserXmlFromLocal();
		
		for (Person person : personList) {
			Log.i("TestCase", person.toString());
		}
	}
	
		
	
	
	
	public void writeXmlToLocal() {
		
		
		List<Person> list = getPersonList();
		
		//获取序列化对象
		XmlSerializer serializer = Xml.newSerializer();
		
		
		
		
		try {
			
			File path = new File(Environment.getExternalStorageDirectory(), "person.xml");
			
			FileOutputStream fos = new FileOutputStream(path);
			//设置输出路径
			serializer.setOutput(fos, "utf-8");
			//开始写
			//写开始 <?xml version='1.0' encoding='utf-8' standalone='yes' ?>
			serializer.startDocument("utf-8", true);
			
			serializer.startTag(null, "persons");//<persons>
			for(Person person : list) {
				serializer.startTag(null, "person");//<person>
				serializer.attribute(null, "id", String.valueOf(person.getId()));
				
				//写name
				serializer.startTag(null, "name");//<name>
				serializer.text(person.getName());
				serializer.endTag(null, "name");//</name>
				
				//写age
				serializer.startTag(null, "age");//<age>
				serializer.text(String.valueOf(person.getAge()));
				serializer.endTag(null, "age");//</age>
				
				
				serializer.endTag(null, "person");//</person>
			}
		 
			
			serializer.endTag(null, "persons");//</persons>
			serializer.endDocument();//文件结束
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
		
		
	}
	
	private List<Person> getPersonList() {
		List<Person> personList = new ArrayList<Person>();
		
		for (int i = 0; i < 30; i++) {
			personList.add(new Person(i, "wang" + i, 18 + i));
		}
		
		return personList;
	}
	
	private List<Person> parserXmlFromLocal() {
		try {
			File path = new File(Environment.getExternalStorageDirectory(), "person.xml");
			FileInputStream fis = new FileInputStream(path);
			
			// 获得pull解析器对象
			XmlPullParser parser = Xml.newPullParser();
			// 指定解析的文件和编码格式
			parser.setInput(fis, "utf-8");
			
			int eventType = parser.getEventType(); 		// 获得事件类型
			
			List<Person> personList = null;
			Person person = null;
			String id;
			while(eventType != XmlPullParser.END_DOCUMENT) {
				String tagName = parser.getName();	// 获得当前节点的名称
				
				switch (eventType) {
				case XmlPullParser.START_TAG: 		// 当前等于开始节点  <person>
					if("persons".equals(tagName)) {	// <persons>
						personList = new ArrayList<Person>();
					} else if("person".equals(tagName)) { // <person id="1">
						person = new Person();
						id = parser.getAttributeValue(null, "id");
						person.setId(Integer.valueOf(id));
					} else if("name".equals(tagName)) { // <name>
						person.setName(parser.nextText());
					} else if("age".equals(tagName)) { // <age>
						person.setAge(Integer.parseInt(parser.nextText()));
					}
					break;
				case XmlPullParser.END_TAG:		// </persons>
					if("person".equals(tagName)) {
						// 需要把上面设置好值的person对象添加到集合中
						personList.add(person);
					}
					break;
				default:
					break;
				}
				
				eventType = parser.next();		// 获得下一个事件类型
			}
			return personList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
	

