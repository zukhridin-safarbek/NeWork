package kg.zukhridin.nework.exceptions

sealed class ApiResult<T>(
    val code: String? = null,
    val data: T? = null,
    val message: String? = null,
) {
    class Success<T>(code: String, data: T) : ApiResult<T>(code = code, data = data)
    class Error<T>(code: String, message: String?, data: T? = null) :
        ApiResult<T>(code = code, message = message, data = data)
}