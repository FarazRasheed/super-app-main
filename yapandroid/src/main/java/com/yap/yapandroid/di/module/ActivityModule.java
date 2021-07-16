package com.yap.yapandroid.di.module;

import android.app.Activity;
import android.content.Context;

import com.yap.yapandroid.di.CustomScope;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private Activity mActivity;

    public ActivityModule(Activity activity) {
        mActivity = activity;
    }

    @Provides
    Activity provideActivity() {
        return mActivity;
    }

    @Provides
    @CustomScope
    Context providesContext() {
        return mActivity;
    }
}