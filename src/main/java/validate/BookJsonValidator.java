package validate;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BookJsonValidator {

    public static boolean validateJson(String entityJson, Class entityClass) {
        JsonObject jsonObject = new JsonParser().parse(entityJson).getAsJsonObject();

        List<String> listNamesOfEntityFields = getListNamesOfEntityFields(entityClass);
        for (String field : listNamesOfEntityFields) {
            JsonElement jsonElement = jsonObject.get(field);
            if (jsonElement.isJsonNull()) {
                return false;
            }
            if (jsonElement.isJsonArray()) {
                if (jsonElement.getAsJsonArray().get(0).getAsString().equals("")) {
                    return false;
                }
            }
            if (jsonElement.isJsonObject()) {
                if (jsonElement.getAsJsonObject().get("number").getAsString().equals("")) {
                    return false;
                }
            }
            if (jsonElement.isJsonPrimitive()) {
                if (jsonElement.getAsJsonPrimitive().getAsString().equals("")) {
                    return false;
                }
            }
        }
        return true;
    }

    private static List<String> getListNamesOfEntityFields(Class c) {
        return Stream.of(c.getDeclaredFields())
                .peek(field -> field.setAccessible(true))
                .map(Field::getName)
                .collect(Collectors.toList());
    }
}
