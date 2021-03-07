package tj.behruz.elon.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.header_layout.view.*
import kotlinx.android.synthetic.main.no_date_layout.view.*
import tj.behruz.elon.R
import tj.behruz.elon.adapters.history.HistoryOrdersAdapter
import tj.behruz.elon.adapters.home.BannerAdapter
import tj.behruz.elon.databinding.HomeFragmentBinding
import tj.behruz.elon.helpers.PreferenceHelper
import tj.behruz.elon.helpers.Utils
import tj.behruz.elon.models.History
import java.lang.Exception

class HomeFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var toggle: ActionBarDrawerToggle

    private var token = ""
    private val viewModel: HomeViewModel by viewModels()
    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            activity?.finish()
        }

        try {

            init()

            //hiding actionbar
            (activity as AppCompatActivity).supportActionBar?.hide()

            toggle = object : ActionBarDrawerToggle(
                activity,
                binding.drawerLayout,
                binding.contentHome.toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
            ) {}
            binding.drawerLayout.addDrawerListener(toggle)

            toggle.syncState()
            //setting navView
            binding.navView.setNavigationItemSelectedListener(this)

            binding.contentHome.ads.setOnClickListener {
                moveToScreen(R.id.action_homeFragment_to_newAdsFragment)
            }

            binding.contentHome.profile.setOnClickListener {

                moveToScreen(R.id.action_homeFragment_to_profileFragment)

            }

            binding.contentHome.history.setOnClickListener {

                moveToScreen(R.id.action_homeFragment_to_historyFragment)
            }

            binding.contentHome.swipeRefresh.setOnRefreshListener {
                init()
                getAllHistory(token)
                viewModel.getBanners(token)

            }

            viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
                Utils.showErrorMessage(binding.drawerLayout, it)

            })


        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    private fun init() {
        val number =
            Utils.getUnMaskedNumber(PreferenceHelper.getNumberPhone(requireContext()).toString())
        val currentNumber = number.plus("avitootiva").plus(number)
        val headerView = binding.navView.getHeaderView(0)
        binding.contentHome.swipeRefresh.isRefreshing = false
        binding.contentHome.mShimmerViewContainer.startShimmerAnimation()

        viewModel.getUserInfo(number, Utils.hashing(currentNumber).toString())
            .observe(viewLifecycleOwner, Observer { response ->

                if (response.code == 200) {
                    val registrationModel = response.payload!!

                    if (registrationModel.code == 6) {
                        val info = registrationModel.data
                        token = info.token
                        Picasso.get().load(info.img_src).placeholder(R.drawable.ic_user)
                            .error(R.drawable.ic_user).noFade().into(binding.contentHome.avatar);
//                        Glide.with(requireContext())
//                            .load(info.img_src)
//                            .error(R.drawable.ic_user)
//                            .into(binding.contentHome.avatar)
                        PreferenceHelper.saveToken(requireContext(), info.token)
                        binding.contentHome.balance.text = info.wallet.plus(" ").plus("сом")
                        binding.contentHome.username.text = info.full_name
                        binding.contentHome.phone.text = info.phone
                        getAllHistory(registrationModel.data.token)
                        binding.contentHome.mShimmerViewContainer.visibility = View.GONE
                        binding.contentHome.contentLayout.visibility = View.VISIBLE
                        binding.contentHome.mShimmerViewContainer.stopShimmerAnimation()
                        binding.contentHome.bannerRecyclerview.visibility = View.VISIBLE
                        binding.contentHome.historyList.visibility = View.VISIBLE
                        headerView.user_phone.text = info.phone
                        headerView.username.text = info.full_name
                        Picasso.get().load(info.img_src).placeholder(R.drawable.ic_user)
                            .error(R.drawable.ic_user).noFade().into(headerView.user_avatar)


                    } else {
                        binding.contentHome.noContentLayout.visibility = View.VISIBLE
                    }

                } else {
                    Utils.showErrorMessage(binding.contentHome.addTitle,"Неизвестная ошибка")

                }
            })


    }


    private fun getAllHistory(token: String) {

        viewModel.getAllHistory("home", "getAllRunningLine", token)
            .observe(viewLifecycleOwner, Observer { response ->
                if (response.code == 200) {
                    val historyModel = response.payload!!
                    if (historyModel.code == 6) {
                        if (historyModel.data.isNotEmpty() || historyModel.data.size > 0) {
                            val list = historyModel.data.filter { it.status == "1" }
                            if (list.isNotEmpty()) {
                                val adapter =
                                    HistoryOrdersAdapter(
                                        requireContext(),
                                        list as ArrayList<History>
                                    )
                                binding.contentHome.historyList.layoutManager =
                                    LinearLayoutManager(requireContext())
                                binding.contentHome.historyList.adapter = adapter
                            } else {
                                binding.contentHome.noContentLayout.visibility = View.VISIBLE
                                binding.contentHome.noContentLayout.noDateTitle.text =
                                    " У вас нет активных объявление"

                            }

                        }
                    } else {
                        Utils.showSnackBar(binding.drawerLayout, historyModel.message)
                    }

                } else {
                    Utils.showErrorMessage(binding.drawerLayout, "неизвестная ошибка")
                }
            })

        viewModel.getBanners(token).observe(viewLifecycleOwner, Observer { response ->
            if (response.code == 200) {

                val payload = response.payload!!
                if (payload.code == 6) {
                    if (payload.data.isNotEmpty()) {
                        val adapter =
                            BannerAdapter(requireContext(), payload.data)
                        binding.contentHome.bannerRecyclerview.layoutManager =
                            LinearLayoutManager(
                                requireContext(),
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )
                        binding.contentHome.bannerRecyclerview.adapter = adapter
                    }
                }
            } else {
                Utils.showErrorMessage(binding.drawerLayout, "неизвестная ошибка")

            }
        })
        //handing errorMessage
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMessage ->
            if (errorMessage.isNotEmpty()) {
                Utils.showAlert(requireContext(), message = errorMessage)
            }
        })

    }


    override fun onNavigationItemSelected(menu: MenuItem): Boolean {
        when (menu.itemId) {

            R.id.home_menu -> (moveToScreen(R.id.action_global_homeFragment))

            R.id.profile_menu -> (moveToScreen(R.id.action_homeFragment_to_profileFragment))

            R.id.logout_menu -> {
                val dialog = AlertDialog.Builder(requireContext())
                dialog.setIcon(R.drawable.exit_icon)
                dialog.setTitle("Выход")
                dialog.setMessage("Вы дейтвительно хотите выйты ?")
                dialog.setPositiveButton("Да") { _, _ ->
                    PreferenceHelper.saveToken(requireContext(), "")
                    moveToScreen(R.id.action_homeFragment_to_registrationFragment)
                }
                dialog.setNegativeButton("Нет") { _, _ ->

                }
                dialog.show()

            }

            R.id.history_payment_menu -> (moveToScreen(R.id.action_homeFragment_to_historyFragment))

            R.id.history_orders_menu -> (moveToScreen(R.id.action_homeFragment_to_historyFragment))

            R.id.canceled_ads_menu -> (moveToScreen(R.id.action_homeFragment_to_historyOrdersCanceledFragment))

        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)

        return true

    }


    private fun moveToScreen(route: Int) {
        findNavController().navigate(route)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}