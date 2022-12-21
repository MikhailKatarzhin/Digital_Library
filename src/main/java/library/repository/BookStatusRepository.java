package library.repository;

import library.model.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookStatusRepository extends JpaRepository<BookStatus, Long> {

    @Query(
            value = "SELECT * FROM \"Book_status\"" +
                    " WHERE id = ?"
            , nativeQuery = true
    )
    BookStatus getById(Long statusId);
}