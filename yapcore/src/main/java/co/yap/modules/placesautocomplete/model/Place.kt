package co.yap.modules.placesautocomplete.model

data class Place(
  val id: String,
  val description: String,
  val mainText:String,
  val secondaryText:String
) {
  override fun toString(): String {
    return ""
  }
}