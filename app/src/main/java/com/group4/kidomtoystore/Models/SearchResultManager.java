package com.group4.kidomtoystore.Models;

import java.util.ArrayList;

public class SearchResultManager {
    private static SearchResultManager instance;
    private ArrayList<ResultSearch> searchResults;

    private SearchResultManager() {
        searchResults = new ArrayList<>();
    }

    public static synchronized SearchResultManager getInstance() {
        if (instance == null) {
            instance = new SearchResultManager();
        }
        return instance;
    }

    public ArrayList<ResultSearch> getSearchResults() {
        return searchResults;
    }

    public void addSearchResult(ResultSearch resultSearch) {
        searchResults.add(resultSearch);
    }

    // Các phương thức khác để quản lý searchResults nếu cần
}
