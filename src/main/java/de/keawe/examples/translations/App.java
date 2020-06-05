package de.keawe.examples.translations;

import de.keawe.tools.translations.Translation;

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
