package com.hwichance.android.WhereIsMyMask.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LocationSearchMeta {

    @SerializedName("is_end")
    @Expose
    private boolean endPage;

    public boolean isEndPage() {
        return endPage;
    }

    public void setEndPage(boolean _endPage) {
        this.endPage = _endPage;
    }

}
