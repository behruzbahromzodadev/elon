package tj.behruz.elon.ui.history

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
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.no_date_layout.view.*
import tj.behruz.elon.adapters.history.HistoryOrdersAdapter
import tj.behruz.elon.databinding.HistoryFragmentBinding
import tj.behruz.elon.helpers.PreferenceHelper
import tj.behruz.elon.helpers.Utils
import tj.behruz.elon.models.History
import tj.behruz.elon.ui.home.HomeViewModel
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class HistoryFragment() : Fragment() {

    private var _binding: HistoryFragmentBinding? = null
    private val binding get() = _binding!!
    private val token: String by lazy {
        PreferenceHelper.getToken(requireContext()) as String
    }
    private var list = ArrayList<History>()
    lateinit var adapter: HistoryOrdersAdapter
    private val viewModel: HomeViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HistoryFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        try {


            (activity as AppCompatActivity).supportActionBar?.show()
            binding.shimmerViewContainer.startShimmerAnimation()

            binding.searchView.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    val body = p0.toString()

                    if (body.isNotEmpty()) {
                        val newList = list.filter {
                            it.text!!.toLowerCase(Locale.ROOT)
                                .contains(body.toLowerCase(Locale.ROOT))
                        } as ArrayList<History>

                        adapter.dataChanged(newList)


                    } else {
                        adapter.dataChanged(list)
                    }
                }

                override fun afterTextChanged(p0: Editable?) {}


            })

            setHistory()

            //handing errorMessage
            viewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMessage ->
                Utils.showErrorMessage(binding.historyRecyclerView, errorMessage)

            })

        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    private fun setHistory() {

        try {

            viewModel.getAllHistory("home", "getAllRunningLine", token)
                .observe(viewLifecycleOwner, Observer { response ->

                    if (response.code == 200) {
                        var history = response.payload!!

                        if (history.code == 6) {
                            binding.shimmerViewContainer.stopShimmerAnimation()
                            binding.shimmerViewContainer.visibility = View.GONE
                            if (history.data.isNotEmpty()) {
                                binding.historyRecyclerView.visibility = View.VISIBLE
                                list = history.data
                                if (list.isNotEmpty()) {
                                    adapter = HistoryOrdersAdapter(
                                        requireContext(),
                                        list.reversed() as ArrayList<History>
                                    )
                                    binding.historyRecyclerView.layoutManager =
                                        LinearLayoutManager(requireContext())
                                    binding.historyRecyclerView.adapter = adapter

                                } else {
                                    binding.noContentLayout.visibility = View.VISIBLE
                                    binding.noContentLayout.noDateTitle.text =
                                        "У вас нет объявлений"
                                }

                            } else {

                            }

                        } else {
                            Utils.showAlert(requireContext(), history.message, "")
                        }


                    } else {
                        Utils.showErrorMessage(binding.historyRecyclerView, "неизвестная ошибка")
                    }
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