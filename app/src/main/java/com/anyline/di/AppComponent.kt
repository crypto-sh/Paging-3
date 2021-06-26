package com.anyline.di

import android.app.Application
import com.anyline.ui.MainActivity
import dagger.BindsInstance
import dagger.Component

@Component(
    modules = arrayOf(
        NetworkModule::class,
        AppModule::class
    )
)
interface AppComponent {

    fun inject(target : MainActivity)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}