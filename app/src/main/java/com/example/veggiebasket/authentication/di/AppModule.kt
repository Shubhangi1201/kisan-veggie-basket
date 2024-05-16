package com.example.veggiebasket.authentication.di

import android.content.Context
import com.example.veggiebasket.admin.fragments.admin_myproduct_screen.domain.AdminMyproductRepository
import com.example.veggiebasket.admin.fragments.admin_myproduct_screen.domain.AdminMyproductRepositoryImpl
import com.example.veggiebasket.authentication.data.SharedPreferencesManager
import com.example.veggiebasket.authentication.domain.repository.AuthRepository
import com.example.veggiebasket.authentication.domain.repository.AuthRepositoryImpl
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.domain.repository.CategoryRepository
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.domain.repository.CategoryRepositoryImpl
import com.example.veggiebasket.user.shopping.presentation.fragment.profilescreen.domain.ProfileRepository
import com.example.veggiebasket.user.shopping.presentation.fragment.profilescreen.domain.ProfileRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferencesManager {
        return SharedPreferencesManager(context)
    }

    @Provides
    @Singleton
    fun provideAdminMyOrder(impl: AdminMyproductRepositoryImpl): AdminMyproductRepository= impl

    @Provides
    @Singleton
    fun provideAuthRepository(impl: AuthRepositoryImpl): AuthRepository = impl

    @Provides
    @Singleton
    fun provideCategoryRepository(impl: CategoryRepositoryImpl): CategoryRepository = impl

    @Provides
    @Singleton
    fun provideProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository = impl

    @Provides
    @Singleton
    fun provideFirebaseDatabase(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }

    @Provides
    @Singleton
    fun provideDatabaseReference(database: FirebaseDatabase): DatabaseReference {
        return database.reference
    }

    @Provides
    @Singleton
    fun provideFirestore() = FirebaseFirestore.getInstance()

}