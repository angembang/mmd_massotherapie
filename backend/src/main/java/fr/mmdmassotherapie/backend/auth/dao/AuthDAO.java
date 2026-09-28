package fr.mmdmassotherapie.backend.auth.dao;

import fr.mmdmassotherapie.backend.auth.model.AdminUser;
import fr.mmdmassotherapie.backend.auth.repository.AdminUserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AuthDAO implements IDAOAuth {
    private final AdminUserRepository adminUserRepository;

    public AuthDAO(AdminUserRepository adminUserRepository) {
        this.adminUserRepository = adminUserRepository;
    }

    @Override
    public AdminUser save(AdminUser adminUser) {
        return adminUserRepository.save(adminUser);
    }

    @Override
    public Optional<AdminUser> findByEmail(String email) {
        return adminUserRepository.findByEmail(email);
    }

    @Override
    public boolean existsByEmail(String email) {
        return adminUserRepository.existsByEmail(email);
    }
}
