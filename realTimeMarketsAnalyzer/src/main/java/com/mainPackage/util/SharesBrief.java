package com.mainPackage.util;

import java.util.List;

/**
 * Created by claudiu on 02.05.2017.
 */
public class SharesBrief {
    private List<Share> shareList;

    public SharesBrief(List<Share> shareList) {
        this.shareList = shareList;
    }

    public List<Share> getShareList() {
        return shareList;
    }
}
