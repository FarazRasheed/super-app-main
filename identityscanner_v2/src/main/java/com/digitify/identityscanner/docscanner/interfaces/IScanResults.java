package com.digitify.identityscanner.docscanner.interfaces;


import com.digitify.identityscanner.docscanner.models.IdentityScannerResult;
import com.digitify.identityscanner.docscanner.states.ScanResultsState;
import com.digitify.identityscanner.docscanner.viewmodels.IdentityScannerViewModel;
import com.digitify.identityscanner.docscanner.viewmodels.ScanResultsViewModel;
import com.digitify.identityscanner.interfaces.IBase;

public interface IScanResults {

    interface View extends IBase.View {
        IdentityScannerResult getResults();
        ScanResultsViewModel getViewModel();
        IdentityScannerViewModel getParentViewModel();
    }

    interface ViewModel extends IBase.ViewModel {
        IScanResults.View getView();

        void init(IdentityScannerResult scanResults, IScanResults.View view);

        ScanResultsState getState();
    }
}
