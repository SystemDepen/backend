package com.github.sysdepen.depen_api.repository;


import com.github.sysdepen.depen_api.entity.Documents;
import com.github.sysdepen.depen_api.security.auth.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Documents, Long> {
    List<Documents> findByUserId(Long userId);
    Optional<Documents> findByUserIdAndDocumentType(Long userId, String documentType);
    Optional<Documents> findByUser(Usuario user);

    List<Documents> findAllByUserId(Long userId);
}
