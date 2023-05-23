package dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import Model.UserStorageData;

public class MyInfoDataBase extends SQLiteOpenHelper {
    private Context context;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MyInfoDataBase";

    // Users table
    private static final String TABLE_USERS_NAME = "users";
    private static String USERS_COLUMN_ID = "id";
    private static final String USERS_COLUMN_USERNAME = "username";
    private static final String USERS_COLUMN_EMAIL = "email";
    private static final String USERS_COLUMN_GENDER = "gender";
    private static final String USERS_COLUMN_BIRTHDAY = "birthday";
    private static final String USERS_COLUMN_PASSWORD = "password";
    private static final String USER_COLUMN__IMAGE = "image";
    private static final String USERS_COLUMN_TYPE = "type";

    private static final String[] TABLE_USERS_COLUMNS = {USERS_COLUMN_ID, USERS_COLUMN_USERNAME, USERS_COLUMN_EMAIL,
            USERS_COLUMN_GENDER, USERS_COLUMN_BIRTHDAY, USERS_COLUMN_PASSWORD, USER_COLUMN__IMAGE,USERS_COLUMN_TYPE};
    private SQLiteDatabase db = null;

    public MyInfoDataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            // SQL statement to create user table
            String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USERS_NAME +
                    " ("
                    + USERS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + USERS_COLUMN_USERNAME + " TEXT, "
                    + USERS_COLUMN_EMAIL + " TEXT, "
                    + USERS_COLUMN_GENDER + " TEXT, "
                    + USERS_COLUMN_BIRTHDAY + " TEXT, "
                    + USERS_COLUMN_PASSWORD + " TEXT, "
                    + USER_COLUMN__IMAGE + " BLOB, "
                    + USERS_COLUMN_TYPE + " TEXT);";
            db.execSQL(CREATE_USER_TABLE);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        try {
//            // drop user table if already exists
//            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS_NAME);
//        } catch (Throwable t) {
//            t.printStackTrace();
//        }
//        onCreate(db);
    }

    public void createUser(UserStorageData user) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            // Make values to be inserted
            ContentValues values = new ContentValues();
            values.put(USERS_COLUMN_USERNAME, user.getUserName());
            values.put(USERS_COLUMN_EMAIL, user.getEmail());
            values.put(USERS_COLUMN_GENDER, user.getGen());
            values.put(USERS_COLUMN_BIRTHDAY, user.getBirthday());
            values.put(USERS_COLUMN_PASSWORD, user.getPassword());

            Bitmap image = user.getImage();
            if (image != null) {
                byte[] data = getBitmapAsByteArray(image);
                if (data.length > 0) {
                    values.put(USER_COLUMN__IMAGE, data);
                }
            }
            values.put(USERS_COLUMN_TYPE, user.getType());

            // Insert the values into the database
            long result=db.insert(TABLE_USERS_NAME, null, values);
            if(result==-1)
            {
                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(context,"Added successfully!",Toast.LENGTH_SHORT).show();
            }
            //db.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    public UserStorageData readUser(int id) {
        UserStorageData user = null;
        try (Cursor cursor = db
                .query(TABLE_USERS_NAME, TABLE_USERS_COLUMNS, USERS_COLUMN_ID + " = ?",
                        new String[]{String.valueOf(id)},
                        null, null,
                        null, null)) {
            //get reference of the itemDB database
            // if results !=null, parse the first one
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                user = new UserStorageData();
                user.setUID(cursor.getInt(0));
                user.setUserName(cursor.getString(1));
                user.setEmail(cursor.getString(2));
                user.setGen(cursor.getString(3));
                user.setBirthday(cursor.getString(4));
                user.setPassword(cursor.getString(5));
                //images
                byte[] img1Byte = cursor.getBlob(6);
                if (img1Byte != null && img1Byte.length > 0) {
                    Bitmap image1 = BitmapFactory.decodeByteArray(img1Byte, 0, img1Byte.length);
                    if (image1 != null) {
                        user.setImage(image1);
                    }
                }
                user.setType(cursor.getString(7));
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return user;
    }

    public UserStorageData readUserByEmail(String email) {
        UserStorageData user = null;
        try (Cursor cursor = db
                .query(TABLE_USERS_NAME, TABLE_USERS_COLUMNS, USERS_COLUMN_EMAIL + " = ?",
                        new String[]{String.valueOf(email)},
                        null, null,
                        null, null)) {
            //get reference of the itemDB database
            // if results !=null, parse the first one
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                user = new UserStorageData();
                user.setUID(cursor.getInt(0));
                user.setUserName(cursor.getString(1));
                user.setEmail(cursor.getString(2));
                user.setGen(cursor.getString(3));
                user.setBirthday(cursor.getString(4));
                user.setPassword(cursor.getString(5));
                //images
                byte[] img1Byte = cursor.getBlob(6);
                if (img1Byte != null && img1Byte.length > 0) {
                    Bitmap image1 = BitmapFactory.decodeByteArray(img1Byte, 0, img1Byte.length);
                    if (image1 != null) {
                        user.setImage(image1);
                    }
                }
                user.setType(cursor.getString(7));
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return user;
    }

    public List<UserStorageData> getAllUsers() {
        List<UserStorageData> result = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_USERS_NAME, TABLE_USERS_COLUMNS, null, null,
                    null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                UserStorageData user = cursorToUser(cursor);
                result.add(user);
                cursor.moveToNext();
            }
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            {
                //make sure to close the cursor
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        return result;
    }

    private UserStorageData cursorToUser(Cursor cursor) {
        UserStorageData result = new UserStorageData();
        try {
            //result.setId(Integer.parseInt(cursor.getString(0)));
            result.setUserName(cursor.getString(1));
            result.setUID(cursor.getInt(0));
            result.setEmail(cursor.getString(2));
            result.setGen(cursor.getString(3));
            result.setBirthday(cursor.getString(4));
            result.setPassword(cursor.getString(5));

            byte[] img1Byte = cursor.getBlob(6);
            if (img1Byte != null && img1Byte.length > 0) {
                Bitmap image1 = BitmapFactory.decodeByteArray(img1Byte, 0, img1Byte.length);
                if (image1 != null) {
                    result.setImage(image1);
                }
            }
            result.setType(cursor.getString(7));
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return result;
    }

    public int updateUser(UserStorageData user) {
        int cnt = 0;
        try {
            //make values to be inserted
            ContentValues values = new ContentValues();
            values.put(USERS_COLUMN_USERNAME, user.getUserName());
            values.put(USERS_COLUMN_EMAIL, user.getEmail());
            values.put(USERS_COLUMN_GENDER, user.getGen());
            values.put(USERS_COLUMN_BIRTHDAY, user.getBirthday());
            values.put(USERS_COLUMN_PASSWORD, user.getPassword());
            Bitmap image = user.getImage();
            if (image != null) {
                byte[] data = getBitmapAsByteArray(image);
                if (data.length > 0) {
                    values.put(USER_COLUMN__IMAGE, data);
                }
            } else {
                values.putNull(USER_COLUMN__IMAGE);
            }
            values.put(USERS_COLUMN_TYPE, user.getType());
            cnt = db.update(TABLE_USERS_NAME, values, USERS_COLUMN_ID + " = ?",
                    new String[]{String.valueOf(user.getUID())});
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return cnt;
    }

    public void deleteUser(UserStorageData user) {

        try {

            // delete item
            db.delete(TABLE_USERS_NAME, USERS_COLUMN_ID + " = ?",
                    new String[]{String.valueOf(user.getUID())});
        } catch (Throwable t) {
            t.printStackTrace();
        }

    }

    public void open() {
        try {
            db = getWritableDatabase();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void close() {
        try {
            db.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


}
