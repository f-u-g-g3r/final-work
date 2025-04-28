package com.artjom.qr_project.repository;

import com.artjom.qr_project.model.Campaign;
import com.artjom.qr_project.model.Company;
import com.artjom.qr_project.model.Partnership;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {
    Page<Campaign> findAllByInitiator(Company initiator, Pageable pageable);
    @Query("""
    SELECT c FROM Campaign c
    JOIN c.partnerships p
    WHERE p IN :partnerships
    AND c.initiator <> :initiator
""")
    Page<Campaign> findAllByPartnershipsInAndInitiatorNot(
            @Param("partnerships") List<Partnership> partnerships,
            @Param("initiator") Company initiator,
            Pageable pageable
    );
}
