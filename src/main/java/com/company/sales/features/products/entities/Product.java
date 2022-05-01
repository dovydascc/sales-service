package com.company.sales.features.products.entities;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

@Entity
@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 16)
  private String sku;

  @Column(nullable = false, length = 32)
  private String name;

  @Column(nullable = false, length = 256)
  private String description;

  @Column(nullable = false, scale = 15, precision = 2)
  private BigDecimal price;

  @Column(nullable = false)
  private int warrantyDays;

  @Column(nullable = false)
  private boolean listed;
}
