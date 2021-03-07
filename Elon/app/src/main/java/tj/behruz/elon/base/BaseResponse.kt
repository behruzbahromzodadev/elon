package tj.behruz.elon.base

data class BaseResponse<T>(var code:Int, var message:String, var payload:T?)