package com.artjom.qr_project.repository;

import com.artjom.qr_project.enums.PartnershipStatus;
import com.artjom.qr_project.model.Company;
import com.artjom.qr_project.model.Partnership;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartnershipRepository extends JpaRepository<Partnership, Long> {
    boolean existsByPartnerIdAndInitiatorIdAndStatus(Long partnerId, Long initiatorId, PartnershipStatus status);

    Page<Partnership> findAllByPartnerAndStatusOrInitiatorAndStatus(Company partner, PartnershipStatus status1, Company initiator, PartnershipStatus status2, Pageable pageable);

    Page<Partnership> findAllByPartner(Company company, Pageable pageable);

    Page<Partnership> findAllByInitiatorAndStatus(Company initiator, PartnershipStatus status, Pageable pageable);

    List<Partnership> findAllByPartnerOrInitiator(Company partner, Company initiator);

    Page<Partnership> findAllByPartnerAndStatus(Company company, PartnershipStatus status, Pageable pageable);
}
