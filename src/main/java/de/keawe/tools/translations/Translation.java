package de.keawe.tools.translations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * This class provides a helper to load translations from text files
 * 
 * @author Stephan Richter <stephan.richter.it@jena.de>
 *
 */
public class Translation {
	private static String root = System.getProperty("user.dir"); 
	private static String locale = Locale.getDefault().getLanguage().toLowerCase();
	private static Map<String, Map<String,String>> translations = new HashMap<String, Map<String,String>>();

	/**
	 * Get the translation for a text. Translations are loaded by clazz and default locale:
	 * When you pass com.example.MyApp as class and your default locale is DE, the lib will search for a translation file named myApp.de.translation in your working directory.
	 * @param clazz The class which uses the translations
	 * @param text the text to be translated
	 * @param fills fillers for {} stanzas, which are also translated
	 * @return
	 */
	public static String get(Class<?> clazz, String text, String...fills) {
		return getTranslation(getClassTranslations(clazz),text,fills);
	}
	
	
	/** Get the translation for a class, uses the context.getClass() method to call the above method
	 * @param context
	 * @param text
	 * @param fills
	 * @return
	 */
	public static String get(Object context, String text, String...fills) {
		return get(context.getClass(),text,fills);
	}

	private static String getTranslation(Map<String, String> classTranslations, String text, String[] fills) {
		if (classTranslations.containsKey(text)) {
			text = classTranslations.get(text);
		}
		for (String fillin : fills) {
			if (classTranslations.containsKey(fillin)) {
				fillin = classTranslations.get(fillin);
			}
			text = text.replaceFirst("\\{\\}", fillin);
		}
		return text;
	}

	private static Map<String,String> getClassTranslations(Class<?> clazz) {
		String key = clazz.getSimpleName();
		if (!translations.containsKey(key)) {
			loadClassTranslations(key);
		}
		return translations.get(key);
	}

	private static void loadClassTranslations(String classname) {
		String translation = classname+"."+locale+".translation";
		File f = find(new File(root),translation);
		if (f != null) {
			System.out.println("Loading "+f);
			try {
				translations.put(classname,loadTranslationsFrom(f));
			} catch (IOException e) {
				translations.put(classname, new HashMap<String, String>());
				e.printStackTrace();
			}
		} else {
			System.out.println("No translations found.");
			translations.put(classname, new HashMap<String, String>());
		}		
	}

	private static Map<String, String> loadTranslationsFrom(File f) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line;
		Map<String,String> map = new HashMap<String, String>();
		while ((line = br.readLine()) != null) {
			// TODO: escape ':' in Strings
			String[] parts = line.split(":");
			map.put(parts[0].trim(), parts[1].trim());
		}
		br.close();
		return map;
	}

	private static File find(File dir, String filename) {
		File[] list = dir.listFiles();
		for (File f : list) {
			if (f.isDirectory()) {
				f = find(f,filename);
				if (f != null) {
					return f;
				}
			} else {
				if (f.getName().equals(filename)) {
					return f;
				}
			}
		}
		return null;
	}
}
