package com.digitify.identityscanner.docscanner.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.digitify.identityscanner.R;
import com.digitify.identityscanner.databinding.FragmentDocReviewBinding;
import com.digitify.identityscanner.base.BaseFragment;
import com.digitify.identityscanner.docscanner.enums.DocumentPageType;
import com.digitify.identityscanner.docscanner.enums.DocumentType;
import com.digitify.identityscanner.docscanner.interfaces.IDocReview;
import com.digitify.identityscanner.docscanner.viewmodels.DocReviewViewModel;
import com.digitify.identityscanner.docscanner.viewmodels.IdentityScannerViewModel;

public class DocReviewFragment extends BaseFragment implements IDocReview.View {
    private static String FILE_PATH = "filepath";
    private static String DOC_TYPE = "docType";
    private static String DOC_PAGE_TYPE = "docPageType";

    public static DocReviewFragment get(String filePath, DocumentType docType, DocumentPageType pageType) {
        DocReviewFragment fragment = new DocReviewFragment();
        Bundle b = new Bundle();
        b.putString(FILE_PATH, filePath);
        b.putString(DOC_PAGE_TYPE, pageType.toString());
        b.putString(DOC_TYPE, docType.toString());
        fragment.setArguments(b);
        return fragment;
    }

    private DocReviewViewModel viewModel;
    private IdentityScannerViewModel parentViewModel;
    private String filePath;
    private DocumentPageType pageType;
    private DocumentType docType;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentDocReviewBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_doc_review, container, false);
        View view = binding.getRoot();

        binding.setModel(getViewModel());

        return view;
    }

    @Override
    protected String getScreenTitle() {
        return getString(R.string.review_doc_title);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getViewModel().init(getFilePath(), getDocType(), getDocumentPageType(), this);
        getViewModel().onStart();

        getViewModel().getSingleClickEvent().observe(this, integer -> {
            if (integer == R.id.btnDone) {
                requestDone();

            }
        });
    }

    public String getFilePath() {
        if (TextUtils.isEmpty(filePath)) {
            Bundle b = getArguments();
            if (b != null) {
                filePath = b.getString(FILE_PATH);
            }
        }
        return filePath;
    }

    public DocumentPageType getDocumentPageType() {
        if (pageType == null) {
            Bundle b = getArguments();
            if (b != null) {
                pageType = DocumentPageType.valueOf(b.getString(DOC_PAGE_TYPE));
            }
        }
        return pageType;
    }

    public DocumentType getDocType() {
        if (docType == null) {
            Bundle b = getArguments();
            if (b != null) {
                docType = DocumentType.valueOf(b.getString(DOC_TYPE));
            }
        }
        return docType;
    }

    public DocReviewViewModel getViewModel() {
        if (viewModel == null)
            viewModel = ViewModelProviders.of(this).get(DocReviewViewModel.class);
        return viewModel;
    }

    public IdentityScannerViewModel getParentViewModel() {
        if (parentViewModel == null)
            parentViewModel = ViewModelProviders.of(getActivity()).get(IdentityScannerViewModel.class);
        return parentViewModel;
    }

    @Override
    public void requestCancel() {
        getParentViewModel().onPictureReviewCancelled();
    }

    @Override
    public void requestDone() {
        if (!viewModel.isDone)
            getParentViewModel().onPictureReviewComplete(getFilePath());
    }

    @Override
    public void requestRetake() {
        viewModel.isDone = false;
        getParentViewModel().onPictureReviewFailed();
    }

    @Override
    public void onDestroy() {
        getViewModel().onStop();
        super.onDestroy();
    }
}
