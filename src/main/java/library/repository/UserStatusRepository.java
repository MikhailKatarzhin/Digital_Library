package library.repository;

import library.model.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStatusRepository extends JpaRepository<UserStatus, Long> {

    @Query(
            value = "select * from \"User_status\" r where r.name = ?",
            nativeQuery = true
    )
    UserStatus findByName(String userStatusName);
}
