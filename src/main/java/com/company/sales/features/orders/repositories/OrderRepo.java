package com.company.sales.features.orders.repositories;

import com.company.sales.features.orders.entities.Order;
import java.time.ZonedDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {

  Page<Order> findAllByOrderedOnAfterAndOrderedOnBefore(
      Pageable pageable,
      ZonedDateTime dateTimeFrom,
      ZonedDateTime dateTimeTo
  );
}
