package tj.behruz.elon.helpers

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.error_message_dialog.view.*
import tj.behruz.elon.R
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class Utils {
    companion object {

        private var dialog: AlertDialog.Builder? = null

        private var progressDialog: ProgressDialog? = null

        fun showProgressDialog(context: Context, isShowing: Boolean) {

            if (isShowing) {
                if (progressDialog != null && progressDialog?.isShowing!!) return

                progressDialog = ProgressDialog(context)
                progressDialog?.setCancelable(false)
                progressDialog?.setMessage(context.getString(R.string.loading))
                try {
                    progressDialog?.show()
                } catch (e: Exception) {
                }
            } else {
                progressDialog?.dismiss()
            }
        }


        fun hashing(number: String): String? {
            val MD5 = "MD5"
            try {
                // Create MD5 Hash
                val digest = MessageDigest.getInstance(MD5)
                digest.update(number.toByteArray())
                val messageDigest = digest.digest()

                // Create Hex String
                val hexString = StringBuilder()
                for (aMessageDigest in messageDigest) {
                    var h = Integer.toHexString(0xFF and aMessageDigest.toInt())
                    while (h.length < 2) h = "0$h"
                    hexString.append(h)
                }
                return hexString.toString()
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            }
            return ""
        }

        fun getNumber(maskedNumber: String): String {

            return maskedNumber.replace("(", "").replace(")", "").replace(" ", "")
        }

        fun showSnackBar(layout: View, message: String) {
            Snackbar.make(layout, message, Snackbar.LENGTH_LONG).show()
        }

        fun getCategory(category: String): String {
            var result = ""
            when (category) {
                "1" -> result = "Продаётся"
                "2" -> result = "Услуги"
                "3" -> result = "Сдаётся"
                "4" -> result = "Меняется"
                "5" -> result = "Утери"
                "6" -> result = "Усдуги Мастеров"
                "7" -> result = "Вещи б/y"
                "8" -> result = "Новые вакансии"
                "9" -> result = "Акции"
                "10" -> result = "Новости компании"
                "11" -> result = "Автомобили"
                "12" -> result = "Бытовая техника"
                "13" -> result = "Куплю"
            }
            return result

        }

        fun getStatus(status: String): String {
            var result = ""
            when (status) {
                "0" -> result = "остановленый"
                "1" -> result = "активный"
                "2" -> result = "в ожидании"
                "3" -> result = "законченый"
                "4" -> result = " отменен"
            }

            return result
        }

        fun round(value: Double, places: Int): Double {
            var value = value
            require(places >= 0)
            val factor = Math.pow(10.0, places.toDouble()).toLong()
            value = value * factor
            val tmp = Math.round(value)
            return tmp.toDouble() / factor
        }


        fun getUnMaskedNumber(currentNumber: String): String {
            var number = ""
            val numbers = currentNumber.split(" ")
            for (item in numbers) {
                number = number.plus(item)
            }

            return number

        }

        fun showAlert(
            context: Context,
            title: String = context.getString(R.string.error),
            message: String = context.getString(R.string.description),
            PosBtnText: String = context.getString(R.string._ok)
        ) {
            if (dialog != null) return
            dialog = AlertDialog.Builder(context)
            dialog!!.setTitle(title)
            dialog!!.setMessage(message)
            dialog!!.setPositiveButton(PosBtnText) { dialogLocal, _ ->
                dialogLocal.dismiss()
                dialog = null
            }
            dialog!!.setOnDismissListener {
                dialog = null
            }

            val alertDialog = dialog!!.create()
            try {
                alertDialog.show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


        fun showErrorMessage(view: View,message: String){
            val errorSnackbar=Snackbar.make(view,message,Snackbar.LENGTH_LONG)
            errorSnackbar.setBackgroundTint(Color.RED)
            errorSnackbar.setTextColor(Color.WHITE)
            errorSnackbar.animationMode = Snackbar.ANIMATION_MODE_FADE
            errorSnackbar.setAction("OK"
            ) { errorSnackbar.dismiss() }
            errorSnackbar.show()
        }


    }


}