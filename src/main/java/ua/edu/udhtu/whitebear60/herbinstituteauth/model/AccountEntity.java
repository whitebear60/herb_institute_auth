package ua.edu.udhtu.whitebear60.herbinstituteauth.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name="accounts")
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="login", length = 100, unique = true)
    private String login;

    @Column(name = "sha_password", length = 60)
    private String hashPass;

    @ManyToMany(targetEntity = RoleEntity.class)
    @JoinTable(name = "accounts_access_level", joinColumns = @JoinColumn(name = "account_id"),
    inverseJoinColumns = @JoinColumn(name = "role"))
    private Set<RoleEntity> roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }


    public void setLogin(String login) {
        this.login = login;
    }

    public String getHashPass() {
        return hashPass;
    }

    public void setHashPass(String hashPass) {
        this.hashPass = hashPass;
    }

    public Set<RoleEntity> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleEntity> roles) {
        this.roles = roles;
    }
}
