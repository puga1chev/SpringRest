package ru.puga1chev.springrest.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity(name = "users")
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username", unique = false, updatable = true)
    private String username;
    @Column(name = "login", unique = true, updatable = true)
    private String login;
    @Column(name = "pass", unique = false, updatable = true)
    private String pass;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    )
    private Collection<Role> roles;

    public User() {
    }

    public User(Long id, String username, String login, String pass, Collection<Role> roles) {
        this.id = id;
        this.username = username;
        this.login = login;
        this.pass = pass;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> roles = new ArrayList<>();
        getRoles().stream().forEach(x -> roles.add(new SimpleGrantedAuthority(x.getAuthority())));
        return roles;
    }

    @Override
    public String getPassword() {
        return getPass();
    }

    public String getUsername() {
        return username;
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

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isAdmin() {
        for (Role role : getRoles()) {
            if (role.getRolename().equals("ADMIN")){
                return true;
            }
        }
        return false;
    }
}
