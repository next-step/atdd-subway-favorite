package nextstep.core.member.fixture;

public enum MemberFixture {
    스미스("smith@email.com", "smith_password", 20),
    존슨("johnson@email.com", "johnson_password", 25),
    윌리엄스("williams@email.com", "williams_password", 30),
    브라운("brown@email.com", "brown_password", 50);

    public final String 이메일;
    public final String 비밀번호;
    public final int 나이;

    MemberFixture(String 이메일, String 비밀번호, int 나이) {
        this.이메일 = 이메일;
        this.비밀번호 = 비밀번호;
        this.나이 = 나이;
    }
}
