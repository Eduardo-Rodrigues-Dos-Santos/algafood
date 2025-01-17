package org.algaworks.algafood.domain.models;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

@Entity
@Table(name = "tb_product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    private String name;
    private String description;
    private BigDecimal value;
    private Boolean active;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductPhoto> productPhoto = new LinkedList<>();

    @ManyToOne()
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
}
