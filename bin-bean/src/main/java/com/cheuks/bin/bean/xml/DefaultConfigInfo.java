package com.cheuks.bin.bean.xml;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;

public class DefaultConfigInfo implements Serializable, XmlType, ConfigInfo {

	public boolean isBean(String tag) {
		return XmlType_Bean.equals(tag);
	}

	public boolean isCachePool(String tag) {
		return XmlType_CachePool.equals(tag);
	}

	public boolean isIntercepts(String tag) {
		return XmlType_Intercept.equals(tag);
	}

	public boolean isScanToPack(String tag) {
		return XmlType_ScanToPack.equals(tag);
	}

	public boolean isInitSystemClassLoader(String tag) {
		return XmlType_InitSystemClassLoader.equals(tag);
	}

	private static final long serialVersionUID = 1L;
	private CachePool cachePool;
	private String scanToPack;
	private boolean initSystemClassLoader;
	private Map<String, Bean> beans = new HashMap<String, DefaultConfigInfo.Bean>();
	/***
	 * 一个CLASS只有一条拦截记录
	 */
	private Map<String, Intercept> intercepts = new HashMap<String, DefaultConfigInfo.Intercept>();

	public CachePool getCachePool() {
		return cachePool;
	}

	public void setCachePool(CachePool cachePool) {
		this.cachePool = cachePool;
	}

	public Map<String, Bean> getBeans() {
		return beans;
	}

	public void setBeans(Map<String, Bean> beans) {
		this.beans = beans;
	}

	public Map<String, Intercept> getIntercepts() {
		return intercepts;
	}

	public void setIntercepts(Map<String, Intercept> intercepts) {
		this.intercepts = intercepts;
	}

	public static class CachePool extends XmlFill<CachePool> {
		private String className;
		private String signleNewInstanceMethod;

		@Override
		public CachePool fill(Attributes a) {
			this.className = a.getValue(XmlType_Class);
			this.signleNewInstanceMethod = a.getValue(XmlType_SignleNewInstanceMethod);
			return this;
		}

		public String getSignleNewInstanceMethod() {
			return signleNewInstanceMethod;
		}

		public void setSignleNewInstanceMethod(String signleNewInstanceMethod) {
			this.signleNewInstanceMethod = signleNewInstanceMethod;
		}

		public String getClassName() {
			return className;
		}

		public void setClassName(String className) {
			this.className = className;
		}
	}

	public static class Bean extends XmlFill<Bean> {
		private String name;
		private String className;
		private String type;
		private String ref;

		@Override
		public Bean fill(Attributes a) {
			this.name = a.getValue(XmlType_Name);
			this.className = a.getValue(XmlType_Class);
			this.ref = a.getValue(XmlType_Ref);
			this.type = a.getValue(XmlType_Type);
			return this;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getClassName() {
			return className;
		}

		public void setClassName(String className) {
			this.className = className;
		}

		public String getRef() {
			return ref;
		}

		public void setRef(String ref) {
			this.ref = ref;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}
	}

	public static class Intercept extends XmlFill<Intercept> {
		private String name;
		private String className;
		private String ref;
		private String methods;

		@Override
		public Intercept fill(Attributes a) {
			this.name = a.getValue(XmlType_Name);
			this.className = a.getValue(XmlType_Class);
			this.ref = a.getValue(XmlType_Ref);
			this.methods = a.getValue(XmlType_Methods);
			return this;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getClassName() {
			return className;
		}

		public void setClassName(String className) {
			this.className = className;
		}

		public String getRef() {
			return ref;
		}

		public void setRef(String ref) {
			this.ref = ref;
		}

		public String getMethods() {
			return methods;
		}

		public void setMethods(String methods) {
			this.methods = methods;
		}

	}

	public String getScanToPack() {
		return scanToPack;
	}

	public DefaultConfigInfo setScanToPack(String scanToPack) {
		this.scanToPack = scanToPack;
		return this;
	}

	public DefaultConfigInfo setScanToPack(Attributes a) {
		this.scanToPack = a.getValue(XmlType_Value);
		return this;
	}

	public boolean isInitSystemClassLoader() {
		return initSystemClassLoader;
	}

	public void setInitSystemClassLoader(boolean initSystemClassLoader) {
		this.initSystemClassLoader = initSystemClassLoader;
	}

	public void setInitSystemClassLoader(Attributes a) {
		this.initSystemClassLoader = Boolean.valueOf(a.getValue(XmlType_Value));
	}

	public DefaultConfigInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DefaultConfigInfo(String scanToPack, boolean initSystemClassLoader) {
		super();
		this.scanToPack = scanToPack;
		this.initSystemClassLoader = initSystemClassLoader;
	}

}
