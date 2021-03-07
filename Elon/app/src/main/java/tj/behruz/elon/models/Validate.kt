package tj.behruz.elon.models

data class Validate(
    var isText: Boolean = false,
    var firstNumber: Boolean = true,
    var category: Boolean = false,
    var tv: Boolean = false,
    var day: Boolean = false
)