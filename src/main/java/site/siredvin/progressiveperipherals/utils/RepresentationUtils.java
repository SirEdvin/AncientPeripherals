package site.siredvin.progressiveperipherals.utils;

import java.util.Collection;

public class RepresentationUtils {

    public static <T> String mergeValues(Collection<T> objects) {
        StringBuilder builder = new StringBuilder();
        for (T obj: objects) {
            builder.append(obj.toString());
            builder.append(",");
        }
        builder.deleteCharAt(builder.lastIndexOf(","));
        return builder.toString();
    }
}
