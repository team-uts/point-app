package dev.teamuts.point.persistence.common.exception

class DatabaseException : RuntimeException {
    constructor(message: String?, cause: Throwable? = null) : super(message, cause)
}
