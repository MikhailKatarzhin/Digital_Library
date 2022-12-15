package library.repository;

import library.model.RoleOfUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleOfUserRepository extends JpaRepository<RoleOfUser, Long> {

    @Query(
            value = "select * from \"Role_of_User\" r where r.name <> ?",
            nativeQuery = true
    )
    List<RoleOfUser> findAllExceptName(String exceptingName);

    @Query(
            value = "select * from \"Role_of_User\" r where r.name = ?",
            nativeQuery = true
    )
    RoleOfUser findByName(String Name);
}
