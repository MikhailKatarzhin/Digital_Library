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
@Table(name = "\"User\"")
@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @ManyToOne(optional = false)
    @JoinColumn(name = "status_id",nullable = false)
    private UserStatus userStatus;

    @ManyToMany
    @JoinTable(name = "\"User_Role\"",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RoleOfUser> roleSet = new LinkedHashSet<>();

    @OneToMany(mappedBy = "creator")
    private Set<BookCycle> bookCycles = new LinkedHashSet<>();

    @OneToMany(mappedBy = "creator")
    private Set<Chapter> chapters = new LinkedHashSet<>();

    @OneToMany(mappedBy = "creator")
    private Set<Book> books = new LinkedHashSet<>();

    @OneToMany(mappedBy = "creator")
    private Set<Universe> universes = new LinkedHashSet<>();

    @OneToMany(mappedBy = "creator")
    private Set<Section> sections = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "consumers")
    private Set<Book> purchasedBooks = new LinkedHashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoleSet();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public boolean hasRole(String roleName){
        for (RoleOfUser role : roleSet)
            if (role.getName().equals(roleName))
                return true;
        return false;
    }
}
