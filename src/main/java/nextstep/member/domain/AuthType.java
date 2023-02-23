package nextstep.member.domain;

public interface AuthType {
    boolean match(String header);
    Member findMember(String header);
    void validate(String header);
}
