package library.repository;

import library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    User findByUsername(String username);

    @Query(
            value = "select count(*) from \"User\" where email = ?",
            nativeQuery = true
    )
    Long countByEmail(String email);
}
