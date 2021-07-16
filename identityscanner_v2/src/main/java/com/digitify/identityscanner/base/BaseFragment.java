package com.digitify.identityscanner.base;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.digitify.identityscanner.interfaces.IBase;

public abstract class BaseFragment extends Fragment implements IBase.View {
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (!(context instanceof IBase.View)) {
            throw new IllegalStateException("Caller Activity is not instance of IBase.View");
        }
    }

    private IBase.View getBaseView() {
        return (IBase.View) getContext();
    }

    @Override
    public void showToast(String message) {
        getBaseView().showToast(message);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // getActivity().setTitle(getScreenTitle());
    }

    protected abstract String getScreenTitle();

}
