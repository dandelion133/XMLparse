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
		
		//��ȡ���л�����
		XmlSerializer serializer = Xml.newSerializer();
		
		
		
		
		try {
			
			File path = new File(Environment.getExternalStorageDirectory(), "person.xml");
			
			FileOutputStream fos = new FileOutputStream(path);
			//�������·��
			serializer.setOutput(fos, "utf-8");
			//��ʼд
			//д��ʼ <?xml version='1.0' encoding='utf-8' standalone='yes' ?>
			serializer.startDocument("utf-8", true);
			
			serializer.startTag(null, "persons");//<persons>
			for(Person person : list) {
				serializer.startTag(null, "person");//<person>
				serializer.attribute(null, "id", String.valueOf(person.getId()));
				
				//дname
				serializer.startTag(null, "name");//<name>
				serializer.text(person.getName());
				serializer.endTag(null, "name");//</name>
				
				//дage
				serializer.startTag(null, "age");//<age>
				serializer.text(String.valueOf(person.getAge()));
				serializer.endTag(null, "age");//</age>
				
				
				serializer.endTag(null, "person");//</person>
			}
		 
			
			serializer.endTag(null, "persons");//</persons>
			serializer.endDocument();//�ļ�����
			
			
			
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
			
			// ���pull����������
			XmlPullParser parser = Xml.newPullParser();
			// ָ���������ļ��ͱ����ʽ
			parser.setInput(fis, "utf-8");
			
			int eventType = parser.getEventType(); 		// ����¼�����
			
			List<Person> personList = null;
			Person person = null;
			String id;
			while(eventType != XmlPullParser.END_DOCUMENT) {
				String tagName = parser.getName();	// ��õ�ǰ�ڵ������
				
				switch (eventType) {
				case XmlPullParser.START_TAG: 		// ��ǰ���ڿ�ʼ�ڵ�  <person>
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
						// ��Ҫ���������ú�ֵ��person������ӵ�������
						personList.add(person);
					}
					break;
				default:
					break;
				}
				
				eventType = parser.next();		// �����һ���¼�����
			}
			return personList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
	

