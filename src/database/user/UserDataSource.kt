package com.example.database.user

import com.example.features.`object`.domain.Object
import com.example.features.account.domain.User

interface UserDataSource {

    suspend fun insertUser(user: User): Boolean

    suspend fun getUserOrNull(email: String): User?

    suspend fun getUser(email: String): User

    suspend fun updateUser(user: User): Boolean

    suspend fun createNewObject(fileName: String): Object




    //add user MyObject                        (status created)
    //delete user MyObject                     (status created)
    //get Object                               (all 4 status)

    //update Object FileName                   (from created and cart)
    //update Object BasicSetting               (from created and cart)
    //update Object AdvancedSetting            (from created and cart)

    //get Tracking Object                      (status Tracking)
    //update Tracking Object                   (status Tracking)

    //change status created > cart
    //change status cart > created
    //change status cart > tracking
    //change status tracking > Completed

    //get User MyObjects
    //get User Cart Objects
    //get User Tracking Objects
    //get User History Objects
}