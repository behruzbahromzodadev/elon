package tj.behruz.elon.models

 class PinCode{
     lateinit var first:String
     lateinit var second:String
     lateinit var third:String
     lateinit var four:String
     lateinit var five:String

     override fun toString(): String {
         return first.plus(second.plus(third.plus(four.plus(five))))
     }
 }