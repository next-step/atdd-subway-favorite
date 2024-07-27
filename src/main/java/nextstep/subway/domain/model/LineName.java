package nextstep.subway.domain.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class LineName {
    public static final int MIN_NAME_LENGTH = 3;
    public static final int MAX_NAME_LENGTH = 10;

    public static final String EMPTY_NAME_ERROR_MESSAGE = "이름이 없는 지하철 노선은 생성할 수 없습니다.";
    public static final String NAME_LENGTH_ERROR_MESSAGE = "이름은 " + MIN_NAME_LENGTH + "자 이상 " + MAX_NAME_LENGTH + "자 이하여야 합니다.";


    @Column(name = "name", length = 10, nullable = false)
    private String value;

    protected LineName() {
    }

    public LineName(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException(EMPTY_NAME_ERROR_MESSAGE);
        }

        if (value.length() < MIN_NAME_LENGTH || value.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException(NAME_LENGTH_ERROR_MESSAGE);
        }

        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        LineName lineName = (LineName)object;
        return Objects.equals(value, lineName.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
