package nextstep.exception;

import java.io.IOException;

public class JsonDeserializeFailed extends IOException {

    public JsonDeserializeFailed(String message) {
        super(message);
    }

}
