package tj.behruz.elon.models

import android.os.Parcel
import android.os.Parcelable


data class History(var id: String?, var text: String?, var phones: String?, var category_id: String?, var add_date: String?, var modified_date: String?, var count_day: String?, var bonus: String?, var price: String?, var replay: String?, var user_id: String?, var tv_id: String?, var status: String?,
                   var name: String?, var category_name: String?, var tv_name: String?,
                   var full_name: String?, var phone: String?, var tv_img: String?): Parcelable {
    constructor(parcel: Parcel): this(parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString(), parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(text)
        parcel.writeString(phones)
        parcel.writeString(category_id)
        parcel.writeString(add_date)
        parcel.writeString(modified_date)
        parcel.writeString(count_day)
        parcel.writeString(bonus)
        parcel.writeString(price)
        parcel.writeString(replay)
        parcel.writeString(user_id)
        parcel.writeString(tv_id)
        parcel.writeString(status)
        parcel.writeString(name)
        parcel.writeString(category_name)
        parcel.writeString(tv_name)
        parcel.writeString(full_name)
        parcel.writeString(phone)
        parcel.writeString(tv_img)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR: Parcelable.Creator<History> {
        override fun createFromParcel(parcel: Parcel): History {
            return History(parcel)
        }

        override fun newArray(size: Int): Array<History?> {
            return arrayOfNulls(size)
        }
    }
}
