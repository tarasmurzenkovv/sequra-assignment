package com.sequra.sequraassignment.controllers

import com.sequra.sequraassignment.Merchant
import com.sequra.sequraassignment.MerchantEntity
import com.sequra.sequraassignment.Order
import com.sequra.sequraassignment.repositories.MerchantRepository
import com.sequra.sequraassignment.repositories.OrderRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.net.URI


/**
 * TODO
 * MerchantController:
 * 1. Add validation for
 *    - order creation: validate for presence of shoper and merchant
 * 2. Add security and roles
 * 3. Add logging
 * 4. Add caching
 * 5. Add pagination
 * 6. Add documentation Open API
 * 7. Introduce layers ie MerchantService
 * 8. Add exception handling logic
 * 9. Add delete method to soft delete Merchant, put for full update, patch for partial update
 * 10. getAllOrders -- replace with the 1 sql query call
 */
@RestController
class MerchantController(
    val merchantRepository: MerchantRepository,
    val orderRepository: OrderRepository
) {
    @PostMapping("/api/v0/merchant")
    fun save(@RequestBody merchant: Merchant): ResponseEntity<Merchant> {
        val savedId = merchantRepository.save(
            MerchantEntity(
                id = null,
                orders = null,
                name = merchant.name,
                email = merchant.email,
                cif = merchant.cif
            )
        ).id
        merchant.id = savedId
        return ResponseEntity.created(URI.create("/api/v0/merchant/$savedId")).body(merchant)
    }

    @GetMapping("/api/v0/merchant/{merchantId}")
    fun get(@PathVariable merchantId: Int) = merchantRepository.findById(merchantId)
        .map {
            Merchant(
                id = it.id,
                cif = it.cif,
                email = it.email,
                name = it.name,
            )
        }
        .map { ResponseEntity.created(URI.create("/api/v0/merchant/$merchantId")).body(it) }
        .orElseThrow { RuntimeException("No merchant is found for id: $merchantId") }

    @GetMapping("/api/v0/merchant")
    fun getAll() = merchantRepository.findAll()
        .map {
            Merchant(
                id = it.id,
                cif = it.cif,
                email = it.email,
                name = it.name,
            )
        }
        .map { ResponseEntity.created(URI.create("/api/v0/merchant")).body(it) }

    @GetMapping("/api/v0/merchant/{merchantId}/order")
    fun getAllOrders(@PathVariable merchantId: Int) = orderRepository
        .findAll()
        .filter { it.merchant.id == merchantId }
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