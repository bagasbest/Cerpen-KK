package com.cerpenkimia.koloid.cerpen;

import android.os.Parcel;
import android.os.Parcelable;

public class CerpenModel implements Parcelable {

    private String title;
    private String description;
    private String dp;
    private String cerpenId;

    public CerpenModel() {}

    protected CerpenModel(Parcel in) {
        title = in.readString();
        description = in.readString();
        dp = in.readString();
        cerpenId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(dp);
        dest.writeString(cerpenId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CerpenModel> CREATOR = new Creator<CerpenModel>() {
        @Override
        public CerpenModel createFromParcel(Parcel in) {
            return new CerpenModel(in);
        }

        @Override
        public CerpenModel[] newArray(int size) {
            return new CerpenModel[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public String getCerpenId() {
        return cerpenId;
    }

    public void setCerpenId(String cerpenId) {
        this.cerpenId = cerpenId;
    }
}
