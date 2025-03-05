/* 
 * CSC 225 - Assignment 3
 * Name: Isaac Preyser
 * Student number: V01022512
 */

 /*
    Time to do some analysis.

    We have that T(n) = 3T(n/2) + 2n + k,
    (for k = constant operations) in the worst case due to the branching structure of the recursion,
    and memoization.

    The memoization is especially helpful as the sub-problems are overlapping,
    and so we can avoid recomputing the same sub-problems.
    We are guaranteed at least 2 repeated problems, leaving at worst 4 sub-problems to do.

    -----------------------------------------------------------------------------------------

    We want to find a closed form of this equation!
    (so lets do it boss)
    T(n) = 3T(n/2) + 2n
    T(n) = 3(3T(n/4) + n) + 2n
    T(n) = 3(3(3T(n/8) + n/2) + n) + 2n
    T(n) = 3(3(3(3T(n/16) + n/4) + n/2) + n) + 2n
    .
    .
    .
    T(n) = 3^{k} T(n/2^k) + 3(1 - (1/2)^k)n ; by geometric series formula (a(1 - r^n) / (1 - r))

    Let n/2^k = 1,
    =>  k = log_2(n)

    T(n) = 3^{log_2(n)} T(1) + 3(1 - (1/2)^{log_2(n)})n
    T(n) = 3^{log_2(n)} T(1) + 3(1 - 1/n)n
    T(n) = n^{log_2(3)} + 3n - 3

    T(n) = O(n^~1.58) in the worst-case, performing significantly better in most scenarios due to the memoization.

     */

import java.io.*;
import java.util.*;

@SuppressWarnings("ALL")
public class OddEquality {

    public static boolean oddEqual(int[] a, int[] b) {
        if (a.length != b.length) {
            return false;
        }
        // Create a memo map to cache results.
        Map<String, Boolean> memo = new HashMap<>();
        return oddEqual(a, 0, a.length, b, 0, b.length, memo);
    }

    private static boolean oddEqual(int[] a, int startA, int endA,
                                    int[] b, int startB, int endB,
                                    Map<String, Boolean> memo) {
        int len = endA - startA;
        // Use a key to identify this subproblem.
        String key = startA + "," + endA + "," + startB + "," + endB; // O(n) operation for n = length of key.
        if (memo.containsKey(key)) {
            return memo.get(key);
        }

        // Base case: element-wise equality.
        boolean equal = true;
        for (int i = 0; i < len; i++) {
            if (a[startA + i] != b[startB + i]) {
                equal = false;
                break;
            }
        } // O(n) operation for n = length of subarray.

        if (equal) {
            memo.put(key, true);
            return true;
        }

        // If the subarray length is odd, Condition II doesn't apply.
        if (len % 2 != 0) {
            memo.put(key, false);
            return false;
        }

        // Calculate midpoint.
        int mid = len / 2;

        // Check Condition IIa) and IIb).
        if (oddEqual(a, startA, startA + mid, b, startB, startB + mid, memo)) {
            if (oddEqual(a, startA + mid, endA, b, startB + mid, endB, memo)) {
                memo.put(key, true); // IIa) is satisfied.
                return true;
            }
            if (oddEqual(a, startA, startA + mid, b, startB + mid, endB, memo)) {
                memo.put(key, true); // IIb) is satisfied.
                return true;
            }
        }
//      Check Condition IIc)
        else if (oddEqual(a, startA + mid, endA, b, startB, startB + mid, memo) &&
                oddEqual(a, startA + mid, endA, b, startB + mid, endB, memo)) {
            memo.put(key, true);
            return true;
        }
        memo.put(key, false);
        return false;
    }






    public static void main(String[] args) {
    /* Read input from STDIN. Print output to STDOUT. Your class should be named OddEquality. 

	You should be able to compile your program with the command:
   
		javac OddEquality.java
	
   	To conveniently test your algorithm, you can run your solution with any of the tester input files using:
   
		java OddEquality inputXX.txt
	
	where XX is 00, 01, ..., 13.
	*/

   	Scanner s;
	if (args.length > 0){
		try{
			s = new Scanner(new File(args[0]));
		} catch(FileNotFoundException e){
			System.out.printf("Unable to open %s\n",args[0]);
			return;
		}
//		System.out.printf("Reading input values from %s.\n",args[0]);
	}else{
		s = new Scanner(System.in);
		System.out.printf("Reading input values from stdin.\n");
	}     
  
        int n = s.nextInt();
        int[] a = new int[n];
        int[] b = new int[n];
        
        for(int j = 0; j < n; j++){
            a[j] = s.nextInt();
        }
        
        for(int j = 0; j < n; j++){
            b[j] = s.nextInt();
        }
        
        System.out.println((oddEqual(a, b) ? "YES" : "NO"));
    }
}
