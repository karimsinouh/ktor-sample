package features.messaging.domain

interface SendWhatsappMessageRepo {

    suspend operator fun invoke( phoneNumber:String, message:String,)

}