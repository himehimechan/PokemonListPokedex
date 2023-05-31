package com.teguh.pokelist.utils

import android.app.Application
import android.content.Context
import com.teguh.pokelist.data.db_helper.DataBaseHandler


class MyContext : Application() {
    override fun onCreate() {
        instance = this
        val db = DataBaseHandler(context!!)
        super.onCreate()
    }

    companion object {
        private var instance: MyContext? = null

        // or return instance.getApplicationContext();
        val context: Context?
            get() = instance
        // or return instance.getApplicationContext();
    }
}