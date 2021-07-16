package co.yap.widgets.mobile


import android.app.Dialog
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import co.yap.yapcore.R
import com.futuremind.recyclerviewfastscroll.SectionTitleProvider
import java.util.*


internal class CountryCodeAdapter(
    context: Context,
    countries: List<CCPCountry>,
    codePicker: CountryCodePicker,
    rlQueryHolder: RelativeLayout,
    editText_search: EditText,
    textView_noResult: TextView,
    dialog: Dialog,
    imgClearQuery: ImageView
) : RecyclerView.Adapter<CountryCodeAdapter.CountryCodeViewHolder>(), SectionTitleProvider {


    var filteredCountries: List<CCPCountry>? = null
    var masterCountries: List<CCPCountry>? = null
    var textView_noResult: TextView
    var codePicker: CountryCodePicker
    var inflater: LayoutInflater
    var editText_search: EditText
    var dialog: Dialog
    var context = context
    var rlQueryHolder: RelativeLayout
    var imgClearQuery: ImageView
    var preferredCountriesCount = 0


    override fun getItemCount(): Int {
        return filteredCountries!!.size
    }

    init {
        this.masterCountries = countries
        this.codePicker = codePicker
        this.dialog = dialog
        this.textView_noResult = textView_noResult
        this.editText_search = editText_search
        this.rlQueryHolder = rlQueryHolder
        this.imgClearQuery = imgClearQuery
        this.inflater = LayoutInflater.from(context)
        this.filteredCountries = getFilteredCountries("")
        setSearchBar()
    }

    private fun setSearchBar() {
        if (codePicker.isSearchAllowed) {
            imgClearQuery.visibility = View.GONE
            setTextWatcher()
            setQueryClearListener()
        } else {
            rlQueryHolder.visibility = View.GONE
        }
    }

    private fun setQueryClearListener() {
        imgClearQuery.setOnClickListener { editText_search.setText("") }
    }


    private fun setTextWatcher() {
        this.editText_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                applyQuery(s.toString())
                if (s.toString().trim { it <= ' ' } == "") {
                    imgClearQuery.visibility = View.GONE
                } else {
                    imgClearQuery.visibility = View.VISIBLE
                }
            }
        })
        this.editText_search.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val `in` = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    `in`.hideSoftInputFromWindow(editText_search.getWindowToken(), 0)
                    return true
                }
                return false
            }
        })
    }

    private fun applyQuery(query: String) {
        var query: String = query
        textView_noResult.setVisibility(View.GONE)
        query = query.toLowerCase()
        //if query started from "+" ignore it
        if (query.isNotEmpty() && query[0] == '+') {
            query = query.substring(1)
        }
        filteredCountries = getFilteredCountries(query)
        if (filteredCountries!!.isEmpty()) {
            textView_noResult.visibility = View.VISIBLE
        }
        notifyDataSetChanged()
    }

    private fun getFilteredCountries(query: String): List<CCPCountry> {
        val tempCCPCountryList = ArrayList<CCPCountry>()
        preferredCountriesCount = 0
        if (codePicker.preferredCountries != null && codePicker.preferredCountries!!.isNotEmpty()) {
            for (CCPCountry in codePicker.preferredCountries!!) {
                if (CCPCountry.isEligibleForQuery(query)) {
                    tempCCPCountryList.add(CCPCountry)
                    preferredCountriesCount++
                }
            }
            if (tempCCPCountryList.size > 0) {
                val divider: CCPCountry? = null
                tempCCPCountryList.add(divider!!)
                preferredCountriesCount++
            }
        }
        for (CCPCountry in this.masterCountries!!) {
            if (CCPCountry.isEligibleForQuery(query)) {
                tempCCPCountryList.add(CCPCountry)
            }
        }
        return tempCCPCountryList
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CountryCodeViewHolder {
        val rootView = inflater.inflate(R.layout.layout_recycler_country_tile, viewGroup, false)
        return CountryCodeViewHolder(rootView)
    }

    override fun onBindViewHolder(countryCodeViewHolder: CountryCodeViewHolder, i: Int) {
        countryCodeViewHolder.setCountry(filteredCountries!![i])
        if (filteredCountries!!.size > i) {
            countryCodeViewHolder.mainView.setOnClickListener { view ->
                if (filteredCountries != null && filteredCountries!!.size > i) {
                    codePicker.onUserTappedCountry(filteredCountries!!.get(i))
                }
                if (view != null && filteredCountries != null && filteredCountries!!.size > i) {
                    val imm =
                        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
                    dialog.dismiss()
                }
            }
        } else {
            countryCodeViewHolder.mainView.setOnClickListener(null)
        }
    }

    override fun getSectionTitle(position: Int): String {
        val ccpCountry = filteredCountries!!.get(position)
        return if (preferredCountriesCount > position) {
            "★"
        } else ccpCountry.name.substring(0, 1) ?: "☺" //this should never be the case
    }

    internal inner class CountryCodeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mainView: RelativeLayout = itemView as RelativeLayout
        var textView_name: TextView
        var textView_code: TextView
        var imageViewFlag: ImageView
        var linearFlagHolder: LinearLayout
        var divider: View

        init {
            textView_name = mainView.findViewById(R.id.tvCountryName) as TextView
            textView_code = mainView.findViewById(R.id.tvCode) as TextView
            imageViewFlag = mainView.findViewById(R.id.ivFlag) as ImageView
            linearFlagHolder = mainView.findViewById(R.id.llFlagHolder) as LinearLayout
            divider = mainView.findViewById(R.id.preferenceDivider)
            if (codePicker.dialogTextColor != 0) {
                textView_name.setTextColor(codePicker.dialogTextColor)
                textView_code.setTextColor(codePicker.dialogTextColor)
                divider.setBackgroundColor(codePicker.dialogTextColor)
            }

        }

        fun setCountry(ccpCountry: CCPCountry) {
            if (ccpCountry != null) {
                divider.visibility = View.GONE
                textView_name.visibility = View.VISIBLE
                textView_code.visibility = View.VISIBLE
                if (codePicker.isCcpDialogShowPhoneCode) {
                    textView_code.visibility = View.VISIBLE
                } else {
                    textView_code.visibility = View.GONE
                }
                var countryName = ""
                if (codePicker.ccpDialogShowFlag && codePicker.ccpUseEmoji) {
                    //extra space is just for alignment purpose
                    countryName += CCPCountry.getFlagEmoji(ccpCountry) + " "
                }
                countryName += ccpCountry.name
                if (codePicker.ccpDialogShowNameCode) {
                    countryName += " (" + ccpCountry.nameCode.toUpperCase() + ")"
                }
                textView_name.text = countryName
                textView_code.text = "+" + ccpCountry.phoneCode
                if (!codePicker.ccpDialogShowFlag || codePicker.ccpUseEmoji) {
                    linearFlagHolder.visibility = View.GONE
                } else {
                    linearFlagHolder.visibility = View.VISIBLE
                    imageViewFlag.setImageResource(ccpCountry.flagID)
                }
            } else {
                divider.visibility = View.VISIBLE
                textView_name.visibility = View.GONE
                textView_code.visibility = View.GONE
                linearFlagHolder.visibility = View.GONE
            }
        }
    }
}