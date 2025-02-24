package com.example.routes.templates.model

import kotlinx.serialization.Serializable
import org.intellij.lang.annotations.Language

@Serializable
data class WhatsappTemplateMessageRequest(
    val messaging_product:String,
    val to:String,
    val type:String,
    val template:WhatsappTemplate,
)

@Serializable
data class WhatsappTemplate(
    val name:String,
    val language:WhatsappTemplateLanguage?=null,
    val components: List<WhatsappTemplateComponents>?=null,
)

@Serializable
data class WhatsappTemplateLanguage(
    val code:String,
)

@Serializable
data class WhatsappTemplateComponents(
    val type:String,
    val parameters:List<TemplateComponentParameter>
)

@Serializable
data class TemplateComponentParameter(
    val type: String,
    val text:String,
)