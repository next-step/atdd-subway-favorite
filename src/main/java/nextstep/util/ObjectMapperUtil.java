package nextstep.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletInputStream;
import java.io.IOException;

public class ObjectMapperUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private ObjectMapperUtil() {
    }

    public static String writeValueAsString(Object value) throws IOException {
        return objectMapper.writeValueAsString(value);
    }

    public static <T> T readValue(ServletInputStream inputStream, Class<T> type) throws IOException {
        return objectMapper.readValue(inputStream, type);
    }
}
