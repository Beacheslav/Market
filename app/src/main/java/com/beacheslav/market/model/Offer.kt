package com.beacheslav.market.model

import java.util.*

class Offer(val id: UUID,
            val name: String,
            val desc: String,
            val groupName: String,
            val type: String,
            val image: String,
            val price: Int,
            val discount: Double
            ) : RowType {
}