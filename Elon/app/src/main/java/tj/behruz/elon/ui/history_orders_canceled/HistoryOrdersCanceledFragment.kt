package tj.behruz.elon.ui.history_orders_canceled

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.no_date_layout.view.*
import tj.behruz.elon.adapters.history.HistoryOrdersAdapter
import tj.behruz.elon.databinding.FragmentCanceledBinding
import tj.behruz.elon.helpers.PreferenceHelper
import tj.behruz.elon.helpers.Utils
import tj.behruz.elon.models.History
import tj.behruz.elon.ui.home.HomeViewModel
import java.lang.Exception

class HistoryOrdersCanceledFragment() : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    private var _binding: FragmentCanceledBinding? = null
    private val binding get() = _binding!!
    lateinit var historyOrdersAdapter: HistoryOrdersAdapter

    private var cancelledAdsList = ArrayList<History>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCanceledBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        try {

            (activity as AppCompatActivity).supportActionBar?.show()

            binding.shimmerViewContainer.startShimmerAnimation()


            //handing errorMessage
            viewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMessage ->

                Utils.showErrorMessage(binding.constraintLayout, errorMessage)
            })

            val token = PreferenceHelper.getToken(requireContext())
            if (token!!.isNotEmpty()) {
                viewModel.getAllHistory("home", "getAllRunningLine", token)
                    .observe(viewLifecycleOwner, Observer { response ->
                        if (response.code == 200) {
                            val history = response.payload!!
                            if (history.code == 6) {
                                binding.shimmerViewContainer.stopShimmerAnimation()
                                cancelledAdsList =
                                    history.data.filter { it.status == "4" } as ArrayList<History>
                                binding.shimmerViewContainer.visibility = View.GONE
                                binding.historyRecyclerView.visibility = View.VISIBLE

                                if (cancelledAdsList.isNotEmpty()) {
                                    historyOrdersAdapter =
                                        HistoryOrdersAdapter(requireContext(), cancelledAdsList)
                                    binding.historyRecyclerView.adapter = historyOrdersAdapter
                                } else {
                                    binding.noContentLayout.noDateTitle.text =
                                        " У вас нет отмененные заказы"
                                    binding.noContentLayout.visibility = View.VISIBLE
                                }


                            } else {
                                Utils.showErrorMessage(binding.historyRecyclerView, history.message)
                            }

                        } else {
                            Utils.showErrorMessage(
                                binding.historyRecyclerView,
                                "неизвестная ошибка"
                            )

                        }
                    })
            }

            binding.searchView.addTextChangedListener(object : TextWatcher {

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    val body = p0.toString()

                    if (body.isNotEmpty()) {
                        val newList = cancelledAdsList.filter {
                            it.text!!.toLowerCase().contains(body.toLowerCase())
                        } as ArrayList<History>

                        historyOrdersAdapter.dataChanged(newList)


                    } else {
                        historyOrdersAdapter.dataChanged(cancelledAdsList)
                    }

                }

                override fun afterTextChanged(p0: Editable?) {}
            })


        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}