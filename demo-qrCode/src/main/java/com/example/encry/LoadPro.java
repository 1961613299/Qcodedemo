package com.example.encry;

import java.io.*;
import java.util.*;

public class LoadPro {
	
	public final static String SYSTEM_PROPERTIES = "/system.properties";

	// 文件存储路径,?后字符应包含文件分隔符号
	private static String propertiesStorePath;
	// 存防属???列?
	private static Map propertieMap = new HashMap();
	// 存放属???文?
	private static Map propertieFileMap = new HashMap();

	static
	{
		Properties properties = init(SYSTEM_PROPERTIES);
		Iterator it = properties.keySet().iterator();
		propertiesStorePath = properties.getProperty("path");

		while (it.hasNext())
		{
			String name = (String) it.next();
			String file = properties.getProperty(name);

			file = file.trim();

			propertieFileMap.put(name, file);
			Properties p = init("/" + file);
			propertieMap.put(name, p);
		}
	}

	private static Properties init(String propertyFile)
	{
		Properties p = new Properties();
		try
		{
			System.out.println("Start Loading property file \t" + propertyFile);
//			p.load(GlobalConfig.class.getResourceAsStream(propertyFile));
//			p.load(GlobalConfig.class.getResourceAsStream(propertyFile));
			p.load(new InputStreamReader(LoadPro.class.getResourceAsStream(propertyFile), "UTF-8"));
			System.out.println("Load property file success!\t" + propertyFile);
		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("Could not load property file." + propertyFile);
		}

		return p;
	}

	/**
	 * <p>
	 * <b>业务处理描述</b>
	 * <ul>
	 * <li>可覺性原因：?要袀其他应用调用</li>
	 * <li>目的：获取属?</li>
	 * <li>适用的前提条?:</li>
	 * <li>后置条件?</li>
	 * <li>例夁处理：无 </li>
	 * <li>已知问题?</li>
	 * <li>调用的例子： </li>
	 * </ul>
	 * </p>
	 * 
	 * @param cls
	 *            属???文件类别，即system.properties中配置的文件key名称
	 * @param name
	 *            属???名?
	 * @return
	 */
	public static String loadProperties(String cls, String name)
	{
		Properties p = (Properties) propertieMap.get(cls);
		if (p != null)
		{
			return p.getProperty(name);
		} else
		{
			return null;
		}
	}

	public static boolean getBooleanProperty(String cls, String name)
	{
		String p = loadProperties(cls, name);
		return "true".equals(p);
	}

	public static Integer getIntegerProperty(String cls, String name)
	{
		String p = loadProperties(cls, name);
		if (p == null)
		{
			return null;
		}
		return Integer.valueOf(p);
	}

	public static Long getLongProperty(String cls, String name)
	{
		String p = loadProperties(cls, name);
		if (p == null)
		{
			return null;
		}
		return Long.valueOf(p);
	}

	public static Double getDoubleProperty(String cls, String name)
	{
		String p = loadProperties(cls, name);
		if (p == null)
		{
			return null;
		}
		return Double.valueOf(p);
	}

	/**
	 * <p>
	 * <b>业务处理描述</b>
	 * <ul>
	 * <li>可覺性原因：?要袀其他应用调用</li>
	 * <li>目的：保存属性配置文?</li>
	 * <li>适用的前提条?:在配置文件中增加了path属???</li>
	 * <li>后置条件?</li>
	 * <li>例夁处理：无 </li>
	 * <li>已知问题?</li>
	 * <li>调用的例子： </li>
	 * </ul>
	 * </p>
	 */
	public static void store()
	{

	}

	/**
	 * <p>
	 * <b>业务处理描述</b>
	 * <ul>
	 * <li>可覺性原因：?要袀其他应用调用</li>
	 * <li>目的：保存单个配置文?</li>
	 * <li>适用的前提条?:配置文件中增加了配置选项</li>
	 * <li>后置条件?</li>
	 * <li>例夁处理：无 </li>
	 * <li>已知问题?</li>
	 * <li>调用的例子： </li>
	 * </ul>
	 * </p>
	 * 
	 * @param cls
	 */
	public static void store(String cls)
	{
		Properties p = (Properties) propertieMap.get(cls);
		FileOutputStream fi;
		try
		{
			fi = new FileOutputStream(new File((String) propertieFileMap
					.get(cls)));
			p.store(fi, "Modified time: " + Calendar.getInstance().getTime());
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	/*public static String loadProperties(String fileName,String key){
		String value="";
		Properties prop =new Properties();
		try {
			prop.load(LoadPro.class.getResourceAsStream("/"+fileName+".properties"));
			value=prop.getProperty(key);
		} catch (Exception e) {
			return "";
		}

		return value;
	}*/
}
