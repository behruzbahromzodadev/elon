package tj.behruz.elon.ui.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import tj.behruz.elon.R
import tj.behruz.elon.databinding.SignInFragemtnBinding
import tj.behruz.elon.helpers.PreferenceHelper
import tj.behruz.elon.helpers.Utils
import java.lang.Exception

class SignInFragment() : Fragment() {

    private val viewModel: SignInViewModel by viewModels()
    private var _binding: SignInFragemtnBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SignInFragemtnBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        try {

            val phoneNumber = arguments?.getString("phoneNumber")
            val code = arguments?.getString("code")

            binding.registration.setOnClickListener {
                val myhash = phoneNumber?.let { it1 -> Utils.getNumber(it1) }
                if (binding.firstName.text.toString()
                        .isNotEmpty() && binding.surname.text.toString().isNotEmpty()
                ) {

                    val number = myhash.plus("avitootiva").plus(
                        binding.firstName.text.toString().plus(binding.firstName.text.toString())
                    )
                    val hash = Utils.hashing(number)

                    if (myhash != null) {

                        viewModel.registration(
                            myhash,
                            binding.firstName.text.toString(),
                            binding.surname.text.toString(),
                            hash!!
                        ).observe(viewLifecycleOwner, androidx.lifecycle.Observer {

                            if (it.code == 6) {
                                val bundle = Bundle()
                                bundle.putString("code", code)
                                bundle.putString("phoneNumber", phoneNumber )
                                bundle.putString("token",it.data.token)
                                findNavController().navigate(
                                    R.id.action_signInFragment_to_authenticationFragment,
                                    bundle
                                )

                            } else {

                                Utils.showErrorMessage(binding.surname, it.message)

                            }


                        })
                    }

                } else {
                    Utils.showErrorMessage(binding.view10, "Запольните всех полей!")
                }

            }

            viewModel.errorMessage.observe(viewLifecycleOwner, Observer { error_message ->
                Utils.showErrorMessage(binding.surname, error_message)
            })


        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {

        }


    }


}