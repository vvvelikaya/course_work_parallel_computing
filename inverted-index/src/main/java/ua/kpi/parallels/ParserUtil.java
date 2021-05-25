package ua.kpi.parallels;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ParserUtil {

    private static final List<String> stopWords = Arrays.asList("a", "an", "able", "about",
            "across", "after", "all", "almost", "also", "am", "among", "an",
            "and", "any", "are", "as", "at", "be", "because", "been", "but",
            "by", "can", "cannot", "could", "dear", "did", "do", "does",
            "either", "else", "ever", "every", "for", "from", "get", "got",
            "had", "has", "have", "he", "her", "hers", "him", "his", "how",
            "however", "i", "if", "in", "into", "is", "it", "its", "just",
            "least", "let", "like", "likely", "may", "me", "might", "most",
            "must", "my", "neither", "no", "nor", "not", "of", "off", "often",
            "on", "only", "or", "other", "our", "own", "rather", "said", "say",
            "says", "she", "should", "since", "so", "some", "than", "that",
            "the", "their", "them", "then", "there", "these", "they", "this",
            "to", "too", "twas", "us", "wants", "was", "we", "were",
            "what", "when", "where", "which", "while", "who", "whom", "why",
            "will", "with", "would", "yet", "you", "your", "yours");

    public static Set<String> parseFile(String filePath) throws IOException {
        return Files.lines(Paths.get(filePath))
                .map(line -> line.split("\\s+|\\.|,|;|:|-|\"|'|\\d|\\)|\\(|<|>|/|_|\\?|!|\\W|\\n|\\t"))
                .flatMap(Arrays::stream)
                .map(String::toLowerCase)
                .filter(word -> !stopWords.contains(word))
                .filter(word -> word.length()>2)
                .collect(Collectors.toSet());
    }
}
