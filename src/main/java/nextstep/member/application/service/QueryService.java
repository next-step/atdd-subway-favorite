package nextstep.member.application.service;

public interface QueryService<T> {
    T find(Long id);
    T find(String email);
}
