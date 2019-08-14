package com.rachio.challenge;

import java.util.Arrays;
import java.util.*;

public class Part1 {

	private List<String> family;
	private List<String> copy;
	
	
	public static void main(String... args) {		
		
		Scanner sc = new Scanner(System.in);
		
		Part1 p  = new Part1();
		p.family = Arrays.asList("Edwin", "Manuel", "Elizabeth", "Angela", "Roger");

		Map<String, String> giver = p.getGivers(p.family);		

		giver.forEach((k, v) -> System.out.println((k + " --> " + v)));
	}

	/**
	 * Returns a Map (giver, recipient) from a given list
	 * 
	 * @param List<String> family
	 *   List of Strings 
	 * @return Map<String,String> where a pair key-value are always different
	 */
	private Map<String, String> getGivers(List<String> family) {
		
		Collections.shuffle(family);
		
		copy = new ArrayList<>(family);
		
		Map<String,String> givers = new HashMap<>();
		int luck;
		String temp;
		for (int i=0; i<family.size(); i++) {	
			luck = getRandom(copy, i);
			givers.put(family.get(i), copy.remove(luck));
		
			// Check if the last person is giving gift to himself and randomly exchange with one of the others
			if(copy.size() == 0 && givers.get(family.get(i)).equals(family.get(i))) {				
				luck = getRandom(family, i);				
				givers.put(family.get(i), givers.get(family.get(luck)));
				givers.put(family.get(luck), family.get(i));
			}
		}

		return givers;
	}
	
	/**
	 * Returns a index of the list given which is selected randomly
	 * 
	 * @param remainings
	 * 	List of strings
	 * @param i
	 * 	Index that must not be chosen from the List
	 * @return luck
	 * 	Index of the list given; chosen randomly
	 */
	private int getRandom(List<String> remainings, int i) {
		Random r = new Random();
		int luck;
		do {
			luck = r.ints(0, remainings.size()).findFirst().getAsInt();			
		} while (family.get(i).equals(remainings.get(luck)) && remainings.size() > 1); 
		
		return luck;
	}

}
