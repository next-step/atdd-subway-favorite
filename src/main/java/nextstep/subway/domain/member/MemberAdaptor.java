package nextstep.subway.domain.member;


public class MemberAdaptor {
    private Long id;
    private String email;
    private String password;
    private Integer age;

    public static MemberAdaptor of(Member member) {
        return new MemberAdaptor(member.getId(), member.getEmail(), member.getPassword(), member.getAge());
    }

    public MemberAdaptor(Long id, String email, String password, Integer age) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }

    public String getPassword() {
        return password;
    }
}
