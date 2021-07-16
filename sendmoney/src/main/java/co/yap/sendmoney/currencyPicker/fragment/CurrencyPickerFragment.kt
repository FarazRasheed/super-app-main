package co.yap.sendmoney.currencyPicker.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.yap.sendmoney.BR
import co.yap.sendmoney.R
import co.yap.sendmoney.currencyPicker.interfaces.ICurrencyPicker
import co.yap.sendmoney.currencyPicker.model.MultiCurrencyWallet
import co.yap.sendmoney.currencyPicker.viewmodel.CurrencyPickerViewModel
import co.yap.yapcore.BaseBindingFragment
import co.yap.yapcore.constants.Constants
import co.yap.yapcore.interfaces.OnItemClickListener
import kotlinx.android.synthetic.main.fragment_currency_picker.*

class CurrencyPickerFragment : BaseBindingFragment<ICurrencyPicker.ViewModel>(),
    ICurrencyPicker.View {

    companion object {
        val IS_DIALOG_POP_UP = "IS_DIALOG_POP_UP"
        val LIST_OF_CURRENCIES = "LIST_OF_CURRENCIES"
    }

    override fun getBindingVariable(): Int = BR.viewModel
    override fun getLayoutId(): Int = R.layout.fragment_currency_picker

    override val viewModel: CurrencyPickerViewModel
        get() = ViewModelProviders.of(this).get(CurrencyPickerViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            bundle.getBoolean(IS_DIALOG_POP_UP).let {
                viewModel.state.currencyDialogChecker.set(it)

            }
            bundle.getParcelableArrayList<MultiCurrencyWallet>(LIST_OF_CURRENCIES)?.let {
                viewModel.availableCurrenciesList = it

            }
        }
        setObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setSearchView()
        setSearchViewDialog()
    }

    private fun setListeners() {
        viewModel.currencyAdapter.setItemListener(currencySelectedItemClickListener)
        viewModel.currencyAdapter.allowFullItemClickListener = true

    }


    private val currencySelectedItemClickListener = object : OnItemClickListener {
        override fun onItemClick(view: View, data: Any, pos: Int) {
            setResultData(data as MultiCurrencyWallet)
        }
    }

    override fun setObservers() {
        viewModel.clickEvent.observe(this, clickObserver)
        viewModel.searchQuery.observe(this, Observer {
            viewModel.currencyAdapter.filter.filter(it)
        })
    }


    private val clickObserver = Observer<Int> {
        when (it) {
            R.id.ivBtnBack -> {
                activity?.finish()
            }
        }
    }

    override fun removeObservers() {
        viewModel.clickEvent.removeObservers(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        removeObservers()
    }

    private fun setSearchView() {
        svSelectCurrency.isIconified = false
        svSelectCurrency.setIconifiedByDefault(false)
        svSelectCurrency.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.searchQuery.value = query
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.searchQuery.value = newText
                return true
            }
        })
    }

    private fun setSearchViewDialog() {
        svBeneficiary.isIconified = false
        svBeneficiary.clearFocus()
        svBeneficiary.setIconifiedByDefault(false)
        svBeneficiary.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.searchQuery.value = query
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.searchQuery.value = newText
                return true
            }
        })
    }

    fun setResultData(multiCurrencyWallet: MultiCurrencyWallet) {
        val intent = Intent()
        intent.putExtra(Constants.CURRENCYWALLET, multiCurrencyWallet)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }
}