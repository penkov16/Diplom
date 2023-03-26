package ru.netology.nmedia.errors

import okhttp3.ResponseBody
import java.io.IOException
import java.sql.SQLException

sealed class AppError(val code: Int, val info: String) : RuntimeException(info) {
    companion object {
        fun from(e: Throwable): AppError = when (e) {
            is AppError -> e
            is SQLException -> DbError
            is IOException -> NetworkException
            else -> UnknownException
        }
    }
}

class ApiError(code: Int, message: String) : AppError(code, message)
class ApiError2(code: Int, message: String, val responseBody: ResponseBody?) : AppError(code, message)

object NetworkException : AppError(-1, "no_network")
object UnknownException : AppError(-1, "unknown")
object DbError : AppError(-1, "error_db")