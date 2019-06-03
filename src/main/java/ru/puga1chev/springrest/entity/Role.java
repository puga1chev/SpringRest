package ru.puga1chev.springrest.entity;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

// TODO Analyze whole proj
@Entity
@Table(name = "getAllRoles")
public class Role implements GrantedAuthority {

    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "rolename", unique = true, updatable = true)
    private String rolename;

/*    @ManyToMany(mappedBy = "getAllRoles")
    private List<User> users;*/

    public Role(Long id, String rolename) {
        setId(id);
        setRolename(rolename);
    }

    public Role(String rolename) {
        setRolename(rolename);
    }

    public Role(Long id) {
        setId(id);
    }

    public Role() {
    }

    public Long getId() {
        return id;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return getRolename();
    }

    @Override
    public String getAuthority() {
        return "ROLE_" + getRolename();
    }
}
