package com.example.unittesting.model;

import android.content.res.Resources;
import android.support.annotation.StringRes;

public class ResourceProvider {

    Resources resources;

    public ResourceProvider(Resources resources) {
        this.resources = resources;
    }

    public String getString(@StringRes int stringResId) {
        return resources.getString(stringResId);
    }
}
