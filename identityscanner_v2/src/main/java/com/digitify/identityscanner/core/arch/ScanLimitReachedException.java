package com.digitify.identityscanner.core.arch;

public class ScanLimitReachedException extends IllegalStateException {
    public ScanLimitReachedException() {
        super("Scan Limit reached");
    }
}
