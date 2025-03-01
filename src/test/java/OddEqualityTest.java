import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.*;
import net.jqwik.api.*;
import net.jqwik.api.arbitraries.*;
import net.jqwik.api.constraints.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

public class OddEqualityTest {


    @ParameterizedTest
    @ArgumentsSource(FileArgumentsProvider.class)
    void testMyFunction(int n, int[] array1, int[] array2, boolean expected) {
        boolean result = OddEquality.oddEqual(array1, array2); // Replace with your actual function
        assertEquals(expected, result);
    }


}
