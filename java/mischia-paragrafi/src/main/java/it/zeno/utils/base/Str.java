package it.zeno.utils.base;


public class Str {
	static public interface consts{
		String VOID = "";
		String COMMA = ",";
	}
	public static char[] alfabeto;
	
	public static char getAalfabeto(int x) {
		
		if(alfabeto == null)
			synchronized (Str.class) {
				if(alfabeto == null) {
					alfabeto = new char[26];
			        for (int i = 0; i < 26; i++) 
			            alfabeto[i] = (char) ('A' + i);
				}
			}
		
		return alfabeto[x];
	}
	
}