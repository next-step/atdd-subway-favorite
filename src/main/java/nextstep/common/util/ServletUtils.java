package nextstep.common.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ServletUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private ServletUtils() {}

    public static <T> T readJson(HttpServletRequest request, Class<T> clazz) throws IOException {
        return objectMapper.readValue(readContent(request), clazz);
    }

    public static byte[] readContent(HttpServletRequest request) throws IOException {
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(request.getInputStream())) {
            byte[] buffer = new byte[128];
            try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                while (bufferedInputStream.read(buffer) > 0) {
                    byteArrayOutputStream.write(buffer);
                }
                return byteArrayOutputStream.toByteArray();
            }
        }
    }
}
