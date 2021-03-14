package com.zs.test.searchgitrepo.data

import android.content.Context

object ResourceHelper {
    lateinit var context: Context

    fun init(context: Context) {
        this.context = context
    }

    fun getString(id: Int): String {
        return context.getString(id)
    }
}