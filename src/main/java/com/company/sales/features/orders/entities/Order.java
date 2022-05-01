package com.company.sales.features.orders.entities;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
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
@Table(name = "orders")
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 64)
  private String email;

  @Column(nullable = false, length = 256)
  private String address;

  @Column(nullable = false)
  private ZonedDateTime orderedOn;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "order_id", nullable = false)
  @OrderBy("price ASC")
  private List<PurchasedProduct> purchasedProducts = new ArrayList<>();

  public BigDecimal getTotalValue() {
    return purchasedProducts.stream()
        .map(product -> product.getPrice().multiply(BigDecimal.valueOf(product.getQuantity())))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}