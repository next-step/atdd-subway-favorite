package atdd.path.application;

import atdd.path.application.dto.User.*;
import atdd.path.application.exception.ExistUserException;
import atdd.path.application.exception.NotExistUserException;
import atdd.path.dao.UserDao;
import atdd.path.security.TokenAuthenticationService;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserDao userDao;
    private TokenAuthenticationService tokenAuthenticationService;

    public UserService(TokenAuthenticationService tokenAuthenticationService, UserDao userDao) {
        this.tokenAuthenticationService = tokenAuthenticationService;
        this.userDao = userDao;
    }

    public UserSighUpResponseView singUp(UserSighUpRequestView newUser) {
        FindByEmailResponseView user = userDao.findByEmail(newUser.getEmail());

        if (!isExistUser(user)) {
            throw new ExistUserException();
        }

        return UserSighUpResponseView.toDtoEntity(userDao.save(UserSighUpRequestView.toEntity(newUser)));
    }

    public UserLoginResponseView login(UserLoginRequestView newUser) {
        FindByEmailResponseView user = userDao.findByEmail(newUser.getEmail());

        if (isExistUser(user)) {
            throw new NotExistUserException();
        }

        String jwt = tokenAuthenticationService.toJwtByEmail(user.getEmail());
        return UserLoginResponseView.toDtoEntity(jwt, tokenAuthenticationService.getTokenTypeByJwt(jwt));
    }

    private boolean isExistUser(FindByEmailResponseView user) {
        return user.getId() == null;
    }

    public UserDetailResponseView findById(Long id) {
        return UserDetailResponseView.toDtoEntity(userDao.findById(id));
    }
}
