package com.artjom.qr_project.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "campaigns")
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "initiator_id", nullable = false)
    private Company initiator;

    @ManyToMany
    @JoinTable(
            name = "campaign_partnerships",
            joinColumns = @JoinColumn(name = "campaign_id"),
            inverseJoinColumns = @JoinColumn(name = "partnership_id")
    )
    private List<Partnership> partnerships;

    private String title;

    private String description;

    private Integer discountPercentage;

    private LocalDateTime createdAt;

    private LocalDateTime validUntil;
}
