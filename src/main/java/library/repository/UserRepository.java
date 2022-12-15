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

    @Query(
            value = "select count(*) from \"User_Role\" ur" +
                    " inner join \"Role_of_User\" r on ur.role_id = r.id" +
                    " where r.name = ?1 AND ur.user_id=?2",
            nativeQuery = true
    )
    long countUserRoleByRoleNameAndUserId(String roleName, long userId);
}
