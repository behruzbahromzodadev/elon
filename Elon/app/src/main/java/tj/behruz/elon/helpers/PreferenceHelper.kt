package tj.behruz.elon.helpers

import android.content.Context

class PreferenceHelper{
    companion object {
        private var access = "Token"
        private var numberPhone = "Number"

        fun saveToken(context: Context, token: String) {
            val sharedPreferences = context.getSharedPreferences(access, Context.MODE_PRIVATE)
            sharedPreferences.edit().putString(access,token).apply()
        }

        fun getToken(context: Context): String? {
            val sharedPreferences = context.getSharedPreferences(access, Context.MODE_PRIVATE)
            return sharedPreferences.getString(access, "")
        }


        fun saveNumberPhone(context: Context, number: String) {
            val sharedPreferences = context.getSharedPreferences(numberPhone, Context.MODE_PRIVATE)
            sharedPreferences.edit().putString(numberPhone, number).apply()
        }

        fun getNumberPhone(context: Context): String? {
            val sharedPreferences = context.getSharedPreferences(numberPhone, Context.MODE_PRIVATE)
            return sharedPreferences.getString(numberPhone, "")
        }

    }
}