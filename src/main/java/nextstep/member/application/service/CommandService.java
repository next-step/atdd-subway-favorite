package nextstep.member.application.service;

import nextstep.member.application.dto.MemberRequest;

public interface CommandService<T> {
    T create(MemberRequest request);
    void update(Long id, MemberRequest param);
    void update(String email, MemberRequest param);
    void delete(Long id);
    void delete(String email);
}
