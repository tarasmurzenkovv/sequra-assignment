package com.sequra.sequraassignment.repositories

import com.sequra.sequraassignment.DisburseEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.jdbc.core.BatchPreparedStatementSetter
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.sql.PreparedStatement

interface DisburseRepository : JpaRepository<DisburseEntity, Int>, DisburseRepositoryCustom {

    @Query(
        """
        select disburse.id as id, 
                disburse.amount as amount, 
                o.merchant_id as merchantId from disburse
        inner join "order" o on o.id = disburse.order_id
        inner join merchant m on m.id = o.merchant_id
        where o.merchant_id = :merchantId
        and EXTRACT('week' from o.completed_at) = :weekNumber
        and o.completed_at is not null
    """, nativeQuery = true
    )
    fun find(
        @Param("merchantId") merchantId: Int,
        @Param("weekNumber") weekNumber: Int,
    ): List<DisburseProjection>?

    @Query(
        """
        select disburse.id as id, 
                disburse.amount as amount from disburse
        inner join "order" o on o.id = disburse.order_id
        inner join merchant m on m.id = o.merchant_id
        where EXTRACT('week' from o.completed_at) = :weekNumber
        and o.completed_at is not null
    """, nativeQuery = true
    )
    fun find(@Param("weekNumber") weekNumber: Int): List<DisburseProjection>?
}

interface DisburseRepositoryCustom {
    fun saveAll(disbursmentsForOrders: List<Pair<Int, BigDecimal>>)
}

@Service
class DisburseRepositoryCustomImpl(val jdbcTemplate: JdbcTemplate) : DisburseRepositoryCustom {
    override fun saveAll(disbursmentsForOrders: List<Pair<Int, BigDecimal>>) {
        jdbcTemplate.batchUpdate("insert into disburse (order_id, amount)  values (?, ?);",
            object : BatchPreparedStatementSetter {
                override fun getBatchSize() = disbursmentsForOrders.size
                override fun setValues(ps: PreparedStatement, i: Int) {
                    ps.setInt(1, disbursmentsForOrders[i].first)
                    ps.setBigDecimal(2, disbursmentsForOrders[i].second)
                }
            })
    }
}

interface DisburseProjection {
    val id: Int
    val amount: BigDecimal
    val merchantId: Int
}
