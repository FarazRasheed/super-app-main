package co.yap.sendmoney.addbeneficiary.models


data class Bank(

    internal var id: String? = null,
    var other_bank_name: String? = "",
    internal var other_branch_addr1: String? = "",
    private var identifier_code1: String? = null,
    private var identifier_code2: String? = null,
    private var other_branch_addr2: String? = null,
    private var other_branch_name: String? = null

)