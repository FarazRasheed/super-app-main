package co.yap.modules.location;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import androidx.appcompat.widget.AppCompatTextView;
import java.util.ArrayList;
import co.yap.countryutils.country.Country;
import co.yap.widgets.CoreCircularImageView;
import co.yap.yapcore.R;

public class CustomAutoCompleteAdapter extends BaseAdapter implements Filterable {

    Context context;

    private final Object mLock = new Object();
    private ArrayList<Country> mOriginalValues;
    private ArrayList<Country> mBackupStrings;
    private StringFilter mFilter;
    private OnItemSelectClickListener mSelectListener;
    private OnItemDeleteClickListener mDeleteListener;
    private int filteredCount = 0;

    AppCompatTextView itemName;
    CoreCircularImageView ivCountry;

    private LayoutInflater mInflater;

    public interface OnItemSelectClickListener {
        public void onItemSelectClicked();
    }

    public interface OnItemDeleteClickListener {
        public void onItemDeleteClicked();
    }

    /**
     * Set listener for clicks on the footer item
     */
    public void setOnItemSelectClickListener(OnItemSelectClickListener listener) {
        mSelectListener = listener;
    }

    /*public void setOnItemDeleteClickListener(OnItemDeleteClickListener listener) {
        mDeleteListener = listener;
    }*/

    public CustomAutoCompleteAdapter(Context context, ArrayList<Country> objects) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mOriginalValues = objects;
        mBackupStrings = objects;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mOriginalValues.size();
    }

    @Override
    public Object getItem(int position) {
        return mOriginalValues.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View rowView = inflater.inflate(R.layout.item_list_country_code, parent, false);
        itemName = (AppCompatTextView) rowView.findViewById(R.id.tvCountryName);
        ivCountry = (CoreCircularImageView) rowView.findViewById(R.id.ivCountry);
        itemName.setText(mOriginalValues.get(position).getName());
        ivCountry.setImageResource(mOriginalValues.get(position).getFlagDrawableResId(itemName.getContext()));
        return rowView;
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new StringFilter();
        }
        return mFilter;
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
                if (country.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                    filterStrings.add(country);
                }
            }
            filterResults.count = filterStrings.size();
            filterResults.values = filterStrings;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mOriginalValues = (ArrayList) results.values;
            filteredCount = results.count;
            if (constraint != null) {
                notifyDataSetChanged();
            }
        }
    }

   public int getFilteredCount(){
        return filteredCount;
    }
}
