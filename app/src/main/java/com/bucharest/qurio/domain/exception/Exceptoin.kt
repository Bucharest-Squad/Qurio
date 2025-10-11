package com.bucharest.qurio.domain.exception

class NetworkErrorException(message: String? = null) : Exception(message)
class NoQuestionsAvailableException : Exception("No questions available")
class InvalidRequestException(message: String? = null) : Exception(message)