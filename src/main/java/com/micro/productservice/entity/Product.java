package com.micro.productservice.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "available_quantity")
    private Integer availableQuantity;

    @Column(name = "price_per_quantity")
    private Double pricePerQuantity;

    @Column(name = "block_quantity")
    private Integer blockQuantity;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<ReservedItem> reservedItems;

    @Version
    @Column(name = "version")
    private Integer version;

}
