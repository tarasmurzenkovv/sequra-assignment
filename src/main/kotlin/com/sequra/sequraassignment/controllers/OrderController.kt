package com.sequra.sequraassignment.controllers

import com.sequra.sequraassignment.Merchant
import com.sequra.sequraassignment.MerchantEntity
import com.sequra.sequraassignment.Order
import com.sequra.sequraassignment.OrderEntity
import com.sequra.sequraassignment.repositories.MerchantRepository
import com.sequra.sequraassignment.repositories.OrderRepository
import com.sequra.sequraassignment.repositories.ShopperRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import java.time.LocalDateTime

/**
 * TODO
 * OrderController:
 * 1. Add validation for
 *    - order creation: validate for presence of shooper and merchant
 * 2. Add security and roles
 * 3. Add logging
 * 4. Add caching
 * 5. Add pagination
 * 6. Add documentation Open API
 * 7. Introduce layers ie OrderService
 * 8. Add exception handling logic
 * 9. Add delete method to soft delete order
 */
@RestController
class OrderController(val orderRepository: OrderRepository,
                      val shopperRepository: ShopperRepository,
                      val merchantRepository: MerchantRepository) {
    @PostMapping("/api/v0/order")
    fun save(@RequestBody order: Order): ResponseEntity<Order> {
        val savedId = orderRepository.save(
            OrderEntity(
                id = null,
                merchant = merchantRepository.getById(order.merchantId),
                shopper = shopperRepository.getById(order.shopperId),
                amount = order.amount,
                createdAt = LocalDateTime.now(),
                completedAt = null,
                disburse = null,
            )
        ).id
        order.id = savedId
        return ResponseEntity.created(URI.create("/api/v0/order/$savedId")).body(order)
    }

    @GetMapping("/api/v0/order{orderId}")
    fun get(@PathVariable orderId: Int) = orderRepository
        .findById(orderId)
        .map {
            Order(
                id = it.id,
                amount = it.amount,
                createdAt = it.createdAt,
                completedAt = it.completedAt,
                merchantId = it.merchant.id!!,
                shopperId = it.shopper.id!!,
            )
        }
        .map { ResponseEntity.created(URI.create("/api/v0/order/$orderId")).body(it) }
        .orElseThrow { RuntimeException("No order is found for id: $orderId") }


}