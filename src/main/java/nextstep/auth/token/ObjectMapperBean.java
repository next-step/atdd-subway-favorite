package nextstep.auth.token;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class ObjectMapperBean {
    private ObjectMapper objectMapper;

    public ObjectMapperBean() {
        this.objectMapper = new ObjectMapper();
    }

    public String writeValueAsString(Object principal) throws JsonProcessingException {
        return objectMapper.writeValueAsString(principal);
    }

    public <T> T readValue(InputStream src, Class<T> valueType) throws IOException {
        return objectMapper.readValue(src, valueType);
    }
}
