package co.yap.modules.location.fragments

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import co.yap.countryutils.country.Country
import co.yap.modules.location.CustomAutoCompleteAdapter
import co.yap.modules.location.interfaces.IPOBSelection
import co.yap.modules.location.viewmodels.POBSelectionViewModel
import co.yap.yapcore.BR
import co.yap.yapcore.R
import co.yap.yapcore.databinding.FragmentPlaceOfBirthSelectionBinding
import co.yap.yapcore.enums.AccountStatus
import co.yap.yapcore.helpers.extentions.afterTextChanged
import co.yap.yapcore.managers.SessionManager

class POBSelectionFragment : LocationChildFragment<IPOBSelection.ViewModel>(), IPOBSelection.View {
    override fun getBindingVariable(): Int = BR.viewModel
    override fun getLayoutId(): Int = R.layout.fragment_place_of_birth_selection
    override val viewModel: POBSelectionViewModel
        get() = ViewModelProviders.of(this).get(POBSelectionViewModel::class.java)

    private var mCustomAutoTextAdapter: CustomAutoCompleteAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().window
            ?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        when (SessionManager.user?.notificationStatuses) {
            AccountStatus.BIRTH_INFO_COLLECTED.name -> {
                skipPOBSelectionFragment()
            }
            else -> {
                addObservers()
            }
        }
    }

    override fun addObservers() {
        viewModel.clickEvent.observe(this, clickObserver)
        viewModel.populateSpinnerData.observe(this, countriesListObserver)
    }

    private val clickObserver = Observer<Int> {
        when (it) {
            R.id.nextButton -> {
                viewModel.saveDOBInfo {
                    findNavController().navigate(R.id.action_POBSelectionFragment_to_taxInfoFragment)
                }
            }
            R.id.ivBackBtn -> {
                activity?.onBackPressed()
            }
        }
    }

    private val countriesListObserver = Observer<List<Country>> {
        setAutoCompleteText(it as ArrayList<Country>)
        if (viewModel.state.selectedCountryIndex >= 0) {
            getBinding().bcountries.setSelection(
                viewModel.state.selectedCountryIndex
            )

        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setAutoCompleteText(it: ArrayList<Country>) {
        mCustomAutoTextAdapter =
            CustomAutoCompleteAdapter(requireContext(), it)
        getBinding().bcountries.setAdapter(mCustomAutoTextAdapter)
        getBinding().bcountries.threshold = 0
        getBinding().bcountries.setOnTouchListener(touchListener)
        getBinding().bcountries.afterTextChanged { string ->
            if (string.isEmpty()) {
                getBinding().bcountries.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    ContextCompat.getDrawable(requireContext(), R.drawable.iv_drown_down),
                    null
                )

                getBinding().bcountries.showDropDown()
            }
            /*  if (mCustomAutoTextAdapter?.getFilteredCount()?:0 <=3){
                  getBinding().bcountries.dropDownHeight = ConstraintLayout.LayoutParams.WRAP_CONTENT
              }
              else{
                  getBinding().bcountries.dropDownHeight = resources.getDimensionPixelSize(R.dimen._80sdp)
              }*/
        }
        getBinding().bcountries.onItemClickListener = itemClickListener


//        setTextSelection(getDefaultCuntry())
    }

    private fun getDefaultCuntry(): Country {
        val countryTest: Country? =
            viewModel.populateSpinnerData.value?.find { it.isoCountryCode2Digit == "AE" }
        return countryTest as Country
    }

    @SuppressLint("ClickableViewAccessibility")
    private val touchListener = View.OnTouchListener { _, _ ->
        getBinding().bcountries.showDropDown()
        false
    }

    private val itemClickListener =
        AdapterView.OnItemClickListener { adapter, _, position, _ ->
            viewModel.state.selectedCountryIndex = position
            val country: Country = adapter?.getItemAtPosition(position) as Country
            setTextSelection(country)
        }

    private fun setTextSelection(country: Country) {
        viewModel.state.selectedCountry = country
        getBinding().bcountries.setText(country.getName())
        getBinding().bcountries.setSelection(country.getName().length)
        val drawable: Drawable? =
            requireActivity().getDrawable(country.getFlagDrawableResId(requireContext()))
        drawable?.setBounds(0, 0, 60, 60)
        getBinding().bcountries.setCompoundDrawables(
            drawable,
            null,
            null,
            null
        )
    }

    override fun removeObservers() {
        viewModel.clickEvent.removeObserver(clickObserver)
        viewModel.populateSpinnerData.removeObserver(countriesListObserver)
    }

    override fun onDestroy() {
        super.onDestroy()
        removeObservers()
    }

    override fun getBinding(): FragmentPlaceOfBirthSelectionBinding {
        return (viewDataBinding as FragmentPlaceOfBirthSelectionBinding)
    }

    private fun skipPOBSelectionFragment() {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.POBSelectionFragment, true) // starting destination skipped
            .build()

        findNavController().navigate(
            R.id.action_POBSelectionFragment_to_taxInfoFragment,
            null,
            navOptions
        )
    }

    override fun onBackPressed(): Boolean {
        return false
    }
}
