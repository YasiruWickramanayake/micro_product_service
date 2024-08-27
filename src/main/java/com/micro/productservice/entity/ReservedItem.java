package com.micro.productservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reserved_item")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ReservedItem {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Integer id;

    @Column(name = "saga_id")
    private String sagaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    private Product product;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "reserved_status")
    private Integer reservedStatus;

    @Column(name = "narration")
    private String narration;

    @Version
    @Column(name = "version")
    private Integer version;
}
