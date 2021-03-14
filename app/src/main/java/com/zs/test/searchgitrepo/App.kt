package com.zs.test.searchgitrepo

import android.app.Application
import com.zs.test.searchgitrepo.data.ResourceHelper

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        ResourceHelper.init(applicationContext)
    }
}