package com.sequra.sequraassignment.repositories

import com.sequra.sequraassignment.MerchantEntity
import com.sequra.sequraassignment.ShopperEntity
import org.springframework.data.jpa.repository.JpaRepository

interface MerchantRepository : JpaRepository<MerchantEntity, Int>
interface ShopperRepository : JpaRepository<ShopperEntity, Int>
