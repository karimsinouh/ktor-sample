package com.example.features.errorsLog.data

import com.example.features.errorsLog.model.ErrorLogModel
import com.google.firebase.cloud.FirestoreClient


class ErrorsLogRepository {

    private val db = FirestoreClient.getFirestore()
    private val errorsCollection = db.collection("errors")


    fun log(log: ErrorLogModel): Result<Unit>{
        return try {

            errorsCollection.add(log).get().get()
            Result.success(Unit)

        }catch (e: Exception){
            Result.failure(e)
        }
    }


    fun getLogs(): Result<List<ErrorLogModel>>{
        return try {

            val q=errorsCollection
                .limit(15)
                .orderBy("timestamp", com.google.cloud.firestore.Query.Direction.DESCENDING)
                .get()
                .get()

            val list=q.toObjects(ErrorLogModel::class.java)
            return Result.success(list)

        }catch (e: Exception){
            Result.failure(e)
        }
    }


}