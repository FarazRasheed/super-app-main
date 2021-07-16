package co.yap.widgets.country_spinner

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.AdapterView
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.core.content.ContextCompat
import co.yap.countryutils.country.Country
import co.yap.modules.location.CustomAutoCompleteAdapter
import co.yap.yapcore.R
import co.yap.yapcore.helpers.extentions.hideKeyboard
import co.yap.yapcore.interfaces.OnItemClickListener

class CountryAutoCompleteTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatAutoCompleteTextView(context, attrs, defStyleAttr) {
    private var countryAdapter: CustomAutoCompleteAdapter? = null
    private var itemSelectedListener: OnItemClickListener? = null

    @SuppressLint("ClickableViewAccessibility")
    fun setUpCountryAutoCompleteTextView(
        it: List<Country>?,
        selectedListener: OnItemClickListener? = null
    ) {
        threshold = 0
        val listOfCountry: ArrayList<Country> = ArrayList(it?.size ?: 0)
        listOfCountry.addAll(it ?: ArrayList())
        setAdapter(getCountryAdapter(listOfCountry))
        setOnTouchListener { v, event ->
            if (!isPopupShowing)
                showDropDown()
            false
        }
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        itemSelectedListener = selectedListener
        setOnItemSelectedListener()
    }

    private val textWatcher: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(editable: Editable) {
            if (editable.toString().isEmpty()) {
                val drawable: Drawable? =
                    ContextCompat.getDrawable(context, R.drawable.ic_chevron_down)
                // drawable?.setBounds(0, 0, 60, 60)
                setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    drawable,
                    null
                )
                showDropDown()
            }

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }

    private fun setOnItemSelectedListener() {
        onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val country: Country = parent?.getItemAtPosition(position) as Country
                setTextSelection(country)
                if (itemSelectedListener != null && view != null) {
                    itemSelectedListener?.onItemClick(
                        view,
                        country,
                        position
                    )
                }
            }
    }

    fun setTextSelection(country: Country?, position: Int = 0) {
        if (position == 0) {
            if (itemSelectedListener != null) {
                itemSelectedListener?.onItemClick(
                    this,
                    country ?: Country(),
                    position
                )
            }
        }
        setText(country?.getName())
        setSelection(country?.getName()?.length ?: 0)
        val drawableId = country?.getFlagDrawableResId(context) ?: -1
        if (drawableId > 0) {
            val drawable: Drawable? = ContextCompat.getDrawable(context, drawableId)
            drawable?.setBounds(0, 0, 60, 60)
            setCompoundDrawables(
                drawable,
                null,
                null,
                null
            )
        }
    }

    private fun getCountryAdapter(countries: ArrayList<Country>): CustomAutoCompleteAdapter? {
        if (countryAdapter == null) countryAdapter = CustomAutoCompleteAdapter(context, countries)
        return countryAdapter
    }

    fun reset() {
        removeTextChangedListener(textWatcher)
        hideKeyboard()
        setText("")
        setCompoundDrawablesWithIntrinsicBounds(
            null,
            null,
            ContextCompat.getDrawable(context, R.drawable.ic_chevron_down),
            null
        )
        addTextChangedListener(textWatcher)
    }
}
