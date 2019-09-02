package com.sai.diagonalley

import android.app.Application
import com.sai.diagonalley.module.ApiModule
import com.sai.diagonalley.module.ConnectivityModule
import com.sai.diagonalley.module.DbModule
import com.sai.diagonalley.module.SharedPreferencesModule
import com.sai.diagonalley.repository.IItemRepository
import com.sai.diagonalley.repository.ItemRepository
import com.sai.diagonalley.viewmodel.DetailActivityViewModel
import com.sai.diagonalley.viewmodel.DAActivityViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber

/**
 * DiagonAlley Application class
 */
class DiagonAlleyApplication : Application() {

    private var moduleList = module {
        single { ApiModule(this@DiagonAlleyApplication) }
        single { DbModule(this@DiagonAlleyApplication) }
        single { ConnectivityModule(this@DiagonAlleyApplication) }
        single { SharedPreferencesModule(this@DiagonAlleyApplication) }

        single<IItemRepository> {ItemRepository(get(), get())}

        viewModel { DAActivityViewModel(get(), get()) }
        viewModel { DetailActivityViewModel(get()) }
    }

    override fun onCreate() {
        super.onCreate()
        initTimber()

        startKoin {
            androidLogger()
            androidContext(this@DiagonAlleyApplication)
            modules(moduleList)
        }
    }

    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }
}
