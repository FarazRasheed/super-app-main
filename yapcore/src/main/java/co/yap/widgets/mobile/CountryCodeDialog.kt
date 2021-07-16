package co.yap.widgets.mobile

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.yap.yapcore.R
import com.futuremind.recyclerviewfastscroll.FastScroller
import java.lang.reflect.Field


@SuppressLint("StaticFieldLeak")
internal object CountryCodeDialog {
    private val sEditorField: Field? = null
    private val sCursorDrawableField: Field? = null
    private var sCursorDrawableResourceField: Field? = null
    private var dialog: Dialog? = null


    lateinit var mContext: Context

    init {
        var editorField: Field? = null
        var cursorDrawableField: Field? = null
        var cursorDrawableResourceField: Field? = null
        try {
            cursorDrawableResourceField =
                TextView::class.java.getDeclaredField("mCursorDrawableRes")
            cursorDrawableResourceField.isAccessible = true
            editorField = TextView::class.java.getDeclaredField("mEditor")
            editorField.isAccessible = true
            val drawableFieldClass: Class<*> = editorField.type
            cursorDrawableField = drawableFieldClass.getDeclaredField("mCursorDrawable")
            cursorDrawableField.isAccessible = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun openCountryCodeDialog(codePicker: CountryCodePicker, countryNameCode: String?) {
        mContext = codePicker.getContext()
        dialog = Dialog(mContext)
        codePicker.refreshCustomMasterList()
        codePicker.refreshPreferredCountries()
        val masterCountries = CCPCountry.getCustomMasterCountryList(mContext, codePicker)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setContentView(R.layout.layout_picker_dialog)
        //keyboard
//        if (codePicker.isSearchAllowed && codePicker.dialogKeyboardAutoPopup) {
//            dialog!!.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
//        } else {
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
//        }
        //dialog views
        val recyclerView_countryDialog =
            dialog!!.findViewById(R.id.recycler_countryDialog) as RecyclerView
        val textViewTitle = dialog!!.findViewById(R.id.tvTitle) as TextView
        val rlQueryHolder = dialog!!.findViewById(R.id.rl_query_holder) as RelativeLayout
        val imgClearQuery = dialog!!.findViewById(R.id.img_clear_query) as ImageView
        val editText_search = dialog!!.findViewById(R.id.editText_search) as EditText
        val textView_noResult = dialog!!.findViewById(R.id.tvNoresult) as TextView
        val rlHolder = dialog!!.findViewById(R.id.rlHolder) as RelativeLayout
        val imgDismiss = dialog!!.findViewById(R.id.ivClear) as ImageView

        //dialog background color
        if (codePicker.dialogBackgroundColor !== 0) {
            rlHolder.setBackgroundColor(codePicker.dialogBackgroundColor)
        }
        //close button visibility
        if (codePicker.isShowCloseIcon) {
            imgDismiss.visibility = View.VISIBLE
            imgDismiss.setOnClickListener { dialog!!.dismiss() }
        } else {
            imgDismiss.visibility = View.GONE
        }
        //title
        if (!codePicker.ccpDialogShowTitle) {
            textViewTitle.visibility = View.GONE
        }
        //clear button color and title color
        if (codePicker.dialogTextColor !== 0) {
            val textColor = codePicker.dialogTextColor
            imgClearQuery.setColorFilter(textColor)
            imgDismiss.setColorFilter(textColor)
            textViewTitle.setTextColor(textColor)
            textView_noResult.setTextColor(textColor)
            editText_search.setTextColor(textColor)
            editText_search.setHintTextColor(
                Color.argb(
                    100,
                    Color.red(textColor),
                    Color.green(textColor),
                    Color.blue(textColor)
                )
            )
        }
        //editText tint
        if (codePicker.dialogSearchEditTextTintColor !== 0) {
            editText_search.backgroundTintList =
                ColorStateList.valueOf(codePicker.dialogSearchEditTextTintColor)
            setCursorColor(editText_search, codePicker.dialogSearchEditTextTintColor)
        }
        //add messages to views
        textViewTitle.text = codePicker.dialogTitle
        editText_search.hint = codePicker.searchHintText
        textView_noResult.text = codePicker.noResultACK
        //this will make dialog compact
        if (!codePicker.isSearchAllowed) {
            val params = recyclerView_countryDialog.layoutParams as RelativeLayout.LayoutParams
            params.height = RecyclerView.LayoutParams.WRAP_CONTENT
            recyclerView_countryDialog.layoutParams = params
        }
        val cca = CountryCodeAdapter(
            this.mContext,
            masterCountries!!,
            codePicker,
            rlQueryHolder,
            editText_search,
            textView_noResult,
            dialog!!,
            imgClearQuery
        )
        recyclerView_countryDialog.setLayoutManager(LinearLayoutManager(mContext))
        recyclerView_countryDialog.setAdapter(cca)
        //fast scroller
        val fastScroller = dialog!!.findViewById(R.id.fastscroll) as FastScroller
        fastScroller.setRecyclerView(recyclerView_countryDialog)
        if (codePicker.isShowFastScroller) {
            if (codePicker.fastScrollerBubbleColor !== 0) {
                fastScroller.setBubbleColor(codePicker.fastScrollerBubbleColor)
            }
            if (codePicker.fastScrollerHandleColor !== 0) {
                fastScroller.setHandleColor(codePicker.fastScrollerHandleColor)
            }
            if (codePicker.fastScrollerBubbleTextAppearance !== 0) {
                try {
                    fastScroller.setBubbleTextAppearance(codePicker.fastScrollerBubbleTextAppearance)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            fastScroller.visibility = View.GONE
        }
        dialog!!.setOnDismissListener { dialogInterface ->
            hideKeyboard(mContext)
            if (codePicker.dialogEventsListener != null) {
                codePicker.dialogEventsListener!!.onCcpDialogDismiss(dialogInterface)
            }
        }
        dialog!!.setOnCancelListener { dialogInterface ->
            hideKeyboard(mContext)
            if (codePicker.dialogEventsListener != null) {
                codePicker.dialogEventsListener!!.onCcpDialogCancel(dialogInterface)
            }
        }
        //auto scroll to mentioned countryNameCode
        if (countryNameCode != null) {
            var isPreferredCountry = false
            if (codePicker.preferredCountries != null) {
                for (preferredCountry in codePicker.preferredCountries!!) {
                    if (preferredCountry.nameCode == countryNameCode) {
                        isPreferredCountry = true
                        break
                    }
                }
            }
            if (!isPreferredCountry) {
                var preferredCountriesOffset = 0
                if (codePicker.preferredCountries != null && codePicker.preferredCountries!!.isNotEmpty()) {
                    preferredCountriesOffset =
                        codePicker.preferredCountries!!.size + 1 //+1 is for divider
                }
                for (i in masterCountries.indices) {
                    if (masterCountries.get(i).nameCode == countryNameCode) {
                        recyclerView_countryDialog.scrollToPosition(i + preferredCountriesOffset)
                        break
                    }
                }
            }
        }
        dialog!!.show()
        if (codePicker.dialogEventsListener != null) {
            codePicker.dialogEventsListener!!.onCcpDialogOpen(dialog!!)
        }
    }

    private fun hideKeyboard(mContext: Context) {
        if (mContext is Activity) {
            val activity = mContext as Activity
            val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            //Find the currently focused view, so we can grab the correct window token from it.
            var view = activity.getCurrentFocus()
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = View(activity)
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
        }
    }

    fun setCursorColor(editText: EditText, color: Int) {
        if (sCursorDrawableField == null) {
            return
        }
        try {
            val drawable = getDrawable(
                editText.context,
                sCursorDrawableResourceField!!.getInt(editText)
            )
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN)
            sCursorDrawableField.set(
                sEditorField?.get(editText), arrayOf(drawable, drawable)
            )
        } catch (ignored: Exception) {
        }
    }

    fun clear() {
        if (dialog != null) {
            dialog!!.dismiss()
        }
    }

    private fun getDrawable(mContext: Context, id: Int): Drawable {
        return mContext.getDrawable(id)!!
    }
}