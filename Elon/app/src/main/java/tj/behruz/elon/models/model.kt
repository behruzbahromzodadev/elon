package tj.behruz.elon.models



data class respond(var is_exist:Boolean,var code:Int)
data class Login(val cod:Int, val message: String, val data: respond)