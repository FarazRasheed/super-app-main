package co.yap.widgets.country_spinner;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.List;

import co.yap.countryutils.country.Country;
import co.yap.yapcore.R;
import co.yap.yapcore.interfaces.OnItemClickListener;


public class CountrySpinner extends LinearLayout {

    private Spinner _spinner;
    private CountryListAdapter countryAdapter;
    private OnItemClickListener itemSelectedListener;

    public CountrySpinner(Context context) {
        super(context);
        init(null);
    }

    public CountrySpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CountrySpinner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        inflate(getContext(), R.layout.spinner_view, this);
        this._spinner = findViewById(R.id.countriesSpinner);
    }

    public void setSelectedItem(int position) {
        this._spinner.setSelection(position);
    }
    public void setAdapter(List<Country> countries) {
        this.countryAdapter = getCountryAdapter(countries);

            _spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                    if (itemSelectedListener != null && view != null) {
                        itemSelectedListener.onItemClick(view, countryAdapter.getItem(position), position);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        _spinner.setAdapter(countryAdapter);

    }

    public void setItemSelectedListener(OnItemClickListener listener) {
        this.itemSelectedListener = listener;
    }

    public void setEnabledSpinner(boolean isEnabled) {
        _spinner.setEnabled(isEnabled);
    }

        public void showDropDownArrow(boolean isShow) {
        if (!isShow)
            _spinner.setBackground(null);
    }

    private CountryListAdapter getCountryAdapter(List<Country> countries) {
        if (countryAdapter == null)
            countryAdapter = new CountryListAdapter(getContext(), countries);
        return countryAdapter;
    }


}