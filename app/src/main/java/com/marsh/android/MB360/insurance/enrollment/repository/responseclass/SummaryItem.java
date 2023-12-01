package com.marsh.android.MB360.insurance.enrollment.repository.responseclass;

public class SummaryItem {
    int id;
    String key;
    String value;
    boolean isHeader;
    boolean isFooter;

    public SummaryItem(int id, String key, String value, boolean isHeader, boolean isFooter) {
        this.id = id;
        this.key = key;
        this.value = value;
        this.isHeader = isHeader;
        this.isFooter = isFooter;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }

    public boolean isFooter() {
        return isFooter;
    }

    public void setFooter(boolean footer) {
        isFooter = footer;
    }
}
