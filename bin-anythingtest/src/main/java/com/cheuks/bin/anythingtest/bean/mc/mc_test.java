package com.cheuks.bin.anythingtest.bean.mc;

import java.util.Date;

import com.cheuks.bin.anythingtest.bean.mc.scan.IocTest1;
import com.cheuks.bin.anythingtest.bean.mc.scan.IocTest2;
import com.cheuks.bin.anythingtest.bean.mc.xml.XmlIocTest1;
import com.cheuks.bin.bean.application.BeanFactory;
import com.cheuks.bin.bean.application.DefaultApplicationContext;

public class mc_test {
	public static void main(String[] args) throws Throwable {
		new DefaultApplicationContext("bean.xml");

		Date now = new Date();
		now = new Date();
		IocTest2 i2 = null;
		for (int z = 0, len = 100000000; z < len; z++) {
			i2 = new IocTest2();
			i2.x();
		}
		Date end = new Date();
		System.out.println("i2使用时间" + (end.getTime() - now.getTime()));

		now = new Date();
		IocTest1 i = null;
		for (int z = 0, len = 100000000; z < len; z++) {
			i = BeanFactory.getBean("IocTest1", true);
			i.aaxx("xxxx");
		}
		end = new Date();
		System.out.println("i使用时间" + (end.getTime() - now.getTime()));

		now = new Date();
		XmlIocTest1 x = BeanFactory.getBean("XmlIocTest1", true);
		x.aaxx("123xxxx");
		x.aaxx3("xxxxxxxxxxxxxx3", "123");
		end = new Date();
		System.out.println("x使用时间" + (end.getTime() - now.getTime()));
	}
}
