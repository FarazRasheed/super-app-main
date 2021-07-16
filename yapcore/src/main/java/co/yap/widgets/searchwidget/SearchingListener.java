package co.yap.widgets.searchwidget;

public interface SearchingListener {
    void onCancel();

    default void onTypingSearch(boolean isSearching, String search){

    }

    void onSearchKeyPressed(String search);
}
