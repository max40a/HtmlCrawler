package parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPropertyFinder {

    protected List<String> getParsedStrings(Document document, String queryCriteria) {
        Element annotationElement = document.getElementById("annotation");
        Elements selectedElements = annotationElement.select(queryCriteria);

        List<String> result = new ArrayList<>();
        for (Element element : selectedElements) {
            result.add(element.text());
        }
        return result;
    }

    protected  List<String> getProperties(List<String> strings, int start, int finish) {
        List<String> result = new ArrayList<>();
        for (int i = start; i <= finish; i++) {
            result.add(strings.get(i));
        }
        return result;
    }

    protected int findStart(List<String> strings, String searchCriteria) {
        int start = 0;
        for (int i = 0; i < strings.size(); i++) {
            String s = strings.get(i);
            //FIX ME
            if (s.isEmpty() || s.length() < searchCriteria.length()) {
                s += "         ";
            }
            if (s.substring(0, searchCriteria.length()).compareToIgnoreCase(searchCriteria) == 0) {
                start = i + 1;
            }
        }
        return start;
    }

    protected int findFinish(List<String> strings, int start) {
        int finish = 0;
        for (int i = start; i < strings.size(); i++) {
            if (containsUppercase(strings.get(i))) {
                finish = i - 1;
                break;
            }
        }
        return finish;
    }

    private boolean containsUppercase(String str) {
        if (str.charAt(0) == '«') {
            str = str.replace(str.charAt(1), Character.toLowerCase(str.charAt(1)));
        }
        return Character.isUpperCase(str.charAt(1));
    }
}
