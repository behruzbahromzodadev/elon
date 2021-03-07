package tj.behruz.elon.base

data class LoginResponse(
    val code: Int,
    val `data`: Data,
    val message: String
)