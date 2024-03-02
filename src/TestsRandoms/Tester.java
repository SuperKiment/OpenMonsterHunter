package TestsRandoms;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.function.Function;

import logic.Entity;
import processing.data.JSONObject;

public class Tester {

	public void lezgo() {

	}

	public static void main(String[] args) {
		System.out.println("Début test");
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		

		HashMap<Class, Getmethod> hash = new HashMap<Class, Getmethod>();
		hash.put(logic.Entity.class, new Getmethod() {
			public void Action() {
				System.out.println("coucou");
			}
		});

		logic.Entity e = new logic.Entity();
		logic.Dog d = new logic.Dog();

		hash.get(e.getClass()).Action();
		hash.get(d.getClass()).Action();
	}

	public void Classes() {
		Abc abc = new Abc("a");

		try {
			Class test = Class.forName("TestsRandoms.Abc");
			System.out.println("Trouvé classe " + test.getName());

			Object o = test.getDeclaredConstructor(String.class).newInstance("fez");
		} catch (Exception e) {

			System.out.println("Erreur : " + e);
		}
	}
}
