package nextstep.member.domain.command;

public interface TokenGenerator {
    String createToken(String principal);
}
