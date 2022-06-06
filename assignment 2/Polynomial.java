package poly;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class implements evaluate, add and multiply for polynomials.
 * 
 * @author: Rohit Manjunath rm1493@scarletmail.rutgers.edu rm1493
 *
 */
public class Polynomial {
	
	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage format
	 * of the polynomial is:
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * with the guarantee that degrees will be in descending order. For example:
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * which represents the polynomial:
	 * <pre>
	 *      4*x^5 - 2*x^3 + 2*x + 3 
	 * </pre>
	 * 
	 * @param sc Scanner from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 * @return The polynomial linked list (front node) constructed from coefficients and
	 *         degrees read from scanner
	 */
	public static Node read(Scanner sc) 
	throws IOException {
		Node poly = null;
		while (sc.hasNextLine()) {
			Scanner scLine = new Scanner(sc.nextLine());
			poly = new Node(scLine.nextFloat(), scLine.nextInt(), poly);
			scLine.close();
		}
		return poly;
	}

	private static Node AddToList(Node front, float coeff, int degree) {
		
		Node ptr = front;
		
		if(ptr == null) {
			
			Node returnNode = new Node(coeff,degree,null);
			return returnNode;
		}
		
		while(ptr!=null) {
	  
			if(ptr.term.degree == degree){

				ptr.term.coeff = ptr.term.coeff + coeff;
				break;
			}
			else if(ptr.term.degree > degree){

				Node returnNode = new Node(coeff,degree,null);
				return returnNode;
			}
			else if(ptr.next == null){

				ptr.next = new Node(coeff,degree,null);
				break;
			}
			else if(ptr.next.term.degree > degree){
				
				Node temp  = new Node(coeff,degree,null);
				temp.next = ptr.next;
				ptr.next = temp;
				break;
			}
			
			ptr = ptr.next;
			
		}
		
		return DeleteZeros(front);
	}

	private static Node DeleteZeros(Node front){

		if(front == null){return front;}
		
		while(CheckZero(front)){
			
			Node ptr = front,prev = null;
			
			while(ptr!=null){
				
				if(prev == null && ptr.term.coeff == 0){
					
					front = front.next;
					ptr = front;
					prev = null;
					continue;
				}
				else if(ptr.term.coeff == 0){

					prev.next = ptr.next;
				}

				prev = ptr;
				ptr=ptr.next;
			}
		}
		return front;
	}

	private static boolean CheckZero(Node front){
		
		if(front == null){return false;}
		
		Node ptr = front;
		while(ptr != null){

			if(ptr.term.coeff == 0){return true;}
			ptr = ptr.next;
		}

		return false;
	}
	
	/**
	 * Returns the sum of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list
	 * @return A new polynomial which is the sum of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node add(Node poly1, Node poly2) {

		Node front = null;
		
		for(Node ptr1 = poly1; ptr1 != null; ptr1 = ptr1.next){
			
			front = AddToList(front,ptr1.term.coeff,ptr1.term.degree);
		}
		
		for(Node ptr2 = poly2; ptr2 != null; ptr2 = ptr2.next){
			
			front = AddToList(front,ptr2.term.coeff,ptr2.term.degree);
		}

		return front;
	}
	
	/**
	 * Returns the product of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list)
	 * @return A new polynomial which is the product of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node multiply(Node poly1, Node poly2) {
		
		Node front = null;
		
		for(Node ptr1 = poly1; ptr1 != null; ptr1 = ptr1.next){
			for(Node ptr2 = poly2; ptr2 != null;ptr2 = ptr2.next){
		  
			  front = AddToList(front,ptr1.term.coeff*ptr2.term.coeff,ptr1.term.degree+ptr2.term.degree);
			}
		}

		return front;
	}
		
	/**
	 * Evaluates a polynomial at a given value.
	 * 
	 * @param poly Polynomial (front of linked list) to be evaluated
	 * @param x Value at which evaluation is to be done
	 * @return Value of polynomial p at x
	 */
	public static float evaluate(Node poly, float x) {
		
		float returnResult = 0;

		for(Node ptr = poly; ptr != null; ptr = ptr.next){
			returnResult += (ptr.term.coeff * (Math.pow(x, ptr.term.degree)));
		}

		return returnResult;
	}
	
	/**
	 * Returns string representation of a polynomial
	 * 
	 * @param poly Polynomial (front of linked list)
	 * @return String representation, in descending order of degrees
	 */
	public static String toString(Node poly) {
		if (poly == null) {
			return "0";
		} 
		
		String retval = poly.term.toString();
		for (Node current = poly.next ; current != null ;
		current = current.next) {
			retval = current.term.toString() + " + " + retval;
		}
		return retval;
	}	
}
