package atdd.user.service;

import atdd.user.dao.UserDao;
import atdd.user.domain.Email;
import atdd.user.domain.User;
import atdd.user.dto.UserCreateRequestDto;
import atdd.user.dto.UserResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Transactional
    public UserResponseDto create(UserCreateRequestDto requestDto) {
        User user = User.create(new Email(requestDto.getEmail()), requestDto.getName(), requestDto.getPassword());
        final User createdUser = userDao.create(user);
        return UserResponseDto.of(createdUser.getId(), createdUser.getEmail(), createdUser.getName(), createdUser.getPassword());
    }

    @Transactional
    public void delete(Long id) {
        userDao.delete(id);
    }

}
