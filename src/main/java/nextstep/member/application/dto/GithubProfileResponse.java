package nextstep.member.application.dto;

public class GithubProfileResponse {
    private String mail;
    private int age;

    public GithubProfileResponse() {
    }

    public GithubProfileResponse(String mail, int age) {
        this.mail = mail;
        this.age = age;
    }   

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
