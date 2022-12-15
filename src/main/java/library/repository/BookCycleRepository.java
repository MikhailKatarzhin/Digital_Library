package library.repository;

import library.model.BookCycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookCycleRepository extends JpaRepository<BookCycle, Long> {
}