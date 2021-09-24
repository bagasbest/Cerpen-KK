package com.cerpenkimia.koloid.profil;

import android.os.Parcel;
import android.os.Parcelable;

public class ProfileModel implements Parcelable {

    private String avatar;
    private String name;
    private String nim;
    private String lecturer1;
    private String lecturer2;

    public ProfileModel() {}

    protected ProfileModel(Parcel in) {
        avatar = in.readString();
        name = in.readString();
        nim = in.readString();
        lecturer1 = in.readString();
        lecturer2 = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(avatar);
        dest.writeString(name);
        dest.writeString(nim);
        dest.writeString(lecturer1);
        dest.writeString(lecturer2);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProfileModel> CREATOR = new Creator<ProfileModel>() {
        @Override
        public ProfileModel createFromParcel(Parcel in) {
            return new ProfileModel(in);
        }

        @Override
        public ProfileModel[] newArray(int size) {
            return new ProfileModel[size];
        }
    };

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getLecturer1() {
        return lecturer1;
    }

    public void setLecturer1(String lecturer1) {
        this.lecturer1 = lecturer1;
    }

    public String getLecturer2() {
        return lecturer2;
    }

    public void setLecturer2(String lecturer2) {
        this.lecturer2 = lecturer2;
    }
}
