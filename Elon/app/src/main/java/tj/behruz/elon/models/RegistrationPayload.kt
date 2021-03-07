package tj.behruz.elon.models

data class RegistrationPayload(
    var user_id:Int,
    var user_group:Int,
    var token:String,
    var phone:String,
    var full_name:String,
    var description:String,
    var wallet:String,
    var img_src:String
)