package co.yap.yapcore.leanplum

data class UserAttributes(
    val accountType: String = "accountType",
    val email: String = "email",
    val nationality: String = "nationality",
    val firstName: String = "firstName",
    val lastName: String = "lastName",
    val documentsVerified: String = "documentsVerified",
    val mainUser: String = "mainUser",
    val householdUser: String = "householdUser",
    val youngUser: String = "youngUser",
    val b2bUser: String = "b2bUser",
    val country: String = "country",
    val emailVerified: String = "emailVerified",
    val phoneNumberVerified: String = "phoneNumberVerified",
    val city: String = "city",
    val uuid: String = "uuid",
    val customerId: String = "customerId",
    val signup_timestamp: String = "signup_timestamp",
    val signup_length: String = "signup_length",
    val biometric_login_enabled: String = "biometric_login_enabled",
    val eid_expired: String = "eid_expired",
    val account_active: String = "account_active",
    val eid_expiry_date: String =  "eid_expiry_date"
)