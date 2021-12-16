package com.example.hangmanwords;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String Database_Name = "shoppingDB.db";
    private static final int Version = 3;

    private static final String Table_Users = "users";
    private static final String Table_Users_Id = "id";
    private static final String Table_Users_UserName = "username";
    private static final String Table_Users_HashedPassword = "hashedPassword";
    private static final String Table_Users_Winstreak = "winstreak";

    private static final String Create_Table_Users = "CREATE TABLE " + Table_Users +
                                "(" +
                                "'" + Table_Users_Id + "' INTEGER PRIMARY KEY AUTOINCREMENT," +
                                "'" + Table_Users_UserName + "' VARCHAR(250) UNIQUE NOT NULL," +
                                "'" + Table_Users_HashedPassword + "' VARCHAR(500) NOT NULL," +
                                "'" + Table_Users_Winstreak + "' NUMERIC DEFAULT 0)";

    private static final String Drop_Database_If_Exists = "DROP TABLE IF EXISTS " + Table_Users;
    private static final String Select_Top_Three_Users = "Select * FROM " + Table_Users + " ORDER BY " + Table_Users_Winstreak + " DESC LIMIT 3";

    public SQLiteHelper(Context context) {
        super(context, Database_Name, null, Version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Create_Table_Users);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(Drop_Database_If_Exists);

        onCreate(sqLiteDatabase);
    }

    public boolean registerUser(User user){
        SQLiteDatabase database = getWritableDatabase();

        boolean isExistingUser = userExists(user.getUsername());

        if (isExistingUser){
            return false; //could not reregister the already registered user
        }

        database.beginTransaction();

        try{
            ContentValues values = new ContentValues();
            values.put(Table_Users_UserName, user.getUsername());

            String hashedPassword = PasswordHasher.get_SHA_512_SecurePassword(user.getPassword());
            values.put(Table_Users_HashedPassword, hashedPassword);

            database.insertOrThrow(Table_Users, null, values);
            database.setTransactionSuccessful();
        }
        catch(Exception e){
            String error = e.getMessage().toString();
        }
        finally {
            database.endTransaction();
        }

        return true;
    }

    public boolean userExists(String username){
        SQLiteDatabase database = getWritableDatabase();

        database.beginTransaction();

        try {
            String usersSelectQuery = "SELECT * FROM " + Table_Users + " WHERE " + Table_Users_UserName + " = " + "'" + username + "'";
            Cursor cursor = database.rawQuery(usersSelectQuery,null);

            if (cursor.moveToFirst()){
                return true;
            }
        }
        catch(Exception e){
            String error = e.getMessage().toString();
        }
        finally{
            database.endTransaction();
        }

        return false;
    }

    @SuppressLint("Range")
    public User getSingleUserInfo(User input){

        SQLiteDatabase database = this.getWritableDatabase();

        User user = new User();

        try{
            Cursor cursor = database.rawQuery("SELECT * FROM " + Table_Users + " WHERE " + Table_Users_UserName + " = " + "'" + input.getUsername() + "'",null);
            cursor.moveToFirst();

            user.setUsername(cursor.getString(cursor.getColumnIndex(Table_Users_UserName)));
            user.setHashedPassword(cursor.getString(cursor.getColumnIndex(Table_Users_HashedPassword)));
            user.setWinstreak(cursor.getInt(cursor.getColumnIndex(Table_Users_Winstreak)));
            cursor.close();
        }
        catch (Exception e){
            user = null;
            String error = e.getMessage().toString();
        }
        finally {
            database.close();
        }

        return user;
    }

    public void updateUserScore(User user, int currentStreak){

        SQLiteDatabase database = getWritableDatabase();

        database.beginTransaction();

        try{
            ContentValues values = new ContentValues();
            values.put(Table_Users_Winstreak, currentStreak);

            database.update(Table_Users, values, Table_Users_UserName + " = ? and " + Table_Users_HashedPassword + " = ?", new String[]{user.getUsername(), user.getHashedPassword()} );
            database.setTransactionSuccessful();
        }
        catch(Exception e){
            //Catches exception
        }
        finally {
            database.endTransaction();
        }
    }

    @SuppressLint("Range")
    public List<String> topThreeUsers (){
        List<String> model = new ArrayList<>();

        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor =  database.rawQuery(Select_Top_Three_Users, null);

        try{
            if (cursor.moveToFirst())
            {
                do {
                    String username = cursor.getString(cursor.getColumnIndex(Table_Users_UserName));
                    int winstreak = cursor.getInt(cursor.getColumnIndex(Table_Users_Winstreak));

                    model.add(username + " - " + winstreak);
                }while(cursor.moveToNext());
            }
        }
        catch(Exception e){
            String error = e.getMessage().toString();
        }
        finally {
            cursor.close();
            database.close();
        }

        return  model;
    }
}
