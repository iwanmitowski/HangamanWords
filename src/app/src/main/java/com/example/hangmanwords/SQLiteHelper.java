package com.example.hangmanwords;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import at.favre.lib.crypto.bcrypt.BCrypt;


public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String Database_Name = "shoppingDB.db";
    public static final int Version = 2;

    public static final String Table_Users = "users";
    public static final String Table_Users_Id = "id";
    public static final String Table_Users_UserName = "username";
    public static final String Table_Users_HashedPassword = "hashedPassword";
    public static final String Table_Users_Winstreak = "winstreak";

    public static final String Create_Table_Users = "CREATE TABLE " + Table_Users +
            "(" +
            "'" + Table_Users_Id + "' INTEGER PRIMARY KEY AUTOINCREMENT," +
            "'" + Table_Users_UserName + "' VARCHAR(250) UNIQUE NOT NULL," +
            "'" + Table_Users_HashedPassword + "' VARCHAR(500) NOT NULL," +
            "'" + Table_Users_Winstreak + "' NUMERIC DEFAULT 0)";

    public SQLiteHelper(Context context) {
        super(context, Database_Name, null, Version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Create_Table_Users);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Table_Users);

        onCreate(sqLiteDatabase);
    }

    //При регистрация
    //В базата се проверява има ли такъв user

    //+ Ако има
    //Toast отказващ регистрация

    //- Ако няма
    //Записваме го и insert-ваме хешираната парола


    //При логин
    //В базата се проверява има ли такъв user

    //+ Ако има:
    //В базата се бърка където името е идентично и се сравнява паролата със записания hash

    //- Ako няма:
    //Toast подканващ към регистраиця


    //Winstreak: най-много победи направени подред, не само за момента
    //Update winstreak при бесене:
    //След всяка позната дума, се обновява winstreak-a, ako e по-добър от предишния

    public String get_SHA_512_SecurePassword(String passwordToHash){
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update("".getBytes(StandardCharsets.UTF_8));
            byte[] bytes = md.digest(passwordToHash.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return generatedPassword;
    }


    public boolean registerUser(User user){
        SQLiteDatabase database = getWritableDatabase();

        boolean isExistingUser = userExists(user.username);

        if (isExistingUser){
            return false; //could not reregister the already registered user
        }

        database.beginTransaction();

        try{
            ContentValues values = new ContentValues();
            values.put(Table_Users_UserName, user.username);

            String hashedPassword = get_SHA_512_SecurePassword(user.password);
            values.put(Table_Users_HashedPassword, hashedPassword);

            database.insertOrThrow(Table_Users, null, values);
            database.setTransactionSuccessful();
        }
        catch(Exception e){
            //Catches exception
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
            Cursor cursor = database.rawQuery("SELECT * FROM " + Table_Users + " WHERE " + Table_Users_UserName + " = " + "'" + input.username + "'",null);
            cursor.moveToFirst();

            user.username = cursor.getString(cursor.getColumnIndex(Table_Users_UserName));
            user.hashedPassword = cursor.getString(cursor.getColumnIndex(Table_Users_HashedPassword));
            user.winstreak = cursor.getInt((cursor.getColumnIndex(Table_Users_Winstreak)));
            cursor.close();
        }
        catch (Exception e){
            return null;
        }
        finally {
            database.close();

        }

        return user;
    }
}
