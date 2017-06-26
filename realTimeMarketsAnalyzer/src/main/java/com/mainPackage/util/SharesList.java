package com.mainPackage.util;

import java.util.List;

/**
 * Created by claudiu on 02.05.2017.
 */
public class SharesList {
    private List<Share> shareList;
    private Share share;

    public SharesList(List<Share> shareList) {
        this.shareList = shareList;
    }

    public List<Share> getSharesList() {
        return shareList;
    }
}
