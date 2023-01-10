package ua.foxminded.warehouse.models.auth;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import ua.foxminded.warehouse.models.auth.RegisteredUser;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name="roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(callSuper = true, includeFieldNames = true)
public class Role implements GrantedAuthority {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @Column(name="name")
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Collection<RegisteredUser> registeredUsers;

    @Override
    public String getAuthority() {
        return name;
    }
}
