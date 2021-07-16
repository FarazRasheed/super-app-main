package co.yap.yapcore.enums


enum class AccountBlockSeverityLevel(val freezeCode: String) {
    TOTAL_BLOCK("T"),   // total block
    DEBIT_BLOCK("D"),   // Debit transaction block
    CREDIT_BLOCK("C"),   // Credit transaction block
    HOTLISTED_BLOCK("H"),   //  Hotlisted
    EID_BLOCK("E")   //  EID Expired
}