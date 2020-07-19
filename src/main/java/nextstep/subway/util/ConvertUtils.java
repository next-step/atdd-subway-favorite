package nextstep.subway.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Base64;

public final class ConvertUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static String b64Decode(String raw) {
        return new String(Base64.getDecoder().decode(raw));
    }

    public static <O> String stringify(O object) {
        try {
            return MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }
}
