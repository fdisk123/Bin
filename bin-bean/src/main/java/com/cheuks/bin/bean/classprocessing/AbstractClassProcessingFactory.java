package com.cheuks.bin.bean.classprocessing;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.cheuks.bin.bean.application.BeanFactory;
import com.cheuks.bin.bean.util.ShortNameUtil;

import javassist.CannotCompileException;
import javassist.CtClass;

@SuppressWarnings("rawtypes")
public abstract class AbstractClassProcessingFactory<C> implements ClassProcessingFactory<C> {

	static final ClassLoader cl = Thread.currentThread().getContextClassLoader();
	static Method addClass = null;

	static {
		Method[] mx = java.lang.ClassLoader.class.getDeclaredMethods();
		for (Method m : mx)
			if ("addClass".contentEquals(m.getName())) {
				addClass = m;
				addClass.setAccessible(true);
				break;
			}
	}

	public static void anthingToClass(final CtClass newClazz, final String superClazzName, boolean initSystemClassLoader, boolean cloneModel) throws CannotCompileException, InstantiationException, IllegalAccessException {
		final Class c = newClazz.toClass();
		try {
			newClazz.writeFile("C:\\Users\\Ben\\Desktop");
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (cloneModel) {
			System.out.println(c.getName());
			final Object parentObject = c.newInstance();
			BeanFactory.addBean(parentObject, FULL_NAME_BEAN, superClazzName);
			BeanFactory.addBean(parentObject, NICK_NAME_BEAN, ShortNameUtil.makeLowerHumpNameShortName(superClazzName));
			BeanFactory.addBean(parentObject, SHORT_NAME_BEAN, ShortNameUtil.makeShortName(superClazzName));
		}
		else {
			BeanFactory.addBean(c, FULL_NAME_BEAN, superClazzName);
			BeanFactory.addBean(c, NICK_NAME_BEAN, ShortNameUtil.makeLowerHumpNameShortName(superClazzName));
			BeanFactory.addBean(c, SHORT_NAME_BEAN, ShortNameUtil.makeShortName(superClazzName));
		}

		if (initSystemClassLoader)
			if (null != addClass)
				try {
					addClass.invoke(cl, c);
				} catch (Exception e1) {
					e1.printStackTrace();
					throw new CannotCompileException(e1);
				}
			else
				throw new CannotCompileException("加载失败");
	}

	public void anthingToClass(List<Map<String, CtClass>> compileObject, boolean initSystemClassLoader) throws CannotCompileException {
		if (null == compileObject)
			return;
		for (int i = 0, len = compileObject.size(); i < len; i++) {
			for (Entry<String, CtClass> en : compileObject.get(i).entrySet()) {
				final Class c = en.getValue().toClass();
				BeanFactory.addBean(c, FULL_NAME_BEAN, en.getKey());
				BeanFactory.addBean(c, NICK_NAME_BEAN, ShortNameUtil.makeLowerHumpNameShortName(en.getKey()));
				BeanFactory.addBean(c, SHORT_NAME_BEAN, ShortNameUtil.makeShortName(en.getKey()));
				if (initSystemClassLoader)
					if (null != addClass)
						try {
							addClass.invoke(cl, c);
						} catch (Exception e1) {
							e1.printStackTrace();
							throw new CannotCompileException(e1);
						}
					else
						throw new CannotCompileException("加载失败");
			}
		}
	}

	protected ClassInfo scanClass(Class c, boolean isConcurrent) {
		Map<String, Field> fields = null;
		Map<String, Method> methods = null;
		if (isConcurrent) {
			final Map<String, Field> f = new ConcurrentHashMap<String, Field>();
			final Map<String, Method> m = new ConcurrentHashMap<String, Method>();
			fields = f;
			methods = m;
		}
		else {
			final Map<String, Field> f = new HashMap<String, Field>();
			final Map<String, Method> m = new HashMap<String, Method>();
			fields = f;
			methods = m;
		}
		Field[] fs = c.getDeclaredFields();
		Method[] ms = c.getDeclaredMethods();
		for (Field f : fs) {
			f.setAccessible(true);
			fields.put(f.getName(), f);
		}
		for (Method m : ms) {
			methods.put(ClassInfo.getMethod(m), m);
		}
		ClassInfo ci = new ClassInfo(c.getName() + Impl, c, fields, methods);
		return ci;
	}
}
