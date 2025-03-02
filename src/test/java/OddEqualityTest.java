import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import net.jqwik.api.*;

import java.util.Arrays;
import java.util.stream.IntStream;

public class OddEqualityTest {


    @ParameterizedTest
    @ArgumentsSource(FileArgumentsProvider.class)
    void testOddEquals(int ignoredN, int[] array1, int[] array2, boolean expected) {
        assertThat(OddEquality.oddEqual(array1, array2)).isEqualTo(expected);
    }

    @Provide
    Arbitrary<Tuple.Tuple2<int[], int[]>> validOddEqualArrays() {
        return Arbitraries.integers().between(1, 100).flatMap(n -> {
            if (n % 2 == 1) n++; // Ensure even size for Condition II cases

            Arbitrary<int[]> baseArray = Arbitraries.integers().between(-1000, 1000).array(int[].class).ofSize(n);

            return baseArray.flatMap(A -> {
                // Option 1: A == B
                Arbitrary<int[]> identicalB = Arbitraries.of(A);

                // Option 2: Generate a valid B that satisfies one of the conditions recursively
                Arbitrary<int[]> recursiveB = generateValidB(A);

                return Arbitraries.oneOf(identicalB, recursiveB)
                        .map(B -> Tuple.of(A, B));
            });
        });
    }

    private Arbitrary<int[]> generateValidB(int[] A) {
        int n = A.length;
        if (n % 2 == 1) return Arbitraries.of(A); // Odd-length should default to A

        int mid = n / 2;
        int[] A1 = Arrays.copyOfRange(A, 0, mid);
        int[] A2 = Arrays.copyOfRange(A, mid, n);

        // Generate B1 and B2
        Arbitrary<int[]> B1 = Arbitraries.oneOf(Arbitraries.of(A1), generateValidB(A1));
        Arbitrary<int[]> B2 = Arbitraries.oneOf(Arbitraries.of(A2), generateValidB(A2));

        return Arbitraries.oneOf(
                B1.flatMap(b1 -> B2.map(b2 -> concatenate(b1, b2))),
                B1.flatMap(b1 -> B1.map(b2 -> concatenate(b1, b2))),
                B2.flatMap(b1 -> B2.map(b2 -> concatenate(b1, b2)))
        );
    }

    private int[] concatenate(int[] a, int[] b) {
        return IntStream.concat(Arrays.stream(a), Arrays.stream(b)).toArray();
    }

    @Property
    void testOddEqualProperty(@ForAll("validOddEqualArrays") Tuple.Tuple2<int[], int[]> arrays) {
        int[] A = arrays.get1();
        int[] B = arrays.get2();
//        System.out.println("A: " + Arrays.toString(A) + ", B: " + Arrays.toString(B));
        assertThat(OddEquality.oddEqual(A, B)).isTrue();
    }
}






