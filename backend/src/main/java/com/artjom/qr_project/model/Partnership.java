package com.artjom.qr_project.model;


import com.artjom.qr_project.enums.PartnershipStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
@Entity
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "partnerships")
public class Partnership {
    private @GeneratedValue(strategy = GenerationType.IDENTITY) @Id Long id;

    @ManyToOne
    @JoinColumn(name = "initiator_id", nullable = false)
    private Company initiator;

    @ManyToOne
    @JoinColumn(name = "partner_id", nullable = false)
    private Company partner;

    private LocalDateTime createdAt;

    @ManyToMany(mappedBy = "partnerships")
    private List<Campaign> campaigns = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private PartnershipStatus status = PartnershipStatus.PENDING;
}
