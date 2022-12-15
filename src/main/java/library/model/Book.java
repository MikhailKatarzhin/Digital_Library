package library.model;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "\"Book\"")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_cycle_id", nullable = false)
    private BookCycle bookCycle;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_status_id", nullable = false)
    private BookStatus bookStatus;

    @ManyToMany
    @JoinTable(name = "\"Book_Tag\"",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags = new LinkedHashSet<>();

    @OneToMany(mappedBy = "book")
    private Set<Chapter> chapters = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "\"User_Book\"",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> consumers = new LinkedHashSet<>();

    @Column(name = "year_of_creation")
    private Short yearOfCreation;

    @Column(name = "year_of_upload")
    private Short yearOfUpload;

    @Column(name = "cost", nullable = false)
    private Long cost;

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }
}