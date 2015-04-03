package zappos.challenge.model;

import java.util.HashMap;
import java.util.Map;

public class Reflector {

	public static Map<Character, Character> reflectorMap = new HashMap<Character, Character>();

	public Reflector(String mapping) {
		char[] charas = mapping.toCharArray();
		for (int i = 0; i <= charas.length - 2; i = i + 2) {
			reflectorMap.put(charas[i], charas[i + 1]);
			reflectorMap.put(charas[i + 1], charas[i]);
		}
	}

	public char encodeLR(char character) {
		return reflectorMap.get(character);
	}
}
