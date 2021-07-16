package co.yap.yapcore.enums

enum class AlertType {
    TOAST,
    DIALOG,
    DIALOG_WITH_FINISH,
    DIALOG_WITH_CUSTOM_BUTTON_TEXT,
    DIALOG_WITH_CLICKABLE; // it is used to make a dialog message clickable like //usage "{msg}^{links}^{AlertType.DIALOG_WITH_CLICKABLE}"
}