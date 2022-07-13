package com.example.foodie.utils

import retrofit2.Response

//Sealed classes are, in a sense, similar to Enum classes. Each enum constant exists only as a single instance (they are implicitly static). Whereas a subclass of a sealed class can have multiple instances, each with their own state.
//Sealed class is abstract by default i.e. it cannot be instantiated. Sealed classes can have abstract members.
//A sealed class can have subclasses, but all of them must be declared in the same package as the sealed class itself. We usually define the subclasses inside the sealed class's body for clarity and readability.
sealed class NetworkResult<T>(
   val data: T? = null,
   val message: String? = null
){
    class Success<T>(data: T): NetworkResult<T>(data)
    class Error<T>(message: String?, data: T? = null): NetworkResult<T>(data, message)
    class Loading<T>: NetworkResult<T>()
}