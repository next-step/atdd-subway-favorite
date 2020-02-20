package atdd.user.domain;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Email {

    private static final Pattern EMAIL_REG_EXP = Pattern.compile(".+@.+\\..+");

    private String emailAddress;

    public Email(String emailAddress) {
        checkEmailForm(emailAddress);
        this.emailAddress = emailAddress;
    }

    private void checkEmailForm(String emailAddress) {
        final Matcher matcher = EMAIL_REG_EXP.matcher(emailAddress);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("이메일 형식에 맞지 않습니다. emailAddress : [" + emailAddress + "]");
        }
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Email)) return false;
        Email email = (Email) o;
        return Objects.equals(emailAddress, email.emailAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(emailAddress);
    }

    @Override
    public String toString() {
        return "Email{" +
                "emailAddress='" + emailAddress + '\'' +
                '}';
    }

}
