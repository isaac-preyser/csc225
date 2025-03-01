/* 
 * CSC 225 - Assignment 3
 * Name: Isaac Preyser
 * Student number: V01022512
 */

 /*
    Time to do some analysis.

    We have that T(n) = 3T(n/2) + 4n. (plus a few constant time operations which really don't matter too much at scale)
    We want to find a closed form of this equation! (so lets do it boss)
    T(n) = 3T(n/2) + 4n
    .
    .
    .
    T(n) = 3^{k} T(n/2^k) + ((3^k) - 1) / 2 * 4n
         = 3^{k} T(n/2^k) + ( 3^k - 1 ) * 2n

    Let n/2^k = 1,
    =>  k = log_2(n)

    Hence,
    T(n) = 3^{log_2(n)} T(1) + (3^{log_2(n) - 1 ) * 2n
         = n^{log_2(3)} T(1) + 2n^{1 + log_2(3)} - 2n

    We see the dominant growing term is n^{log_2(3)}, and so we can use that for the Big-O analysis.

    T(n) \in O(n^{log_2(3)})
    T(n) \in O(n^{~1.58})
    Hence T(n) grows less fast than O(n^2).





     */
 
 
import java.io.*;
import java.util.*;

@SuppressWarnings("ALL")
public class OddEquality {
    
    static boolean oddEqual(int[] a, int[] b){
        // Condition I: A = B (the two arrays have element-wise equality.)
        // java already implements Arrays.equals(), for element-wise array comparison.

        if (Arrays.equals(a, b)) return true; //n + k operations.

        /*
        Condition II: If n is divisible by 2, A and B are divided into two sub-arrays of equal size.
        (A is divided into A1 and A2, and B is divided into B1 and B2)
        Then, at least one of the following conditions is satisfied:
            a) (A1 is oddly equal to B1) AND (A2 is oddly equal to B2)
            b) (A1 is oddly equal to B1) AND (A1 is oddly equal to B2)
            c) (A2 is oddly equal to B1) AND (A2 is oddly equal to B2)

        NOTE: if n is not divisible by 2, condition II is not satisfied.
         */
        if (a.length % 2 == 1 || b.length % 2 == 1) return false; //k = 7 work in java.

        //now we can create the sub-arrays.
        int mid = a.length / 2; // k = 3 work.
        int[] a1 = Arrays.copyOfRange(a, 0, mid); //n work.

        // n work is justifiable to be on the safe side; Arrays.copyOfRange calls System.arraycopy, which is a native method.
        // This means that this method is run using a per-JVM implementation, which means that
        // the amount of work done may differ based on the version of java, and the platform that java is running on, per the JNI [1].
        // I'm personally running OpenJDK 23, so I went for a little dive into the source code.
        // In my specific case, AVX extensions are used in the relevant intrinsic function, suggesting an optimization of ~O(n/32) [2].
        // For my purposes, this I'll call this O(n) work.

        // [1] “Introduction to Java Native Interface.” Accessed: Feb. 28, 2025. [Online]. Available: https://docs.oracle.com/javase/8/docs/technotes/guides/jni/spec/intro.html#java_native_interface_overview
        // [2] “jdk/src/hotspot/cpu/x86/stubGenerator_x86_64_arraycopy.cpp at master · openjdk/jdk.” Accessed: Feb. 28, 2025. [Online]. Available: https://github.com/openjdk/jdk/blob/master/src/hotspot/cpu/x86/stubGenerator_x86_64_arraycopy.cpp

        int[] a2 = Arrays.copyOfRange(a, mid, a.length); //n work.

        int[] b1 = Arrays.copyOfRange(b, 0, mid); //n work.
        int[] b2 = Arrays.copyOfRange(b, mid, b.length); //n work.

        //we can then recur on our sub-arrays according to scenarios a), b) and c) as defined above.
        //this is gonna be a doozy of a recurisive statement, but bear with me...
        return (
/* condition a) */  (oddEqual(a1, b1) && oddEqual(a2, b2)) ||
/* condition b) */  (oddEqual(a1, b1) && oddEqual(a1, b2)) ||
/* condition c) */  (oddEqual(a2, b1) && oddEqual(a2, b2))
        ); // + 3*T(n/2)

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
		System.out.printf("Reading input values from %s.\n",args[0]);
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
