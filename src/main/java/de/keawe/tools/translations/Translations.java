package de.keawe.tools.translations;
import java.util.Locale;
import java.util.TreeMap;


public class Translations {
	
	private static TreeMap<String, String> map=new TreeMap<String, String>();
	
	public static String get(String key){		
		if (map.containsKey(key)) return map.get(key);
		return key;
	}

	private static Translation loadTranslation(String path, String locale) {
		if (locale==null || locale.isEmpty()) {
			locale=Locale.getDefault().getLanguage();
			locale=locale.toUpperCase();
			System.out.print("Trying to load Translation"+locale+" based on operating system...");
		} else {
			locale=locale.toUpperCase();
			System.out.print("Trying to load Translation"+locale+" based on user settings...");			
		}
		Translation trans;
		try {
			trans = (Translation) Translation.class.getClassLoader().loadClass(path+"Translation"+locale).getDeclaredConstructor().newInstance();
			System.out.println("success.");
			return trans;
		} catch (Exception e) {
		}
		System.out.println("Failed. Using en.");
		return new Translation();
	}
	
	public static String get(String key, Object insert) {
		String result=get(key);
		if (result==null) result=key;
		if (insert instanceof Object []){
			Object[] oarray = (Object[])insert;
			for (Object o:oarray){
				result=result.replaceFirst("#", string(o));
			}
			return result;
		}		
		return result.replace("#", string(insert));
	}

	private static String string(Object insert) {
		if (insert==null) return "null";
	  return insert.toString();
  }

	public static void getFor(@SuppressWarnings("rawtypes") Class cl) {
		Translation translation = loadTranslation(cl.getPackage().getName()+".",null);
		map=translation.getMap();
  }

	public static boolean getFor(@SuppressWarnings("rawtypes") Class cl, String lang) {
		Translation translation=loadTranslation(cl.getPackage().getName()+".",lang);
		if (translation!=null){
			map=translation.getMap();
			return true;
		}
		map=new TreeMap<String, String>();
		return false;
		
	}
}
