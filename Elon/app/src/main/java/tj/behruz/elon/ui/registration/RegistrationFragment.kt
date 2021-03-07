package tj.behruz.elon.ui.registration


import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import tj.behruz.elon.R
import tj.behruz.elon.databinding.FragmentRegistrationBinding
import tj.behruz.elon.helpers.MaskTextWatcher
import tj.behruz.elon.helpers.PreferenceHelper
import tj.behruz.elon.helpers.Utils

class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RegistrationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_registration, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        (activity as AppCompatActivity).supportActionBar?.hide()

        val token = PreferenceHelper.getToken(requireContext())
        if (token?.isNotEmpty()!!) {
            findNavController().navigate(R.id.action_registrationFragment_to_homeFragment)
        }


        val spannable =
            SpannableString("Я согласен(на) с Условиями соглашения и подтверждаю, что мне есть 18 лет.")
        spannable.setSpan(
            ForegroundColorSpan(Color.GREEN),
            17,
            37,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            (activity as AppCompatActivity).finish()
        }
        binding.ruleCheckbox.text = spannable
        binding.number.addTextChangedListener(MaskTextWatcher(binding.number, "## ### ## ##"))

        binding.number.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(number: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (number.toString().length == 12 && binding.ruleCheckbox.isChecked) {
                    binding.status.visibility = View.VISIBLE
                    setBackBtn(true)
                } else {
                    binding.status.visibility = View.GONE
                    setBackBtn(false)
                }
            }


        })

        binding.ruleCheckbox.setOnCheckedChangeListener { _but, b ->
            if (b && binding.number.text.toString().length == 12) {
                setBackBtn(b)
            } else setBackBtn(b)
        }

        binding.accept.setOnClickListener {
            Utils.showProgressDialog(requireContext(), true)
            val phoneNumber = Utils.getUnMaskedNumber(binding.number.text.toString())
            val numberHash = Utils.hashing(phoneNumber.plus(phoneNumber).plus("avitootiva"))
            PreferenceHelper.saveNumberPhone(requireContext(), phoneNumber)

            try {
                if (numberHash != null) {
                    Log.d("hash", numberHash)
                    Log.d("hash", phoneNumber)
                    viewModel.login(phoneNumber, numberHash)
                        .observe(viewLifecycleOwner, Observer { loginModel ->
                            Utils.showProgressDialog(requireContext(), false)
                            Log.d("testting", loginModel.toString())
                            if (loginModel.code == 6) {
                                val bundle = Bundle()
                                bundle.putString("code", loginModel.data.code.toString())
                                bundle.putString("phoneNumber", binding.number.text.toString())
                                bundle.putString("token", "")
                                if (loginModel.data.is_exist) {

                                    findNavController().navigate(
                                        R.id.action_registrationFragment_to_authenticationFragment,
                                        bundle
                                    )
                                } else {
                                    findNavController().navigate(
                                        R.id.action_registrationFragment_to_signInFragment,
                                        bundle
                                    )
                                }

                            }
                            //else {
//                                Utils.showSnackBar(
//                                    binding.chooseCountry,
//                                    loginModel.message.toString()
//                                )
//                            }

                        })
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }


        }

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMessage ->
            Utils.showErrorMessage(binding.chooseCountry, errorMessage)

        })


    }

    fun setBackBtn(status: Boolean) {
        val back = if (status) R.drawable.btn_background else R.drawable.enabled_btn_background
        binding.accept.setBackgroundResource(back)
        binding.accept.isEnabled = status
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}