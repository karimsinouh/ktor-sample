package com.example.features.config.data

import com.example.features.config.domain.ConfigRepository
import com.example.features.config.model.ConfigModel
import com.google.firebase.cloud.FirestoreClient

class ConfigRepositoryImpl: ConfigRepository {

    private val db = FirestoreClient.getFirestore()
    private val configsCollection = db.collection("configs")
    private val configsDocument=configsCollection.document("configs")

    override suspend fun updateConfigs(configs: ConfigModel){
        val fields=mapOf(
            "trainingPrompt" to configs.trainingPrompt,
            "messagesLimit" to configs.messagesLimit,
            "maintenanceMode" to configs.maintenanceMode
        )
        try {
            configsDocument.set(fields).get()
        }catch (e: Exception){
            throw e
        }
    }

    override suspend fun updateApiKey(newKey: String?) {
        if (newKey==null) throw IllegalArgumentException("key should not be empty")
        val fields=mapOf(
            "geminiKey" to newKey,
        )
        try {
            configsDocument.set(fields).get()
        }catch (e: Exception){
            throw e
        }
    }

    override suspend fun getConfigs(): Result<ConfigModel> {
        val querySnapshot=configsDocument.get().get()
        if (querySnapshot.exists()){
            val configs=querySnapshot.toObject(ConfigModel::class.java)
            return if (configs!=null)
                Result.success(configs)
            else
                Result.failure(Exception("Couldn't get configs. They might have not been set yet"))
        }else
            return Result.failure(Exception("Configs are not set yet"))
    }


}