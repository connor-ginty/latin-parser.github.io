package org.latinkotlinproject.controllers

class ServiceResponseBodies {

    data class ParserServiceResponse<T>(
        var success: Boolean,
        var message: String,
        val data: T? = null
    )
}