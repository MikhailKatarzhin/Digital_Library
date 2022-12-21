package library.repository;

import library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query(
            value = "select COUNT(*) from \"Book\"" +
                    " WHERE id=?1",
            nativeQuery = true
    )
    Long countById(Long bookId);

    @Query(
            value = "select * from \"Book\"" +
                    " WHERE id=?1",
            nativeQuery = true
    )
    Book getById(Long bookId);

    @Query(
            value = "SELECT COUNT(*) FROM \"Book\""
            , nativeQuery = true
    )
    Long countBy(Long creatorId);

    @Query(
            value = "SELECT COUNT(*) FROM \"Book\"" +
                    " WHERE creator_id = ?"
            , nativeQuery = true
    )
    Long countByCreatorId(Long creatorId);

    @Query(
            value = "SELECT COUNT(*) FROM \"Book\" b" +
                    " INNER JOIN \"User\" u on b.creator_id = u.id" +
                    " INNER JOIN \"Book_status\" s on b.book_status_id = s.id" +
                    " AND s.name LIKE ?1" +
                    " WHERE u.username LIKE ?2" +
                    " AND b.name LIKE ?3" +
                    " AND b.cost >= ?4 AND b.cost <= ?5" +
                    " AND b.year_of_upload >= ?6 AND b.year_of_upload <= ?7" +
                    " AND b.year_of_creation >= ?8 AND b.year_of_creation <= ?9"
            , nativeQuery = true
    )
    Long countBookByBookSearchRequest(String statusName, String username,String BookName,
                                      long minCost, long maxCost,
                                      long minYearOfUpload, long maxYearOfUpload,
                                      long minYearOfCreation, long maxYearOfCreation
    );

    @Query(
            value = "SELECT * FROM \"Book\" b" +
                    " INNER JOIN \"User\" u on b.creator_id = u.id" +
                    " INNER JOIN \"Book_status\" s on b.book_status_id = s.id" +
                    " WHERE s.name ILIKE ?1" +
                    " AND u.username ILIKE ?2" +
                    " AND b.name ILIKE ?3" +
                    " AND b.cost >= ?4 AND b.cost <= ?5" +
                    " AND b.year_of_upload >= ?6 AND b.year_of_upload <= ?7" +
                    " AND b.year_of_creation >= ?8 AND b.year_of_creation <= ?9" +
                    " ORDER BY b.name" +
                    " LIMIT ?10 OFFSET ?11"
            , nativeQuery = true
    )
    List<Book> getBookListByBookSearchRequest(String statusName, String username, String BookName,
                                              long minCost, long maxCost,
                                              short minYearOfUpload, short maxYearOfUpload,
                                              short minYearOfCreation, short maxYearOfCreation,
                                              long limit, long offset
    );
}