package nextstep.auth.model.authentication;

import nextstep.subway.domain.member.MemberAdaptor;

public interface UserDetails {

    String getUsername();

    String getCredential();

    boolean validateCredential(String targetPassword);

    boolean isEnabled();

    UserDetails Anonymous = new MemberAdaptor() {
        @Override
        public boolean isEnabled() {
            return false;
        }

        @Override
        public String getUsername() {
            return null;
        }

        @Override
        public String getCredential() {
            return null;
        }

        @Override
        public boolean validateCredential(String targetPassword) {
            return false;
        }
    };
}
