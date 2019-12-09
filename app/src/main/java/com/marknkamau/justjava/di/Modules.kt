package com.marknkamau.justjava.di

import androidx.room.Room
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.marknjunge.core.data.local.PreferencesRepository
import com.marknkamau.justjava.BuildConfig
import com.marknkamau.justjava.data.db.AppDatabase
import com.marknkamau.justjava.data.db.DbRepository
import com.marknkamau.justjava.data.db.DbRepositoryImpl
import com.marknkamau.justjava.data.preferences.PreferencesRepositoryImpl
import com.marknkamau.justjava.ui.addressBook.AddressBookViewModel
import com.marknkamau.justjava.ui.cart.CartViewModel
import com.marknkamau.justjava.ui.completeSignUp.CompleteSignUpViewModel
import com.marknkamau.justjava.ui.login.SignInViewModel
import com.marknkamau.justjava.ui.main.MainViewModel
import com.marknkamau.justjava.ui.previousOrders.PreviousOrdersPresenter
import com.marknkamau.justjava.ui.previousOrders.PreviousOrdersView
import com.marknkamau.justjava.ui.productDetails.ProductDetailsViewModel
import com.marknkamau.justjava.ui.profile.ProfileViewModel
import com.marknkamau.justjava.ui.signup.SignUpViewModel
import com.marknkamau.justjava.ui.viewOrder.ViewOrderPresenter
import com.marknkamau.justjava.ui.viewOrder.ViewOrderView
import com.marknkamau.justjava.utils.NotificationHelper
import com.marknkamau.justjava.utils.NotificationHelperImpl
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<PreferencesRepository> { PreferencesRepositoryImpl(androidContext()) }

    single<NotificationHelper> { NotificationHelperImpl(androidContext()) }

    single<GoogleSignInClient> {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(BuildConfig.googleClientId)
            .build()
        GoogleSignIn.getClient(androidContext(), gso)
    }
}

val dbModule = module {
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "justjava-db")
            .fallbackToDestructiveMigrationFrom(1)
            .build()
    }
    single { get<AppDatabase>().cartDao() }
    single<DbRepository> { DbRepositoryImpl(get()) }
}

val presentersModule = module {
    factory { (view: ViewOrderView) -> ViewOrderPresenter(view, get(), get(), Dispatchers.Main) }
    factory { (view: PreviousOrdersView) -> PreviousOrdersPresenter(view, get(), Dispatchers.Main) }
}

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { ProductDetailsViewModel(get()) }
    viewModel { CartViewModel(get()) }
    viewModel { SignInViewModel(get()) }
    viewModel { CompleteSignUpViewModel(get()) }
    viewModel { SignUpViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { AddressBookViewModel(get()) }
}