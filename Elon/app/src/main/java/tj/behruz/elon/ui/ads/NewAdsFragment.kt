package tj.behruz.elon.ui.ads

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import tj.behruz.elon.R
import tj.behruz.elon.databinding.OrderDetailsFragmentBinding
import tj.behruz.elon.helpers.MaskTextWatcher
import tj.behruz.elon.helpers.PreferenceHelper
import tj.behruz.elon.helpers.Utils
import tj.behruz.elon.models.*

class NewAdsFragment : Fragment() {

    private var _binding: OrderDetailsFragmentBinding? = null
    private val binding get() = _binding!!
    private var categoriesList = ArrayList<Category>()
    private var tvList = ArrayList<TvInfo>()
    private val currentValidate = Validate(true, true, true, true, true)
    private var price = 0.0
    private var adsModel = Ads("", "", "", "", "", "", "", "")

    private val viewModel: AddRequestViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.order_details_fragment, container, false)
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.show()


        try {
            val token = PreferenceHelper.getToken(requireContext())
            binding.firstPhone.addTextChangedListener(
                MaskTextWatcher(
                    binding.firstPhone,
                    "## ### ## ##"
                )
            )
            binding.secondPhone.addTextChangedListener(
                MaskTextWatcher(
                    binding.secondPhone,
                    "## ### ## ##"
                )
            )

            if (token!!.isNotEmpty()) {
                //getting category info
                viewModel.getCategory(token).observe(viewLifecycleOwner, Observer { response ->

                    if (response.code == 200) {
                        val gategories = response.payload!!
                        if (gategories.code == 6) {
                            val list = ArrayList<String>()
                            if (gategories.data.isNotEmpty()) {
                                categoriesList = gategories.data
                                for (i in gategories.data) {
                                    list.add(i.name)
                                }

                                val categoryAdapter =
                                    ArrayAdapter(requireContext(), R.layout.list_item, list)

                                (binding.categoryEditText as? AutoCompleteTextView)?.setAdapter(
                                    categoryAdapter
                                )
                            }


                        }else{
                            Utils.showErrorMessage(binding.bonusLayout,"неизвестная ошибка")
                        }

                    } else {
                        Utils.showErrorMessage(binding.bonusLayout,"неизвестная ошибка")
                    }
                })


                //getting info about tv
                viewModel.getTvInfo(token).observe(viewLifecycleOwner, Observer { response ->
                    if (response.code == 200) {
                        val tvModel = response.payload!!
                        if (tvModel.code == 6) {
                            tvList = tvModel.data as ArrayList<TvInfo>
                            val list = ArrayList<String>()
                            if (tvModel.data.isNotEmpty()) {
                                for (i in tvModel.data) {
                                    list.add(i.name)
                                }

                                val categoryAdapter =
                                    ArrayAdapter(requireContext(), R.layout.list_item, list)

                                (binding.tvTextField as? AutoCompleteTextView)?.setAdapter(
                                    categoryAdapter
                                )
                            }

                        }else{
                            Utils.showErrorMessage(binding.bonusLayout,"неизвестная ошибка")
                        }

                    } else {
                        Utils.showErrorMessage(binding.bonusLayout, "Неизвестная ошибка")
                    }


                })


                //category spinner listner
                binding.categoryEditText.setOnItemClickListener { adapterView, view, i, l ->

                    val item = binding.categoryEditText.editableText.toString()
                    val category = categoriesList.find { it.name == item }
                    if (category != null) {
                        adsModel.category = category.id.toString()

                    }


                }

                //tv spinner listner
                binding.tvTextField.setOnItemClickListener { adapterView, view, i, l ->

                    val item = binding.tvTextField.editableText.toString()
                    val tvInfo = tvList.find { it.name == item }
                    if (tvInfo != null) {
                        adsModel.tv = tvInfo.name
                        adsModel.tvId = tvInfo.id.toString()
                        adsModel.price = tvInfo.options.our_price_per_word
                        binding.PriceTv.setText(tvInfo.options.our_price_per_word.plus(" c"))
                        price = tvInfo.options.our_price_per_word.toDouble()
                    }

                }


            }

            binding.firstPhone.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    getCount()
                }

                override fun afterTextChanged(p0: Editable?) {

                }


            })
            binding.secondPhone.addTextChangedListener(object :TextWatcher{
                override fun afterTextChanged(p0: Editable?) {}

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                   getCount()
                }


            })

            //adsText listner
            binding.adsText.addTextChangedListener(object : TextWatcher {

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (text.toString().isNotEmpty()) {
                        getCount()
                    }

                }

                override fun afterTextChanged(p0: Editable?) {}
            })

            binding.day.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0.toString().isNotEmpty()) {
                        val dat = p0.toString().toInt()
                        var bonus = 0.0
                        if (dat.div(5) >= 1.0) {
                            bonus = dat.div(5).toDouble();
                            adsModel.bonus = bonus.toString()

                            binding.bonus.setText(bonus.toString())


                        } else {
                            binding.bonus.setText("0")
                        }

                    } else {
                        binding.bonus.setText("0")
                    }

                }

                override fun afterTextChanged(p0: Editable?) {}


            })

            binding.confirmButton.setOnClickListener {

                val validate = validate()

                if (currentValidate == validate) {
                    adsModel.day = binding.day.text.toString()
                    adsModel.text = binding.adsText.text.toString()
                    adsModel.bonus = binding.bonus.text.toString()
                    adsModel.phone = binding.firstPhone.text.toString().plus(";").plus(" ")
                        .plus(binding.secondPhone.text)

                    viewModel.addAds(adsModel, token).observe(viewLifecycleOwner, Observer {

                        if (it.code == 6) {
                            Utils.showSnackBar(binding.confirmButton, it.message)
                            findNavController().navigate(R.id.action_global_homeFragment)
                        } else {
                            Utils.showSnackBar(binding.confirmButton, it.message)

                        }

                    })

                }

            }


        } catch (e: Exception) {
            e.printStackTrace()
        }

        //handing errorMessage
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer
        { errorMessage ->
            Utils.showErrorMessage(binding.bonusLayout, errorMessage)
        })

    }

    //validate user inputs
    private fun validate(): Validate {
        val result = Validate()
        if (binding.adsText.text.toString().isEmpty()) {
            binding.textLayout.error = "Запольните текст"
            result.isText = false
        } else {
            result.isText = true
            binding.textLayout.isErrorEnabled = false
        }

        if (binding.categoryEditText.editableText.isEmpty()) {
            binding.categoryLayout.error = "Выбирайте категория"
            result.category = false
        } else {
            binding.categoryLayout.isErrorEnabled = false
            result.category = true
        }

        if (binding.tvTextField.editableText.isEmpty()) {
            binding.tvLayout.error = "Выбирайте тв"
            result.tv = false
        } else {
            binding.tvLayout.isErrorEnabled = false
            result.tv = true
        }


        if (binding.firstPhone.text?.isEmpty()!!) {
            binding.firstPhoneLayout.error = "Введите номер телефона"
            result.firstNumber = false
        } else {
            binding.firstPhoneLayout.isErrorEnabled = false
            result.firstNumber = true
        }

        if (binding.day.text.toString().isEmpty()) {
            binding.dayLayout.error = "Введите срок "
            result.day = false
        } else {
            binding.dayLayout.isErrorEnabled = false
            result.day = true
        }
        return result

    }

    fun getCount(): Int {
        var count = 0;
        val body = binding.adsText.text.toString().trim();
        if (body.isNotEmpty()) {
            val textCount = body.split(" ")
            for (i in textCount) {
                if (i.length >= 3) {
                    count++
                }
            }
        }
        val phone = binding.firstPhone.text.toString();
        if (phone.isNotEmpty()) {
            count++
        }
        val second = binding.secondPhone.text.toString();
        if (second.isNotEmpty()) {
            count++
        }

        binding.wordsCount.setText(count.toString())
        return count
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}