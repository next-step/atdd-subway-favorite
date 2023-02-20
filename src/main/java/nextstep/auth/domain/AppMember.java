package nextstep.auth.domain;

public class AppMember {
    private Long id;
    private String email;
    private Integer age;

    public AppMember(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
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
}
