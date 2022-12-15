package library.model;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "\"Book_status\"")
public class BookStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 15)
    private String name;

    @OneToMany(mappedBy = "bookStatus")
    private Set<Book> books = new LinkedHashSet<>();
}