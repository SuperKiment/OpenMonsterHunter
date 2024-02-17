package TestsRandoms;

import java.util.HashMap;

import processing.data.JSONObject;

public class Tester {

	public static void main(String[] args) {
		System.out.println("Début test");

		JSONObject json = new JSONObject();
		json.put("coucou", 5);
		json.put("coucdezou", 55);
		json.put("cozeghetucou", 54);

		System.out.println(json.keys().toArray());
		
		for (Object str : json.keys().toArray()) {
			System.out.println(str);
			
		}
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
