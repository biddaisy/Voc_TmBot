package by.iba.bot.vocabulary.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
@Getter
@Setter
class Chunk {
    String id;
    String word;
    String translation;

    @Override
    public String toString() {
        return "{\n" +
                " \"_id\" : " + id + ",\n" +
                " \"word\" : " + "\"" + word + "\",\n" +
                " \"translations\" : [{" + "\"en\" : " + "\"" + translation + "\"" + "}]\n" +
                "}";
    }
}

public class Converter {

    static String inputFile = "dumps\\words.draft.txt";
    static String outputFile = "dumps\\words.draft.json";

    @SneakyThrows
    public static void main(String[] args) {
        Path input = Paths.get(inputFile);
        try (Stream<String> stream = Files.lines(input)) {
            Path output = Paths.get(outputFile);

            String str = stream.map(s -> {
                var arr = s.split("\\t");
                Chunk c = new Chunk(arr[0].trim(), arr[1].trim(), arr[2].trim());
                return c.toString();
            }).collect(Collectors.joining(",\n"));
            Files.writeString(output, str);
        }
    }
}
