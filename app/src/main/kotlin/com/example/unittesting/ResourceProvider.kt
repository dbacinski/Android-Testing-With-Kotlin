package com.example.unittesting

import android.content.res.Resources
import android.support.annotation.StringRes

class ResourceProvider(private var resources: Resources) {

    fun getString(@StringRes stringResId: Int): String {
        return resources.getString(stringResId)
    }
}
