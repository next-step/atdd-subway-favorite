package atdd.path.application;

import atdd.path.application.dto.UserResponseDto;
import atdd.path.application.exception.UserNotFoundException;
import atdd.path.domain.User;
import atdd.path.domain.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserResponseDto create(String email,
                                  String name,
                                  String password) {

        User savedUser = userRepository.save(User.of(email, name, password));
        return UserResponseDto.of(savedUser.getId(), savedUser.getEmail(), savedUser.getName());
    }

    @Transactional(readOnly = true)
    public UserResponseDto getUser(Long id) {
        User savedUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        return UserResponseDto.of(savedUser.getId(), savedUser.getEmail(), savedUser.getName());
    }

    @Transactional
    public void delete(Long id) {
        try {
            User savedUser = userRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

            savedUser.delete();
        } catch (IllegalArgumentException e) {
            throw new UserNotFoundException("해당 사용자가 존재하지 않습니다.");
        }
    }
}
