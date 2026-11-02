package com.clicKart.cart.repository;



import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clicKart.cart.entity.CartItem;



@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByCartId(Long cartId);

    Optional<CartItem> findByCartIdAndProductId(
            Long cartId,
            Long productId
    );

    boolean existsByCartIdAndProductId(
            Long cartId,
            Long productId
    );

    void deleteByCartId(Long cartId);
}
