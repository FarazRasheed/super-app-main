package co.yap.widgets.searchwidget;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import co.yap.yapcore.R;
import co.yap.yapcore.helpers.Utils;

public class CoreSearchView extends RelativeLayout implements TextWatcher, TextView.OnEditorActionListener {

    private EditText mEtSearch;
    private TextView tvCancel;
    private ProgressBar pbLoadingIndicator;
    private boolean enableProgressbar;


    private boolean IsSearching;
    private boolean FireOnClick = true;
    private CountDownTimer timer = null;


    private SearchingListener searchingListener;


    public CoreSearchView(Context context) {
        super(context);
    }


    public CoreSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public CoreSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void initializeSearch(Context context, SearchingListener searchingListener, boolean IsSearching, boolean fireOnClickOnly) {
        this.searchingListener = searchingListener;
        this.IsSearching = IsSearching;
        this.FireOnClick = fireOnClickOnly;
        this.enableProgressbar = false;
        initializeView(context, null);
    }

    private void initializeView(Context context, AttributeSet attrs) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_searchview, this, true);
        initComponents(view);
    }


    public void enableLoadingIndicator(boolean enable) {
        enableProgressbar = enable;
        if (!enableProgressbar)
            pbLoadingIndicator.setVisibility(GONE);
    }


    public void updateLoadingState(boolean isLoading) {
        if (enableProgressbar)
            pbLoadingIndicator.setVisibility(isLoading ? VISIBLE : View.GONE);
    }


    private void initComponents(View view) {

        mEtSearch = view.findViewById(R.id.etSearch);
        tvCancel = view.findViewById(R.id.tvCancel);
        pbLoadingIndicator = view.findViewById(R.id.pb_loading_indicator);
        if (searchingListener != null) {
            mEtSearch.clearFocus();
            mEtSearch.setFocusable(false);
            mEtSearch.setOnClickListener(view12 -> {
                tvCancel.setVisibility(VISIBLE);
                setFocus(mEtSearch);
            });
            AddListeners();
        } else {
            mEtSearch.setEnabled(false);
        }
        tvCancel.setOnClickListener(view1 -> cancelSearch());
    }

    public void cancelSearch() {
        if (searchingListener != null) {
            mEtSearch.getText().clear();
            mEtSearch.setFocusable(false);
            tvCancel.setVisibility(GONE);
            searchingListener.onCancel();
        }
    }


    public void clearInputField() {
        if (mEtSearch != null)
            mEtSearch.getText().clear();
    }


    public void addOnCancelListener(SearchingListener searchingListener) {
        this.searchingListener = searchingListener;
    }


    private void AddListeners() {


        registerTextChangeListener();
        mEtSearch.setOnEditorActionListener(this);
    }


    public void registerTextChangeListener() {
        mEtSearch.addTextChangedListener(this);
    }


    public void unRegisterTextChangeListener() {
        mEtSearch.removeTextChangedListener(this);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }


    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }


    @Override
    public void afterTextChanged(Editable s) {
        if (mEtSearch.getText().toString().length() == 0) {
            IsSearching = false;
            searchData();
        } else if (!mEtSearch.getText().toString().trim().equalsIgnoreCase("")) {
            IsSearching = true;
            if (!FireOnClick)
                TimerLogic();
        }
    }


    private void searchData() {
        if (searchingListener != null)
            searchingListener.onTypingSearch(IsSearching, mEtSearch.getText().toString());
    }


    private void searchDataOnSearchKeyPressed() {
        searchingListener.onSearchKeyPressed(mEtSearch.getText().toString());
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            if (!mEtSearch.getText().toString().trim().equalsIgnoreCase("")) {
                IsSearching = true;
                searchDataOnSearchKeyPressed();
            } else {
                IsSearching = false;
            }
            Utils.INSTANCE.hideKeyboard(v);
            return true;
        }
        return false;
    }


    private void TimerLogic() {
        if (timer != null) {
            timer.cancel();
        }
        timer = new CountDownTimer(800, 800) {


            public void onTick(long millisUntilFinished) {
            }


            public void onFinish() {
                if (IsSearching) {
                    if (searchingListener != null)
                        searchingListener.onTypingSearch(IsSearching, mEtSearch.getText().toString());
                }
            }
        }.start();
    }


    public EditText getEditText() {
        return mEtSearch;
    }


    public void setFocus(View view) {
        mEtSearch.setFocusable(true);
        Utils.INSTANCE.requestKeyboard(view, true, true);
    }

}
