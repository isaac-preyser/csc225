import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.Collectors;

public class FileArgumentsProvider implements ArgumentsProvider {
    private static final String INPUT_DIR = "src/test/resources/test_files/input/";
    private static final String OUTPUT_DIR = "src/test/resources/test_files/output/";

    @Override
    public Stream<? extends Arguments> provideArguments(org.junit.jupiter.api.extension.ExtensionContext context) throws Exception {
        try {
            // Find all inputXX.txt files
            List<Path> inputFiles = Files.list(Paths.get(INPUT_DIR))
                    .filter(path -> path.getFileName().toString().matches("input\\d{2}\\.txt"))
                    .sorted()
                    .collect(Collectors.toList());

            return inputFiles.stream().map(inputFile -> {
                try {
                    String fileName = inputFile.getFileName().toString(); // e.g., "input01.txt"
                    String outputFileName = fileName.replace("input", "output"); // "output01.txt"
                    Path outputFilePath = Paths.get(OUTPUT_DIR, outputFileName);

                    // Read input file
                    List<String> lines = Files.readAllLines(inputFile);
                    int n = Integer.parseInt(lines.get(0).trim());
                    int[] array1 = Arrays.stream(lines.get(1).split(" ")).mapToInt(Integer::parseInt).toArray();
                    int[] array2 = Arrays.stream(lines.get(2).split(" ")).mapToInt(Integer::parseInt).toArray();

                    // Read expected output file
                    String output = Files.readString(outputFilePath).trim();
                    boolean expected = output.equalsIgnoreCase("YES");

                    return Arguments.of(n, array1, array2, expected);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to read test files", e);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException("Failed to list test files in directory", e);
        }
    }
}
