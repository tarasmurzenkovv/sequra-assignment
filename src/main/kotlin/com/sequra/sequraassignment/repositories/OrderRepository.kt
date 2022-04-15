package com.sequra.sequraassignment.repositories

import com.sequra.sequraassignment.OrderEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.sql.ResultSet
import java.time.LocalDateTime
import java.util.LinkedList

interface OrderRepository : JpaRepository<OrderEntity, Int>, OrderRepositoryCustom {

    @Modifying
    @Query(
        """
            update "order" as o
            set completed_at = :completedAt
            where o.completed_at is null
    """, nativeQuery = true
    )
    fun markOrdersAsProcessed(@Param("completedAt") completedAt: LocalDateTime)
}

interface OrderRepositoryCustom {
    fun lockUnProcessedOrders(): List<Pair<Int, BigDecimal>>
}

@Service
class OrderRepositoryCustomImpl(val jdbcTemplate: JdbcTemplate) : OrderRepositoryCustom {
    private val unprocessedOrders = LinkedList<Pair<Int, BigDecimal>>()
    override fun lockUnProcessedOrders(): List<Pair<Int, BigDecimal>> {
        jdbcTemplate.query(
            """
            select o.id, o.amount from "order" as o
            where o.completed_at is null
            for update
        """.trimIndent()
        ) { rs: ResultSet, _: Int ->
            unprocessedOrders.add(Pair(rs.getInt("id"), rs.getBigDecimal("amount")))
        }
        return unprocessedOrders
    }
}