package com.yap.yapandroid.di.component;

import com.yap.yapandroid.di.CustomScope;
import com.yap.yapandroid.di.module.ActivityModule;
import com.yap.yapandroid.main.MainActivity;

import dagger.Component;


@CustomScope
@Component(modules = {ActivityModule.class})
public interface ActivityComponent {

//    void inject(MainActivity activity);

}