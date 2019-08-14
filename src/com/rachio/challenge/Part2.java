package com.rachio.challenge;

import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.*;

public class Part2 {

	private List<String> family;

	private Map<String, Queue<String>> forbidden = new HashMap<>();
	
	
	public static void main(String... args) {		
		
		Scanner sc = new Scanner(System.in);
		Part2 p  = new Part2();
		
		
		System.out.println("How many members are in your family?");
		int n = sc.nextInt();
		System.out.println("Please enter each of the "+ n +" members");
		
		for (int i = 0; i<n; i++) {
			p.family.add(sc.next());
		}
		
		//p.family = Arrays.asList("Edwin", "Manuel", "Elizabeth", "Angela", "Roger");

		Map<String, String> givers;		

		// execute for 5 years
		for (int i=0; i<5; i ++) {
			 System.out.println("Year:" + (i+1));
			 givers = p.getGivers(p.family);			 
			 
			 givers.forEach((k, v) -> System.out.println((k + " --> " + v)));
			 System.out.println();
		}
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
		
		Map<String,String> givers = new HashMap<>();
		String recipient, temp;		
		boolean flag;		
		List<String> taken = new CopyOnWriteArrayList<>();

		for (String member: family) {
			flag = false;
			Queue<String> q = forbidden.containsKey(member) ? forbidden.get(member) : new LinkedList<>();			
			
			List<String> remainings = family.stream()
					.filter(c -> !q.contains(c) && !taken.contains(c) && !c.equals(member))
					.collect(Collectors.toList());

			// if remainings is empty means that the current person in the family list is going to give gift to him/herself
			// Pick up another family member			
			if(remainings.size() == 0) {
				do {				
					remainings = family.stream()
							.filter(c -> !q.contains(c) && !c.equals(member))
							.collect(Collectors.toList());
					
					recipient = getRandomRecipient(remainings);
					final String rec = recipient;
					temp = givers.entrySet().stream()
							.filter(x -> rec.equals(x.getValue()))
							.map(Map.Entry::getKey).findFirst().get();

					if (forbidden.containsKey(recipient) && !forbidden.get(recipient).contains(member)) {
						forbidden.get(temp).remove(recipient);
						forbidden.get(temp).offer(member);
						givers.put(temp, member);						
						flag = true;
					}
				} while(!flag); 

			} else {
				recipient = getRandomRecipient(remainings);
			}

			taken.add(recipient);
			givers.put(member, recipient);
			if (forbidden.containsKey(member)) {
				forbidden.get(member).offer(recipient);
				//Only store info of the last 3 years
				if (forbidden.get(member).size() > 3) forbidden.get(member).poll(); 
			} else {
				Queue<String> a = new LinkedList<>();
				a.add(recipient);
				forbidden.put(member, a);
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
	private String getRandomRecipient(List<String> remainings) { 
		
		return remainings.get(new Random().ints(0, remainings.size()).findFirst().getAsInt());
	}

}
