package com.sequra.sequraassignment.it.service

import com.sequra.sequraassignment.MerchantEntity
import com.sequra.sequraassignment.OrderEntity
import com.sequra.sequraassignment.ShopperEntity
import com.sequra.sequraassignment.initializer.PostgreInitializer
import com.sequra.sequraassignment.repositories.DisburseRepository
import com.sequra.sequraassignment.repositories.MerchantRepository
import com.sequra.sequraassignment.repositories.OrderRepository
import com.sequra.sequraassignment.repositories.ShopperRepository
import com.sequra.sequraassignment.services.DisbursementService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import java.math.BigDecimal
import java.time.LocalDateTime

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = [PostgreInitializer::class])
class DisbursementServiceIntegrationTest @Autowired constructor(
    val disbursementService: DisbursementService,
    val orderRepository: OrderRepository,
    val merchantRepository: MerchantRepository,
    val shopperRepository: ShopperRepository,
    val disburseRepository: DisburseRepository,
) {
    var saveMerchantId: Int? = null
    @BeforeEach
    fun initData() {
        val merchantEntity = merchantRepository.save(
            MerchantEntity(
                id = null,
                name = "name",
                email = "email@email.com",
                cif = "X112221",
                orders = null
            )
        )
        saveMerchantId = merchantEntity.id

        val shopperEntity = shopperRepository.save(
            ShopperEntity(
                id = null,
                name = "name",
                email = "email@email.com",
                nif = "X112221",
                orders = null
            )
        )

        orderRepository.save(
            OrderEntity(
                id = null,
                amount = BigDecimal(1),
                completedAt = null,
                disburse = null,
                shopper = shopperEntity,
                merchant = merchantEntity,
            )
        )

        orderRepository.save(
            OrderEntity(
                id = null,
                amount = BigDecimal(2),
                completedAt = LocalDateTime.now(),
                createdAt = LocalDateTime.of(2022, 4, 14, 13, 1, 1),
                disburse = null,
                shopper = shopperEntity,
                merchant = merchantEntity,
            )
        )
    }

    @AfterEach
    fun clean() {
        disburseRepository.deleteAll()
        orderRepository.deleteAll()
        shopperRepository.deleteAll()
        merchantRepository.deleteAll()
    }

    @Test
    fun `should properly persist disbusments`() {
        disbursementService.calculateAndStoreDisbursmentComission()
        val disbursments = disburseRepository.findAll()
        Assertions.assertThat(disbursments).hasSize(1)
        val disburseEntity = disbursments[0]
        Assertions.assertThat(disburseEntity.id).isNotNull
        Assertions.assertThat(disburseEntity.amount.compareTo(BigDecimal(0.01))).isEqualTo(-1)
    }
}