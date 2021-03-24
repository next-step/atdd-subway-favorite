package nextstep.subway.auth.dto;

public class UserPrincipal {
    private Long id;
    private String principal;
    private int age;

    public UserPrincipal(){ }

    public UserPrincipal(Long id, String principal, int age) {
        this.id = id;
        this.principal = principal;
        this.age = age;
    }

    public String getPrincipal() {
        return principal;
    }

    public Long getId() {
        return id;
    }

    public int getAge() {
        return age;
    }
}
