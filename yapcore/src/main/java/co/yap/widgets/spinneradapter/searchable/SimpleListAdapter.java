package co.yap.widgets.spinneradapter.searchable;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import co.yap.countryutils.country.Country;
import co.yap.widgets.CoreCircularImageView;
import co.yap.yapcore.R;

public class SimpleListAdapter extends BaseAdapter implements Filterable, ISpinnerSelectedView {

    private Context mContext;
    private ArrayList<Country> mBackupStrings;
    private ArrayList<Country> mStrings;
    private StringFilter mStringFilter = new StringFilter();

    public SimpleListAdapter(Context context, ArrayList<Country> strings) {
        mContext = context;
        mStrings = strings;
        mBackupStrings = strings;
    }

    @Override
    public int getCount() {
        return mStrings == null ? 0 : mStrings.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        if (mStrings != null && position > 0)
            return mStrings.get(position - 1);
        else
            return null;
    }

    @Override
    public long getItemId(int position) {
        if (mStrings == null && position > 0)
            return mStrings.get(position).hashCode();
        else
            return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (position == 0) {
            view = getNoSelectionView();
        } else {
            view = View.inflate(mContext, R.layout.item_list_country_code, null);
            CoreCircularImageView letters = view.findViewById(R.id.ivCountry);
            TextView dispalyName = view.findViewById(R.id.tvCountryName);
            dispalyName.setText(mStrings.get(position - 1).getName());
            letters.setImageResource(mStrings.get(position-1).getFlagDrawableResId(dispalyName.getContext()));
        }
        return view;
    }

    @Override
    public View getSelectedView(int position) {
        View view = null;
        if (position == 0) {
            view = getNoSelectionView();
        } else {
            view = View.inflate(mContext, R.layout.item_list_country_code, null);
            CoreCircularImageView letters = view.findViewById(R.id.ivCountry);
            TextView dispalyName = view.findViewById(R.id.tvCountryName);
            dispalyName.setText(mStrings.get(position - 1).getName());
            letters.setImageResource(mStrings.get(position-1).getFlagDrawableResId(dispalyName.getContext()));
        }
        return view;
    }

    @Override
    public View getNoSelectionView() {
        View v = View.inflate(mContext, R.layout.item_list_no_selection, null);
        TextView TxtVw_NoSelection = v.findViewById(R.id.TxtVw_NoSelection);
        TxtVw_NoSelection.setText("No Selection");
        return v;
    }

    @Override
    public Filter getFilter() {
        return mStringFilter;
    }

    public enum ItemViewType {
        ITEM, NO_SELECTION_ITEM
    }

    public class StringFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            final FilterResults filterResults = new FilterResults();
            if (TextUtils.isEmpty(constraint)) {
                filterResults.count = mBackupStrings.size();
                filterResults.values = mBackupStrings;
                return filterResults;
            }
            final ArrayList<Country> filterStrings = new ArrayList<>();
            for (Country country : mBackupStrings) {
                if (country.getName().toLowerCase().contains(constraint)) {
                    filterStrings.add(country);
                }
            }
            filterResults.count = filterStrings.size();
            filterResults.values = filterStrings;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mStrings = (ArrayList) results.values;
            notifyDataSetChanged();
        }
    }
}
