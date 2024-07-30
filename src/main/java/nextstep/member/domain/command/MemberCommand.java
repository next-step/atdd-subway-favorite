package nextstep.member.domain.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import nextstep.member.domain.entity.Member;

public class MemberCommand {

    @ToString
    @Getter
    @AllArgsConstructor
    public static class CreateMember {
        private String email;
        private String password;
        private Integer age;

        public Member toMember() {
            return new Member(email, password, age);
        }
    }

    @ToString
    @Getter
    @AllArgsConstructor
    public static class UpdateMember {
        private Long id;
        private String email;
        private String password;
        private Integer age;

        public Member toMember() {
            return new Member(email, password, age);
        }
    }
}
