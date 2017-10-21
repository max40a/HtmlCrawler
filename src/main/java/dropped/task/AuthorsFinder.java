package dropped.task;

import org.jsoup.nodes.Document;

import java.util.List;

public class AuthorsFinder extends AbstractPropertyFinder implements PropertyFinder {

    private String searchCriteria = "ПРО АВТОР";

    @Override
    public List<String> findProperty(Document document) {
        List<String> parsedStrings = getParsedStrings(document, "p > strong");
        int start = findStart(parsedStrings, searchCriteria);
        int finish = findFinish(parsedStrings, start);
        return getProperties(parsedStrings, start, finish);
    }

   /* private static List<String> getParsedStrings(Document document) {
        Element annotationElement = document.getElementById("annotation");
        Elements selectedElements = annotationElement.select("p > strong");

        List<String> result = new ArrayList<>();
        for (Element element : selectedElements) {
            result.add(element.text());
        }
        return result;
    }

    private static List<String> getAuthors(List<String> strings, int start, int finish) {
        List<String> result = new ArrayList<>();
        for (int i = start; i <= finish; i++) {
            result.add(strings.get(i));
        }
        return result;
    }

    private static int findStart(List<String> strings, String searchCriteria) {
        int start = 0;
        for (int i = 0; i < strings.size(); i++) {
            String s = strings.get(i);
            if (s.isEmpty() || s.length() < searchCriteria.length()) {
                s += "         ";
            }
            if (s.substring(0, searchCriteria.length()).compareToIgnoreCase(searchCriteria) == 0) {
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
        return Character.isUpperCase(str.charAt(1));
    }*/
}
