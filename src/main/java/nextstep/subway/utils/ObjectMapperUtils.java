package nextstep.subway.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class ObjectMapperUtils {
    private static final ObjectMapper OBJECT_MAPPER = JsonMapper.builder()
            .enable(JsonWriteFeature.WRITE_NUMBERS_AS_STRINGS)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .build();

    public static <T> T convert(String json, Class<T> clazz) throws Exception {
        return OBJECT_MAPPER.readValue(json, clazz);
    }

    public static String convertAsString(Object object) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(object);
    }

    public static void writeStream(OutputStream out, Object object) throws IOException {
        OBJECT_MAPPER.writeValue(out, object);
    }
}
