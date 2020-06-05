package de.keawe.examples.translations;

import de.keawe.tools.translations.Translation;

public class App {
	public App() {
		System.out.println(Translation.get(this,"Testing text with colons: {}","It works!"));
		System.out.println(_("Short form: {}","_(text, fills);"));
	}
	
	private String _(String text,String...fills) {
		return Translation.get(this, text, fills);
	}
	
	public static void main(String[] args) {
		System.out.println(Translation.get(App.class,"Hello {} world","beautiful"));
		new App();		
	}
}
