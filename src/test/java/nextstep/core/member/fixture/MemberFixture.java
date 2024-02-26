package nextstep.core.member.fixture;

public enum MemberFixture {
    SMITH("smith@email.com", "smith_password", 20),
    JOHNSON("johnson@email.com", "johnson_password", 25),
    WILLIAMS("williams@email.com", "williams_password", 30),
    BROWN("brown@email.com", "brown_password", 50);

    public final String email;
    public final String password;
    public final int age;

    MemberFixture(String email, String password, int age) {
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getAge() {
        return age;
    }
}
