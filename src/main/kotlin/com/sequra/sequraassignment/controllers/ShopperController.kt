package com.sequra.sequraassignment.controllers

import com.sequra.sequraassignment.*
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

/**
 * TODO
 * ShopperController:
 * 1. Add validation for
 *    - order creation: validate for presence of shoper and merchant
 * 2. Add security and roles
 * 3. Add logging
 * 4. Add caching
 * 5. Add pagination
 * 6. Add documentation Open API
 * 7. Introduce layers ie ShopperService
 * 8. Add exception handling logic
 * 9. Add delete method to soft delete Merchant, put for full update, patch for partial update
 */
@RestController
class ShopperController(val shopperRepository: ShopperRepository, val orderRepository: OrderRepository) {
    @PostMapping("/api/v0/shopper")
    fun save(@RequestBody shopper: Shopper): ResponseEntity<Shopper> {
        val savedId = shopperRepository.save(
            ShopperEntity(
                id = null,
                orders = null,
                name = shopper.name,
                email = shopper.email,
                nif = shopper.nif
            )
        ).id
        shopper.id = savedId
        return ResponseEntity.created(URI.create("/api/v0/shopper/$savedId")).body(shopper)
    }

    @GetMapping("/api/v0/shopper/{shopperId}")
    fun get(@PathVariable shopperId: Int) = shopperRepository.findById(shopperId)
        .map {
            Merchant(
                id = it.id,
                cif = it.nif,
                email = it.email,
                name = it.name,
            )
        }
        .map { ResponseEntity.created(URI.create("/api/v0/merchant/$shopperId")).body(it) }
        .orElseThrow { RuntimeException("No merchant is found for id: $shopperId") }

    @GetMapping("/api/v0/shopper")
    fun getAll() = shopperRepository.findAll()
        .map {
            Merchant(
                id = it.id,
                cif = it.nif,
                email = it.email,
                name = it.name,
            )
        }
        .map { ResponseEntity.created(URI.create("/api/v0/shopper")).body(it) }


    @GetMapping("/api/v0/shopper/{shopperId}/order")
    fun getAllOrders(@PathVariable shopperId: Int) = orderRepository
        .findAll()
        .filter { it.shopper.id == shopperId }
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
        .map { ResponseEntity.created(URI.create("/api/v0/merchant")).body(it) }
}