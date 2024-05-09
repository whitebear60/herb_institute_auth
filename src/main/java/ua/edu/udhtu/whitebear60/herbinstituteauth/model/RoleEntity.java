package ua.edu.udhtu.whitebear60.herbinstituteauth.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "roles")
public class RoleEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "role",length = 25, unique = true, nullable = false, updatable = false)
    private String role;

    @ManyToMany(targetEntity = AccountEntity.class, mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<AccountEntity> accounts;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Set<AccountEntity> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<AccountEntity> accounts) {
        this.accounts = accounts;
    }
}
