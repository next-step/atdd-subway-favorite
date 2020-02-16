package atdd.path.repository;

import org.springframework.data.repository.CrudRepository;

import atdd.path.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {
}
