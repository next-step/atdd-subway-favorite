package nextstep.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.config.LoginMember;
import nextstep.member.application.dto.MemberResponse;

public class ObjectStringMapper {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private ObjectStringMapper() {
    }

    public static <T> T convertStringToLoginMember(String principal, Class<T> cls) {
        try {
            return objectMapper.readValue(principal, cls);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String convertObjectAsString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
