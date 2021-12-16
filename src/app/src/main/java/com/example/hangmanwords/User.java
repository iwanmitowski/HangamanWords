package com.example.hangmanwords;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String username;
    private String hashedPassword;
    private String password;
    private int winstreak;

    public User() {
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getHashedPassword()
    {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword)
    {
        this.hashedPassword = hashedPassword;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public int getWinstreak()
    {
        return winstreak;
    }

    public void setWinstreak(int winstreak)
    {
        this.winstreak = winstreak;
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
