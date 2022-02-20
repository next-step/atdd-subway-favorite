package nextstep.member.domain;


public class UserDetails {
    private Long id;
    private String email;
    private String password;
    private Integer age;

    protected UserDetails() {
    }

    public UserDetails(Long id, String email, String password, Integer age) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public static UserDetails of(Member member) {
        return new UserDetails(member.getId(), member.getEmail(), member.getPassword(), member.getAge());
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
