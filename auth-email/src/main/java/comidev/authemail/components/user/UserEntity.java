package comidev.authemail.components.user;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import comidev.authemail.components.role.Role;
import comidev.authemail.components.user.dto.UserCreate;
import comidev.authemail.components.user.dto.UserLogin;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Entity(name = "User")
@Table(name = "users")
@ToString
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = true)
    private String password;

    @Column(nullable = false)
    private Boolean locked = false;

    @Column(nullable = false)
    private Boolean enabled = false;

    @ManyToMany
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(referencedColumnName = "id", name = "user_id"), inverseJoinColumns = @JoinColumn(referencedColumnName = "id", name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public UserEntity(Long id) {
        this.id = id;
    }

    public UserEntity(UserCreate userCreate) {
        this.name = userCreate.getName();
        this.email = userCreate.getEmail();

        UserLogin userLogin = userCreate.getUser();

        this.username = userLogin.getUsername();
        this.password = userLogin.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = this.roles.stream()
                .map(role -> {
                    String roleName = "ROLE_" + role.getName().toString();
                    return new SimpleGrantedAuthority(roleName);
                })
                .toList();
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
