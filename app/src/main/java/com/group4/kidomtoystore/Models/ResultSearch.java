package com.group4.kidomtoystore.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ResultSearch implements Parcelable {
    String searchResult;
    int imageDelete;

    public ResultSearch(String searchResult, int imageDelete) {
        this.searchResult = searchResult;
        this.imageDelete = imageDelete;
    }

    public String getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(String searchResult) {
        this.searchResult = searchResult;
    }

    public int getImageDelete() {
        return imageDelete;
    }

    public void setImageDelete(int imageDelete) {
        this.imageDelete = imageDelete;
    }
    protected ResultSearch(Parcel in) {
        searchResult = in.readString();
        imageDelete = in.readInt();
    }
    public static final Creator<ResultSearch> CREATOR = new Creator<ResultSearch>() {
        @Override
        public ResultSearch createFromParcel(Parcel in) {
            return new ResultSearch(in);
        }

        @Override
        public ResultSearch[] newArray(int size) {
            return new ResultSearch[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(searchResult);
        dest.writeInt(imageDelete);
    }
}
