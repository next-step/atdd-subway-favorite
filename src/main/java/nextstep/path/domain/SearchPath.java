package nextstep.path.domain;

public class SearchPath {

    private final Long source;

    private final Long target;

    public SearchPath(final Long source, final Long target) {
        this.source = source;
        this.target = target;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof SearchPath)) return false;

        final SearchPath that = (SearchPath) o;

        if (!source.equals(that.source)) return false;
        return target.equals(that.target);
    }

    @Override
    public int hashCode() {
        int result = source.hashCode();
        result = 31 * result + target.hashCode();
        return result;
    }
}
