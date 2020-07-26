package nextstep.subway.auth.dto;

import nextstep.subway.auth.application.UserDetail;

public class UserDetailDto implements UserDetail {

    private Long id;
    private String email;
    private Integer age;

    public UserDetailDto() {
    }

    public UserDetailDto(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    @Override
    public boolean checkPassword(String password) {
        return false;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public Integer getAge() {
        return age;
    }
}
