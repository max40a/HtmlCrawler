package dropped.task;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class DescriptionFinder extends AbstractPropertyFinder implements PropertyFinder {

    private String searchCriteria = "ПРО КНИЖКУ";

    @Override
    public List<String> findProperty(Document document) {
        List<String> parsedStrings = getParsedStrings(document, "p");
        int start = findStart(parsedStrings, searchCriteria);
        int finish = findFinish(parsedStrings, start);
        return getProperties(parsedStrings, start, finish);
    }

    /*private static List<String> getParsedStrings(Document document) {
        Element annotationElement = document.getElementById("annotation");
        Elements selectedElements = annotationElement.select("p");

        List<String> result = new ArrayList<>();
        for (Element element : selectedElements) {
            result.add(element.text());
        }
        return result;
    }

    private static List<String> getDescription(List<String> strings, int start, int finish) {
        List<String> result = new ArrayList<>();
        for (int i = start; i <= finish; i++) {
            result.add(strings.get(i));
        }
        return result;
    }

    private static int findStart(List<String> strings) {
        int start = 0;
        for (int i = 0; i < strings.size(); i++) {
            if (strings.get(i).compareToIgnoreCase("ПРО КНИЖКУ") == 0) {
                start = i + 1;
            }
        }
        return start;
    }

    private static int findFinish(List<String> strings, int start) {
        int finish = 0;
        for (int i = start; i < strings.size(); i++) {
            if (containsUppercase(strings.get(i))) {
                finish = i - 1;
                break;
            }
        }
        return finish;
    }

    private static boolean containsUppercase(String str) {
        if (str.charAt(0) == '«') {
            str = str.replace(str.charAt(1), Character.toLowerCase(str.charAt(1)));
        }
        return Character.isUpperCase(str.charAt(1));
    }
*/
}
