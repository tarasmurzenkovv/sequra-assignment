package com.sequra.sequraassignment.it.controller

import com.sequra.sequraassignment.DisburseEntity
import com.sequra.sequraassignment.MerchantEntity
import com.sequra.sequraassignment.OrderEntity
import com.sequra.sequraassignment.ShopperEntity
import com.sequra.sequraassignment.initializer.PostgreInitializer
import com.sequra.sequraassignment.repositories.DisburseRepository
import com.sequra.sequraassignment.repositories.MerchantRepository
import com.sequra.sequraassignment.repositories.OrderRepository
import com.sequra.sequraassignment.repositories.ShopperRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.math.BigDecimal
import java.time.LocalDateTime
import kotlin.properties.Delegates

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ContextConfiguration(initializers = [PostgreInitializer::class])
class DisbursementControllerIntegrationTest@Autowired constructor(
    val orderRepository: OrderRepository,
    val merchantRepository: MerchantRepository,
    val shopperRepository: ShopperRepository,
    val disburseRepository: DisburseRepository,
    val mockMvc: MockMvc
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

        val orderEntity2 = orderRepository.save(
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

        disburseRepository.save(
            DisburseEntity(
                id = null,
                amount = BigDecimal(1),
                order = orderEntity2
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
    fun `should find disbursements for merchant and week`() {
        //when
        val response = mockMvc.perform(
            get("/api/v0/disbursement/15?merchantId=$saveMerchantId")
        ).andExpect(
            status().isOk
        ).andReturn()
            .response
            .contentAsString
        Assertions.assertThat(response).isEqualTo("[{\"id\":5,\"amount\":1,\"merchantId\":1}]")
    }
}