package com.cheuks.bin.bean.classprocessing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import com.cheuks.bin.annotation.AutoLoad;
import com.cheuks.bin.annotation.Register;
import com.cheuks.bin.bean.application.BeanFactory;
import com.cheuks.bin.bean.classprocessing.handler.ClassProcessingHandler;
import com.cheuks.bin.bean.classprocessing.handler.DefaultAutoLoadHandler;
import com.cheuks.bin.bean.classprocessing.handler.DefaultInterceptHandler;
import com.cheuks.bin.bean.classprocessing.handler.DefaultRmiClientHandler;
import com.cheuks.bin.bean.classprocessing.handler.HandlerInfo;
import com.cheuks.bin.bean.util.ExecutorServiceFatory;
import com.cheuks.bin.bean.util.ShortNameUtil;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMember;
import javassist.CtMethod;
import javassist.NotFoundException;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class DefaultClassProcessingFactory extends AbstractClassProcessingFactory<CreateClassInfo> {

	public CreateClassInfo getCompleteClass(Set<String> clazzs, Object config) throws Throwable {
		final ConcurrentHashMap<String, CtClass> complete = new ConcurrentHashMap<String, CtClass>();
		final ConcurrentHashMap<String, String> nick = new ConcurrentHashMap<String, String>();
		final ConcurrentHashMap<String, String> shortName = new ConcurrentHashMap<String, String>();
		final CountDownLatch countDownLatch = new CountDownLatch(clazzs.size());
		Map<String, Map> cache = new HashMap<String, Map>();
		cache.put(ClassProcessingFactory.REGISTER_CACHE, complete);
		cache.put(ClassProcessingFactory.NICK_NAME_CACHE, nick);
		cache.put(ClassProcessingFactory.SHORT_NAME_CACHE, shortName);
		Iterator<String> it = clazzs.iterator();
		while (it.hasNext()) {
			ExecutorServiceFatory.SINGLE_EXECUTOR_SERVICE.execute(new Worker(countDownLatch, cache, it.next()));
		}
		countDownLatch.await();
		//
		List<ClassProcessingHandler> list = new ArrayList<ClassProcessingHandler>();
		list.add(new DefaultAutoLoadHandler());
		list.add(new DefaultInterceptHandler());
		list.add(new DefaultRmiClientHandler());
		return doHandler(list, cache);

	}

	class Worker implements Runnable {

		private CountDownLatch countDownLatch;
		private Map<String, Map> cache;
		private String className;
		private List<String> xmlAppendList;
		private boolean falgs;

		public Worker(CountDownLatch countDownLatch, Map<String, Map> cache, String className) {
			super();
			this.countDownLatch = countDownLatch;
			this.cache = cache;
			this.className = className;
		}

		public Worker(CountDownLatch countDownLatch, Map<String, Map> cache, List<String> xmlAppendList) {
			super();
			this.countDownLatch = countDownLatch;
			this.cache = cache;
			this.xmlAppendList = xmlAppendList;
			this.falgs = true;
		}

		public void run() {
			try {
				int count = 1;
				if (this.falgs) {
					count = xmlAppendList.size();
				}
				while (--count >= 0) {
					if (this.falgs)
						className = xmlAppendList.get(count);

					ClassPool pool = ClassPool.getDefault();
					CtClass clazz = pool.get(className);

					Object o = clazz.getAnnotation(Register.class);
					if (null == o || null != BeanFactory.getBean(clazz.getName()))
						continue;
					String name = ((Register) o).value();
					if (name.length() > 0)
						cache.get(NICK_NAME_CACHE).put(name, clazz.getName());
					cache.get(REGISTER_CACHE).put(clazz.getName(), clazz);

					BeanFactory.addCache(clazz.getName(), ClassProcessingFactory.SHORT_NAME_CACHE, ShortNameUtil.makeShortName(clazz.getName()));
					//搜索class
					try {
						BeanFactory.addClassInfo(scanClass(Class.forName(className), true));
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
				return;
			} catch (NotFoundException e) {
				//				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e1) {
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			} finally {
				if (null != countDownLatch)
					countDownLatch.countDown();
			}
		}
	}

	protected CreateClassInfo doHandler(List<ClassProcessingHandler> handler, final Map<String, Map> classCache) throws Throwable {

		final Map<String, String> autoLoadClass = new ConcurrentHashMap<String, String>();
		classCache.put(ClassProcessingFactory.AUTO_LOAD_CACHE, autoLoadClass);

		final CreateClassInfo result = new CreateClassInfo();

		Iterator<Entry<String, CtClass>> it = classCache.get(REGISTER_CACHE).entrySet().iterator();

		//		List<Map<String, CtClass>> compileObject = new ArrayList<Map<String, CtClass>>();
		//		Set<CtField> additionalField = new HashSet<CtField>();//附加字段
		//		Set<CtMethod> additionalMethod = new HashSet<CtMethod>();//附加方法

		//		Map<String, CtClass> A1 = new HashMap<String, CtClass>();
		//		Map<String, CtClass> A2 = new HashMap<String, CtClass>();
		//		Map<String, CtClass> A3 = new HashMap<String, CtClass>();
		//		compileObject.add(A1);
		//		compileObject.add(A2);
		//		compileObject.add(A3);

		Entry<String, CtClass> en;
		int level = 0;
		List<HandlerInfo> handlerInfos = null;
		boolean appendTry;
		boolean isInterface;
		while (it.hasNext()) {
			level = 0;
			handlerInfos = new ArrayList<HandlerInfo>();
			en = it.next();
			CtClass tempClazz = en.getValue();
			CtClass newClazz = tempClazz.getClassPool().makeClass(tempClazz.getName() + Impl);
			newClazz.getClassPool().importPackage(en.getKey());
			//搜索Feld
			CtField[] ctFields = tempClazz.getDeclaredFields();
			//搜索Method
			//********************
			CtMethod[] ctMethods = tempClazz.getDeclaredMethods();

			if (isInterface = tempClazz.isInterface())
				newClazz.addInterface(tempClazz);
			else
				newClazz.setSuperclass(tempClazz);

			HandlerInfo handlerInfo;
			for (ClassProcessingHandler<CtClass, Object, CtMember, CtClass, HandlerInfo> cph : handler) {
				for (CtField f : ctFields) {//Field
					if (null != cph.getCheck(f, ClassProcessingHandler.Field) || null != cph.getCheckII(tempClazz, ClassProcessingHandler.Type)) {
						handlerInfo = cph.doProcessing(classCache, newClazz, f, null);
						if (null == handlerInfo)
							continue;
						handlerInfos.add(handlerInfo);
						level++;
					}
				}
				for (CtMethod m : ctMethods) {//Method
					if (null != cph.getCheck(m, ClassProcessingHandler.Method) || null != cph.getCheckII(tempClazz, ClassProcessingHandler.Type)) {
						//						handlerInfos.add();
						handlerInfo = cph.doProcessing(classCache, newClazz, m, null);
						if (null == handlerInfo)
							continue;
						handlerInfos.add(handlerInfo);
						newClazz = handlerInfo.getNewClazz();
						level++;
					}
				}
			}

			//Import
			newClazz.getClassPool().importPackage("java.lang.Exception");
			newClazz.getClassPool().importPackage("java.lang.reflect.Field");
			newClazz.getClassPool().importPackage("java.lang.reflect.Method");
			newClazz.getClassPool().importPackage("com.cheuks.bin.bean.application.BeanFactory");
			newClazz.getClassPool().importPackage("com.cheuks.bin.bean.classprocessing.ClassInfo");
			//			//建立构造、构造加载
			//			CtConstructor tempC;
			//			CtConstructor[] ctConstructors = tempClazz.getDeclaredConstructors();
			//			CtConstructor defauleConstructor = CtNewConstructor.defaultConstructor(newClazz);
			StringBuffer sb = new StringBuffer("{");
			sb.append("super($$);");
			appendTry = false;
			if (handlerInfos.size() > 0) {
				sb.append("try {");
				for (HandlerInfo h : handlerInfos) {
					if (null != h.getX() && h.getX().length() > 0) {
						sb.append(h.getX());
						appendTry = true;
					}
				}
				sb.append("}catch(java.lang.Exception e){e.printStackTrace();}");
			}
			sb.append("}");
			//			System.out.println(sb.toString());
			//						defauleConstructor.setBody(sb.toString());

			//			defauleConstructor.addCatch("", newClazz.getClassPool().get("java.lang.Exception"));
			//			//################构造###################
			//			newClazz.addConstructor(defauleConstructor);
			//
			//			try {
			//				for (CtConstructor c : ctConstructors) {
			//					tempC = CtNewConstructor.copy(c, newClazz, null);
			//					tempC.setBody("{super($$);}");
			//					newClazz.addConstructor(tempC);
			//				}
			//			} catch (DuplicateMemberException e) {
			//				//				e.printStackTrace();
			//			}
			//Import
			for (HandlerInfo h : handlerInfos) {
				if (null != h.getImports())
					for (String s : h.getImports()) {
						newClazz.getClassPool().importPackage(s);
					}
			}
			System.out.println(level + ":" + newClazz.getName());
			if (level == 0)
				//				A1.put(en.getKey(), newClazz);
				result.addFirstQueue(new DefaultTempClass(tempClazz, newClazz));
			else
				result.addSecondQueue(new DefaultTempClass(tempClazz, newClazz, appendTry ? sb.toString() : null, null));
			//				A2.put(en.getKey(), newClazz);

			//反编查看
			//			try {
			//				tempClazz.writeFile("C:/Users/Ben/Desktop");
			//			} catch (IOException e) {
			//				e.printStackTrace();
			//			}
		}
		//				anthingToClass(compileObject);
		//		return compileObject;
		return result;
	}

}
