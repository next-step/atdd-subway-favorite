package nextstep.subway.domain.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class LineColor {
    public static final int MAX_COLOR_LENGTH = 20;

    public static final String EMPTY_COLOR_ERROR_MESSAGE = "값이 없는 지하철 노선 색상을 생성할 수 없습니다.";

    public static final String COLOR_LENGTH_ERROR_MESSAGE = "색상은 " + MAX_COLOR_LENGTH + "자 이하여야 합니다.";

    @Column(name = "color", length = 20, nullable = false)
    private String value;

    protected LineColor() {
    }

    public LineColor(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException(EMPTY_COLOR_ERROR_MESSAGE);
        }

        if (value.length() > MAX_COLOR_LENGTH) {
            throw new IllegalArgumentException(COLOR_LENGTH_ERROR_MESSAGE);
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

        LineColor lineName = (LineColor)object;
        return Objects.equals(value, lineName.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}