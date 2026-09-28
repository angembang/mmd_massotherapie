package fr.mmdmassotherapie.backend.auth.dao;

import fr.mmdmassotherapie.backend.auth.model.AdminUser;

import java.util.Optional;

public interface IDAOAuth {
    AdminUser save(AdminUser adminUser);

    Optional<AdminUser> findByEmail(String email);

    boolean existsByEmail(String email);
}
