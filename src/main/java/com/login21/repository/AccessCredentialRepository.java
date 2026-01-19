package com.login21.repository;

import com.login21.entity.AccessCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccessCredentialRepository extends JpaRepository<AccessCredential, Long>
{
    Optional<AccessCredential> findByUser(String user);
    boolean existsByIdUser(String idUser);
}
