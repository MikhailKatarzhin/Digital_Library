package library.repository;

import library.model.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {

    @Query(
            value = "SELECT * FROM \"Chapter\"" +
                    " WHERE book_id = ?1" +
                    " ORDER BY number",
            nativeQuery = true
    )
    List<Chapter> findByBookId(Long bookId);
}