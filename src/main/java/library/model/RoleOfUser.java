package library.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@RequiredArgsConstructor
@Table(name = "\"Role_of_User\"")
@Entity
public class RoleOfUser implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private String name;

    @ManyToMany
    @JoinTable(name = "\"User_Role\"",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<RoleOfUser> roleSet = new LinkedHashSet<>();

    @Override
    public String getAuthority() {
        return getName();
    }
}
