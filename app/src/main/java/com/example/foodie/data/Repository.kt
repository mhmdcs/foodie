package com.example.foodie.data

import com.example.foodie.data.RemoteDataSource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityRetainedScoped // This means: when we supply this class as a dependency to another class (i.e. ViewModels), we want this Hilt to only create one instance of it throughout the whole lifecycle of that class (the ViewModel), this is so that when a configuration change happens and Repository needs to be supplied again to the ViewModel, a whole brand new instance doesn't get created, and instead it reuses the one that was already created.
// Because RemoteDataSource is a type we have defined ourselves, and not an interface type or an external-library type, we can inject it directly like so, without needing to define a binding that specifies its creation in a Module for Hilt.
class Repository @Inject constructor(
    remoteDataSource: RemoteDataSource,
    localDataSource: LocalDataSource
) {
    val remote = remoteDataSource
    val local = localDataSource
}