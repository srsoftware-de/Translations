package de.srsoftware.tools.translations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides a helper to load translations from text files
 * 
 * @author Stephan Richter <stephan.richter.it@jena.de>
 *
 */
public class Translation {
	private static final Logger log = LoggerFactory.getLogger(Translation.class);
	private static String root = System.getProperty("user.dir"); 
	private static String locale = Locale.getDefault().getLanguage().toLowerCase();
	private static Map<String, Map<String,String>> translations = new HashMap<String, Map<String,String>>();
	
	public static File find(File dir, String filename) {
		File[] list = dir.listFiles();
		for (File f : list) {
			if (f.isDirectory()) {
				f = find(f,filename);
				if (f != null) return f;
			} else if (f.getName().equals(filename)) return f;
		}
		return null;
	}

	/**
	 * Get the translation for a text. Translations are loaded by clazz and default locale:
	 * When you pass com.example.MyApp as class and your default locale is DE, the lib will search for a translation file named myApp.de.translation in your working directory.
	 * @param clazz The class which uses the translations
	 * @param text the text to be translated
	 * @param fills fillers for {} stanzas, which are also translated
	 * @return
	 */
	public static String get(Class<?> clazz, String text, Object...fills) {
		return getTranslation(getClassTranslations(clazz),text,fills);
	}
	
	
	/** Get the translation for a class, uses the context.getClass() method to call the above method
	 * @param context
	 * @param text
	 * @param fills
	 * @return
	 */
	public static String get(Object context, String text, Object...fills) {
		return get(context.getClass(),text,fills);
	}

	private static String getTranslation(Map<String, String> classTranslations, String text, Object[] fills) {
		if (classTranslations.containsKey(text)) {
			text = classTranslations.get(text);
		}
		for (Object fillin : fills) {
			String fill = (fillin == null) ? "" :  fillin.toString();
			if (classTranslations.containsKey(fill)) {
				fill = classTranslations.get(fill);
			}
			text = text.replaceFirst("\\{\\}", fill);
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
			log.info("Loading {}",f);
			try {
				translations.put(classname,loadTranslationsFrom(f));
			} catch (IOException e) {
				translations.put(classname, new HashMap<String, String>());
				e.printStackTrace();
			}
		} else {
			log.info("No translations found.");
			translations.put(classname, new HashMap<String, String>());
		}		
	}

	private static Map<String, String> loadTranslationsFrom(File f) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line;
		Map<String,String> map = new HashMap<String, String>();
		while ((line = br.readLine()) != null) {
			int colon = findColon(line);
			if (colon == -1) continue;
			
			map.put(line.substring(0,colon).replace("\\:", ":").trim(), line.substring(colon+1).replace("\\:", ":").trim());
		}
		br.close();		
		return map;
	}

	private static int findColon(String line) {
		int colon = line.indexOf(':');
		while (colon > 1 && line.charAt(colon-1)=='\\') {
			colon = line.indexOf(':', colon+1);
		}
		return colon;
	}


}
