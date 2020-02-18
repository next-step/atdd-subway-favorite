package atdd.path.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT * FROM USER WHERE email=?", nativeQuery = true)
    User findByEmail(String email);
}

