package com.zufar.icedlatte.order.repository;

import com.zufar.icedlatte.openapi.dto.OrderStatus;
import com.zufar.icedlatte.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    @Query(value = "SELECT o FROM Order o WHERE o.userId = :userId AND o.status IN :statusList")
    List<Order> findAllByUserIdAndStatus(@Param("userId") UUID userId,
                                         @Param("statusList") List<OrderStatus> statusList);

    Optional<Order> findByUserIdAndId(@Param("userId") UUID userId,
                                      @Param("orderId") UUID orderId);

    @Modifying
    @Query(value = "UPDATE orders SET status = :order_status WHERE id = :id", nativeQuery = true)
    void updateOrderStatus(@Param("id") UUID orderId,
                           @Param("order_status") String orderStatus);
}
