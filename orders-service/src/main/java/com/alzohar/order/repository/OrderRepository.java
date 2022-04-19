package com.alzohar.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.alzohar.order.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

	@Query("Select o FROM Order o WHERE o.name =:name")
	public Order getUserByUsername(@Param("name") String name);

}
