package de.keawe.examples.translations;

import de.keawe.tools.translations.Translation;

/**
 * Simple application demonstrating the use of the Translation class
 * @author Stephan Richter <stephan.richter.it@jena.de>
 *
 */
public class App {
	public App() {
		System.out.println("Hello world!");
		System.out.println(Translation.get(this,"Hello {} world","wonderful"));
	}
	
	public static void main(String[] args) {
		new App();
		System.out.println(Translation.get(App.class,"Hello {} world","beautiful"));
	}
}
