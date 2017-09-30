package com.atc.planner.commons

import android.content.Context
import javax.inject.Inject

interface StringProvider {
    fun getString(id: Int): String
    fun getString(id: Int, vararg formats: Any): String
    fun getQuantityString(id: Int, quantity: Int): String
    fun getQuantityString(id: Int, quantity: Int, vararg formats: Any): String
}

class StringProviderImpl @Inject constructor(val context: Context) : StringProvider {
    private val resources = context.resources
    override fun getString(id: Int): String = resources.getString(id)
    override fun getString(id: Int, vararg formats: Any): String = resources.getString(id, formats)
    override fun getQuantityString(id: Int, quantity: Int): String = resources.getQuantityString(id, quantity)
    override fun getQuantityString(id: Int, quantity: Int, vararg formats: Any): String = resources.getQuantityString(id, quantity, formats)
}
