package com.artjom.qr_project.repository;

import com.artjom.qr_project.enums.Role;
import com.artjom.qr_project.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(@NotBlank(message = "Email is mandatory") @Email(message = "Wrong email format") String email);
    Optional<User> findByUuid(String uuid);
    boolean existsByEmail(String email);


    Page<User> findAllByIsActive(boolean active, Pageable pageable);
    Page<User> findAllByRoleIn(List<Role> role, Pageable pageable);
    Page<User> findAllByCompanyId(Long companyId, Pageable pageable);
    List<User> findAllByCompanyId(Long companyId);

    Page<User> findAllByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrUuidContainsIgnoreCase(String filterValue, String filterValue1, String filterValue2, Pageable pageable);

    Page<User> findAllByCompanyIdAndIsActive(Long companyId, boolean equals, Pageable pageable);

    Page<User> findAllByCompanyIdAndRoleIn(Long companyId, List<Role> filterByRoles, Pageable pageable);

    Page<User> findAllByCompanyIdAndNameContainingIgnoreCaseOrCompanyIdAndEmailContainingIgnoreCaseOrCompanyIdAndUuidContainsIgnoreCase(Long company_id, String name, Long company_id2, String email, Long company_id3, String uuid, Pageable pageable);
}
