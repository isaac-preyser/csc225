import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

public class OddEqualityTest {


    @ParameterizedTest
    @ArgumentsSource(FileArgumentsProvider.class)
    void testOddEquals(int ignoredN, int[] array1, int[] array2, boolean expected) {
        assertThat(OddEquality.oddEqual(array1, array2)).isEqualTo(expected);
    }


}
