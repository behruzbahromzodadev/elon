package tj.behruz.elon.ui.authentification

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import tj.behruz.elon.R
import tj.behruz.elon.databinding.FragmentAuthentificationBinding
import tj.behruz.elon.helpers.PreferenceHelper
import tj.behruz.elon.helpers.Utils
import tj.behruz.elon.models.PinCode

class AuthenticationFragment : Fragment() {

    private var code = PinCode()
    private var _binding: FragmentAuthentificationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAuthentificationBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        try {

            (activity as AppCompatActivity).supportActionBar?.hide()
            val token = arguments?.getString("token")
            val smsCode = arguments?.getString("code")
            val phoneNumber = arguments?.getString("phoneNumber")

            binding.authenticationInfo.text =
                "Код аутентификации был отправлен на номер".plus(" ").plus("+992 ")
                    .plus(phoneNumber)
            binding.firstCode.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0.toString().isNotEmpty()) {
                        binding.secondCode.requestFocus()
                        code.first = p0.toString()
                    } else {
                        code.first = ""
                    }
                }

                override fun afterTextChanged(p0: Editable?) {}

            })

            binding.secondCode.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    if (p0.toString().isNotEmpty()) {
                        binding.thirdCode.requestFocus()
                        code.second = p0.toString()
                    } else {
                        code.second = ""
                    }
                }

                override fun afterTextChanged(p0: Editable?) {}


            })

            binding.thirdCode.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    if (p0.toString().isNotEmpty()) {
                        binding.fourthCode.requestFocus()
                        code.third = p0.toString()
                    } else {
                        code.third = ""
                    }

                }

                override fun afterTextChanged(p0: Editable?) {}


            })


            binding.fourthCode.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    if (p0.toString().isNotEmpty()) {

                        code.four = p0.toString()
                        binding.fives.requestFocus()
                    } else {
                        code.four = ""
                    }

                }

                override fun afterTextChanged(p0: Editable?) {}


            })

            binding.fives.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0.toString().isNotEmpty()) {

                        code.five = p0.toString()
                    } else {
                        code.five = ""
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                    val imm: InputMethodManager = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view!!.windowToken, 0)
                }


            })

            binding.checkCode.setOnClickListener {


                if (code.toString() == smsCode) {
                    token?.let { it1 -> PreferenceHelper.saveToken(requireContext(), it1) }

                    findNavController().navigate(R.id.action_authenticationFragment_to_homeFragment)
                } else {
                    Utils.showErrorMessage(binding.view5, "Неправильный код")
                }


            }

        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}