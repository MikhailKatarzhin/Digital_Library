package library.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "\"User\"")
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "username", nullable = false, length = 25)
    private String username;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "status_id", nullable = false)
    private UserStatus status;

    @Column(name = "password", nullable = false, length = 60)
    private String password;

    @OneToMany(mappedBy = "creator")
    private Set<BookCycle> myBookCycles = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "\"User_Book\"",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id"))
    private Set<Book> purchasedBooks = new LinkedHashSet<>();

    @OneToMany(mappedBy = "creator")
    private Set<Chapter> myChapters = new LinkedHashSet<>();

    @OneToMany(mappedBy = "creator")
    private Set<Book> myBooks = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "\"User_Role\"",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleOfUser> roleOfUsers = new LinkedHashSet<>();

    @OneToMany(mappedBy = "creator")
    private Set<Universe> myUniverses = new LinkedHashSet<>();

    @OneToMany(mappedBy = "creator")
    private Set<Section> mySections = new LinkedHashSet<>();

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
    private Wallet wallet;

    public boolean hasRole(String roleName){
        for (RoleOfUser role : getRoleOfUsers())
            if (role.getName().equals(roleName))
                return true;
        return false;
    }

    public boolean hasBook(Long bookId){
        for (Book book : getPurchasedBooks())
            if ((long)book.getId() == (long)bookId)
                return true;
        return false;
    }
}