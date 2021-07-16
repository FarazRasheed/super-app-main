package com.digitify.identityscanner.docscanner.enums;

public enum DocumentType {
    EID, PASSPORT, UNKNOWN;

    public int getNumberOfPages() {
        switch (this) {
            case EID:
                return 2;
            case UNKNOWN:
                return 0;
            case PASSPORT:
                return 1;
            default:
                return 0;
        }
    }
}
