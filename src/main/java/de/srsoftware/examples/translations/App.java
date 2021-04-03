package de.srsoftware.examples.translations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.srsoftware.tools.translations.Translation;

/**
 * Simple application demonstrating the use of the Translation class
 * @author Stephan Richter <stephan.richter.it@jena.de>
 *
 */
public class App {
	private static final Logger log = LoggerFactory.getLogger(App.class);
	public App() {
		log.info(de.srsoftware.tools.translations.Translation.get(this,"Testing text with colons: {}","It works!"));
		log.info(t("Short form: {}","_(text, fills);"));
	}
	
	private String t(String text,Object...fills) {
		return Translation.get(this, text, fills);
	}
	
	public static void main(String[] args) {
		log.info(Translation.get(App.class,"Hello {} world","beautiful"));
		new App();		
	}
}
