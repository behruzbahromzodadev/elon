package tj.behruz.elon.models

import android.os.Parcel
import android.os.Parcelable
import kotlin.collections.ArrayList


data class CategoryModel(var code: Int, var message: String, var data: ArrayList<Category>)
data class Category(var id: Int, var name: String)
data class AddAdsModel(var code: Int, var message: String, var data: Boolean)
data class Ads(var text: String?, var tv: String?, var phone: String?, var day: String?, var bonus: String?, var price: String?,var tvId:String,var category:String?): Parcelable {
    constructor(parcel: Parcel): this(parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString().toString(),parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(text)
        parcel.writeString(tv)
        parcel.writeString(phone)
        parcel.writeString(day)
        parcel.writeString(bonus)
        parcel.writeString(price)
        parcel.writeString(tvId)
        parcel.writeString(category)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR: Parcelable.Creator<Ads> {
        override fun createFromParcel(parcel: Parcel): Ads {
            return Ads(parcel)
        }

        override fun newArray(size: Int): Array<Ads?> {
            return arrayOfNulls(size)
        }
    }
}
data class HistoryModel(var code: Int,var message: String,var data:ArrayList<History>)