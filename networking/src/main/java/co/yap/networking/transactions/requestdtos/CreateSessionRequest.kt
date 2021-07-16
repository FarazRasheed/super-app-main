package co.yap.networking.transactions.requestdtos

import com.google.gson.annotations.SerializedName

class CreateSessionRequest(@SerializedName("order") var order: Order)