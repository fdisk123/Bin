package com.cheuks.bin.anythingtest.bean;

import java.lang.reflect.Method;
import java.util.Arrays;

import com.cheuks.bin.anythingtest.bean.mc.scan.AutoLoadTestI;

public class A {

	public AutoLoadTestI autoLoadTestI;

	public void a() {
		autoLoadTestI.hello("xxxx");
	}

	public static void main(String[] args) {

		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		//		Method[] x = Thread.currentThread().getContextClassLoader().getClass().getDeclaredMethods();
		Method[] x = java.lang.ClassLoader.class.getDeclaredMethods();
		for (Method m : x)
			System.err.println(m.getName());
		System.out.println(Arrays.asList(x));

	}
}
