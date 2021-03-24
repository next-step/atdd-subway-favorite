package nextstep.subway.auth.domain;

public class UserDetails {
    private Long id;
    private String email;
    private String password;
    private Integer age;

    public static class Builder {
        private Long id;
        private String email;
        private String password;
        private Integer age;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder age(Integer age) {
            this.age = age;
            return this;
        }

        public UserDetails build() {
            return new UserDetails(this);
        }
     }

    private UserDetails(Builder builder) {
        this.id = builder.id;
        this.email = builder.email;
        this.password = builder.password;
        this.age = builder.age;
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

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

}
