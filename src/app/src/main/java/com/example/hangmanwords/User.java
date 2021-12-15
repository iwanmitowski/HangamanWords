package com.example.hangmanwords;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    String username;
    String hashedPassword;
    String password;
    int winstreak;

    public User() {
    }

    protected User(Parcel in) {
        username = in.readString();
        hashedPassword = in.readString();
        password = in.readString();
        winstreak = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(username);
        parcel.writeString(hashedPassword);
        parcel.writeString(password);
        parcel.writeInt(winstreak);
    }
}
