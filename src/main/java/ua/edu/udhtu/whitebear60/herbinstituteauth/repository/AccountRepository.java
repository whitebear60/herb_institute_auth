package ua.edu.udhtu.whitebear60.herbinstituteauth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.edu.udhtu.whitebear60.herbinstituteauth.model.AccountEntity;

public interface AccountRepository
        extends JpaRepository<AccountEntity, Long> {
    AccountEntity findByLogin(String login);
}
