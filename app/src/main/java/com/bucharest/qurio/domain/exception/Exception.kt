package com.bucharest.qurio.domain.exception

class NetworkErrorException(message: String? = null) : Exception(message)
class NoQuestionsAvailableException : Exception("No questions available")
class InvalidRequestException(message: String? = null) : Exception(message)

class NotEnoughCoinsException(message: String) : Exception(message)
class NoLivesLeftException(message: String) : Exception(message)
class EntityNotFoundException(message: String) : Exception(message)
class ValidationException(message: String) : Exception(message)