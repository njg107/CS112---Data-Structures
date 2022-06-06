package friends;

import java.util.ArrayList;

import structures.Queue;
import structures.Stack;

public class Friends {

	private static void DFS1(boolean[] visited, ArrayList<String> member, String school, int ind, Graph g){
		
		Person person = g.members[ind];

		if(!visited[ind] && person.student && person.school.equals(school)){member.add(person.name);}

		visited[g.map.get(person.name)] = true;

		Friend tempF = g.members[ind].first;
		
		while(tempF != null){

			int number = tempF.fnum;
			Person friend = g.members[number];

			if(visited[number] == false && friend.student && friend.school.equals(school)){DFS1(visited, member, school, number, g);}

			tempF = tempF.next;
		}
	}

	private static void DFS2(int v, int start, Graph g, boolean[] visited, int[] dfsnum, int[] back, ArrayList<String> answer){
		
		Person p = g.members[v];
		visited[g.map.get(p.name)] = true;
		int count = sizeArr(dfsnum) + 1;
		
		if (dfsnum[v] == 0 && back[v] == 0){
			
			dfsnum[v] = count;
			back[v] = dfsnum[v];
		}
		
		for(Friend ptr = p.first;ptr != null;ptr = ptr.next){
			
			if(!visited[ptr.fnum]){
				
				DFS2(ptr.fnum, start, g, visited, dfsnum, back, answer);
				
				if(dfsnum[v] > back[ptr.fnum]){back[v] = Math.min(back[v], back[ptr.fnum]);} 
				
				else{
					
					if (Math.abs(dfsnum[v] - back[ptr.fnum]) < 1 && Math.abs(dfsnum[v] - dfsnum[ptr.fnum]) <= 1 && back[ptr.fnum] == 1 && v == start){continue;}
					if(dfsnum[v] <= back[ptr.fnum] && (v != start || back[ptr.fnum] == 1)){if (!answer.contains(g.members[v].name)){answer.add(g.members[v].name);}}
				}

			}
			else{back[v] = Math.min(back[v], dfsnum[ptr.fnum]);}
		}
	}

	private static int sizeArr(int[] array){
		
		int count = 0;
		
		for(int i = 0; i < array.length; i++){
			if (array[i] != 0){count++;}
		}

		return count;
	}

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null or empty array list if there is no
	 *         path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
		
		boolean[] visited = new boolean[g.members.length];

		Queue<Person> bfs = new Queue<>();

		int index = g.map.get(p1);
		bfs.enqueue(g.members[index]);

		Queue<ArrayList<String>> path = new Queue<>();

		ArrayList<String> firstList = new ArrayList<>();
		firstList.add(g.members[index].name);
		path.enqueue(firstList);

		while(!bfs.isEmpty()){
			
			Person person = bfs.dequeue();
			int personIndex = g.map.get(person.name);
			visited[personIndex] = true;
			
			ArrayList<String> temp = path.dequeue();

			Friend tempF = g.members[personIndex].first;
			while(tempF != null){

				if(!visited[tempF.fnum]){

					ArrayList<String> tempL = new ArrayList<>(temp);
					String Name = g.members[tempF.fnum].name;
					tempL.add(Name);
					
					if(Name.equals(p2)){
						return tempL;
					}

					bfs.enqueue(g.members[tempF.fnum]);
					path.enqueue(tempL);
				}

				tempF = tempF.next;
			}
		}

		return null;
	}
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null or empty array list if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
		
		ArrayList<ArrayList<String>> allC = new ArrayList<>();
		boolean[] visited = new boolean[g.members.length];

		for(int i = 0; i < g.members.length; i++){

			Person person = g.members[i];
			if(!person.student || visited[i]){continue;}

			ArrayList<String> tempC = new ArrayList<>();
			DFS1(visited, tempC, school, i, g);
			
			if(tempC.size() > 0 && tempC != null){allC.add(tempC);}
		}
		
		return allC;
	}

	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null or empty array list if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {
		
		int[] dfsnum = new int[g.members.length];
		ArrayList<String> answer = new ArrayList<>();
		int[] back = new int[g.members.length];
		boolean[] visited = new boolean[g.members.length]; 
		
		for(Person member : g.members){
			
			if (!visited[g.map.get(member.name)]){

			    dfsnum = new int[g.members.length];
			    DFS2(g.map.get(member.name), g.map.get(member.name), g, visited, dfsnum, back, answer);
			}
		}
			  
		for(int i = 0;i < answer.size();i++){
			
			Friend ptr = g.members[g.map.get(answer.get(i))].first;
			
			int count = 0;
			while (ptr != null){
				
				ptr = ptr.next;
			    count++;
			}
			
			if (count == 0 || count == 1){answer.remove(i);}
		}
		
		for(Person member : g.members){if((member.first.next == null && !answer.contains(g.members[member.first.fnum].name))){answer.add(g.members[member.first.fnum].name);}}
		
		return answer;
	}
			
}