package dataBase;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import Model.Host;
import Model.Professional;
import Model.UserStorageData;
import Model.donations;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "YourDatabaseName";
    private static final int DATABASE_VERSION = 9;

    // User table
    private static final String USER_TABLE_NAME = "user";
    private static final String USER_COLUMN_ID = "UID";
    private static final String USER_COLUMN_NAME = "userName";
    private static final String USER_COLUMN_GEN = "gen";
    private static final String USER_COLUMN_IMAGE = "image";
    private static final String USER_COLUMN_TYPE = "type";
    private static final String USER_COLUMN_BIRTHDATE = "birthdate";
    private static final String USER_COLUMN_EMAIL = "email";

    // Donations table
    private static final String DONATIONS_TABLE_NAME = "donations";
    private static final String DONATIONS_COLUMN_ADDRESS = "adress";
    private static final String DONATIONS_COLUMN_CATEGORY = "catagory";
    private static final String DONATIONS_COLUMN_DESCRIPTION = "description";
    private static final String DONATIONS_COLUMN_EMAIL = "email";
    private static final String DONATIONS_COLUMN_IMAGE = "img";
    private static final String DONATIONS_COLUMN_NAME = "name";

    // Hosts table
    private static final String HOSTS_TABLE_NAME = "hosts";
    private static final String HOSTS_COLUMN_EMAIL = "hostEmail";
    private static final String HOSTS_COLUMN_DESCRIPTION = "description";
    private static final String HOSTS_COLUMN_ADDRESS = "hostAdress";
    private static final String HOSTS_COLUMN_IMAGE = "hostImg";
    private static final String HOSTS_COLUMN_NAME = "hostName";
    private static final String HOSTS_COLUMN_DATE = "hostingDate";
    private static final String HOSTS_COLUMN_LOC_IMAGE = "hostingLocImg";
    private static final String HOSTS_COLUMN_RESIDENTS = "listOfResidents";
    private static final String HOSTS_COLUMN_PETS = "pets";
    private static final String HOSTS_COLUMN_PRIVATE_ROOM = "privateRoom";
    private static final String HOSTS_COLUMN_SECURE_ENV = "secureEnv";
    private static final String HOSTS_COLUMN_ACCOMMODATION = "accommodation";

    // Professional table
    private static final String PROFESSIONAL_TABLE_NAME = "professional";
    private static final String PROFESSIONAL_COLUMN_ID = "id";
    private static final String PROFESSIONAL_COLUMN_ADDRESS = "address";
    private static final String PROFESSIONAL_COLUMN_CATEGORY = "category";
    private static final String PROFESSIONAL_COLUMN_DESCRIPTION = "description";
    private static final String PROFESSIONAL_COLUMN_EMAIL = "email";
    private static final String PROFESSIONAL_COLUMN_IMAGE_URL = "imageUrl";
    private static final String PROFESSIONAL_COLUMN_PHONE_NUM = "phoneNum";
    private static final String PROFESSIONAL_COLUMN_PREC_AVAILABILITY = "precAvailability";
    private static final String PROFESSIONAL_COLUMN_USER_NAME = "userName";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create user table
        db.execSQL("CREATE TABLE " + USER_TABLE_NAME + "(" +
                USER_COLUMN_ID + " TEXT PRIMARY KEY," +
                USER_COLUMN_NAME + " TEXT," +
                USER_COLUMN_GEN + " TEXT," +
                USER_COLUMN_IMAGE + " TEXT," +
                USER_COLUMN_TYPE + " TEXT," +
                USER_COLUMN_BIRTHDATE + " TEXT," +
                USER_COLUMN_EMAIL + " TEXT" +
                ");");

        // Create donations table
        db.execSQL("CREATE TABLE " + DONATIONS_TABLE_NAME + "(" +
                DONATIONS_COLUMN_ADDRESS + " TEXT," +
                DONATIONS_COLUMN_CATEGORY + " TEXT," +
                DONATIONS_COLUMN_DESCRIPTION + " TEXT," +
                DONATIONS_COLUMN_EMAIL + " TEXT PRIMARY KEY," +
                DONATIONS_COLUMN_IMAGE + " TEXT," +
                DONATIONS_COLUMN_NAME + " TEXT" +
                ");");

        // Create hosts table
        db.execSQL("CREATE TABLE " + HOSTS_TABLE_NAME + "(" +
                HOSTS_COLUMN_EMAIL + " TEXT PRIMARY KEY," +
                HOSTS_COLUMN_DESCRIPTION + " TEXT," +
                HOSTS_COLUMN_ADDRESS + " TEXT," +
                HOSTS_COLUMN_IMAGE + " TEXT," +
                HOSTS_COLUMN_NAME + " TEXT," +
                HOSTS_COLUMN_DATE + " TEXT," +
                HOSTS_COLUMN_LOC_IMAGE + " TEXT," +
                HOSTS_COLUMN_RESIDENTS + " TEXT," +
                HOSTS_COLUMN_PETS + " TEXT," +
                HOSTS_COLUMN_PRIVATE_ROOM + " TEXT," +
                HOSTS_COLUMN_SECURE_ENV + " TEXT," +
                HOSTS_COLUMN_ACCOMMODATION + " TEXT" +
                ");");

        // Create professional table
        db.execSQL("CREATE TABLE " + PROFESSIONAL_TABLE_NAME + "(" +
                PROFESSIONAL_COLUMN_ID + " TEXT PRIMARY KEY," +
                PROFESSIONAL_COLUMN_ADDRESS + " TEXT," +
                PROFESSIONAL_COLUMN_CATEGORY + " TEXT," +
                PROFESSIONAL_COLUMN_DESCRIPTION + " TEXT," +
                PROFESSIONAL_COLUMN_EMAIL + " TEXT," +
                PROFESSIONAL_COLUMN_IMAGE_URL + " TEXT," +
                PROFESSIONAL_COLUMN_PHONE_NUM + " TEXT," +
                PROFESSIONAL_COLUMN_PREC_AVAILABILITY + " TEXT," +
                PROFESSIONAL_COLUMN_USER_NAME + " TEXT" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DONATIONS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + HOSTS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PROFESSIONAL_TABLE_NAME);
        onCreate(db);
    }

    // User table operations
    public boolean insertUserData(UserStorageData user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_COLUMN_ID, user.getUID());
        contentValues.put(USER_COLUMN_NAME, user.getUserName());
        contentValues.put(USER_COLUMN_GEN, user.getGen());
        contentValues.put(USER_COLUMN_IMAGE, user.getImage());
        contentValues.put(USER_COLUMN_TYPE, user.getType());
        contentValues.put(USER_COLUMN_EMAIL,user.getEmail());
        contentValues.put(USER_COLUMN_BIRTHDATE,user.getBirthday());
        long result = db.insert(USER_TABLE_NAME, null, contentValues);
        return result != -1;
    }

    // Donations table operations
    public boolean insertDonationData(donations donation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DONATIONS_COLUMN_ADDRESS, donation.getAdress());
        contentValues.put(DONATIONS_COLUMN_CATEGORY, donation.getCatagory());
        contentValues.put(DONATIONS_COLUMN_DESCRIPTION, donation.getDescription());
        contentValues.put(DONATIONS_COLUMN_EMAIL, donation.getEmail());
        contentValues.put(DONATIONS_COLUMN_IMAGE, donation.getImg());
        contentValues.put(DONATIONS_COLUMN_NAME, donation.getName());
        long result = db.insert(DONATIONS_TABLE_NAME, null, contentValues);
        return result != -1;
    }

    // Hosts table operations
    // Hosts table operations
    public boolean insertHostData(Host host) {
        Gson gson = new Gson();
        String listOfResidentsJson = gson.toJson(host.getListOfResidents());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(HOSTS_COLUMN_EMAIL, host.getHostEmail());
        contentValues.put(HOSTS_COLUMN_DESCRIPTION, host.getDescription());
        contentValues.put(HOSTS_COLUMN_ADDRESS, host.getHostAddress());
        contentValues.put(HOSTS_COLUMN_IMAGE, host.getHostImg());
        contentValues.put(HOSTS_COLUMN_NAME, host.getHostName());
        contentValues.put(HOSTS_COLUMN_DATE, host.getHostingDate());
        contentValues.put(HOSTS_COLUMN_LOC_IMAGE, host.getHostingLocImg());
        contentValues.put(HOSTS_COLUMN_RESIDENTS, listOfResidentsJson); // Assuming this is a serialized list
        contentValues.put(HOSTS_COLUMN_PETS, host.isPetsString());
        contentValues.put(HOSTS_COLUMN_PRIVATE_ROOM, host.isPrivateRoom());
        contentValues.put(HOSTS_COLUMN_SECURE_ENV, host.isSecureEnv());
        contentValues.put(HOSTS_COLUMN_ACCOMMODATION, host.isAccommodation());
        long result = db.insert(HOSTS_TABLE_NAME, null, contentValues);
        return result != -1;
    }

    // Professional table operations
    public boolean insertProfessionalData(Professional professional) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PROFESSIONAL_COLUMN_ID, "");
        contentValues.put(PROFESSIONAL_COLUMN_ADDRESS, professional.getAddress());
        contentValues.put(PROFESSIONAL_COLUMN_CATEGORY, professional.getCategory());
        contentValues.put(PROFESSIONAL_COLUMN_DESCRIPTION, professional.getDescription());
        contentValues.put(PROFESSIONAL_COLUMN_EMAIL, professional.getEmail());
        contentValues.put(PROFESSIONAL_COLUMN_IMAGE_URL, professional.getImageUrl());
        contentValues.put(PROFESSIONAL_COLUMN_PHONE_NUM, professional.getPhoneNum());
        contentValues.put(PROFESSIONAL_COLUMN_PREC_AVAILABILITY, professional.getPrecAvailability());
        contentValues.put(PROFESSIONAL_COLUMN_USER_NAME, professional.getUserName());
        long result = db.insert(PROFESSIONAL_TABLE_NAME, null, contentValues);
        return result != -1;
    }



    public void compareAndUpdate()
    {

    }
    @SuppressLint("Range")
    public List<UserStorageData> getAllUsers() {
        List<UserStorageData> users = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + USER_TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                UserStorageData user = new UserStorageData();
                user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(USER_COLUMN_EMAIL)));
                user.setUID(cursor.getString(cursor.getColumnIndexOrThrow(USER_COLUMN_ID)));
                user.setUserName(cursor.getString(cursor.getColumnIndexOrThrow(USER_COLUMN_NAME)));
                user.setGen(cursor.getString(cursor.getColumnIndexOrThrow(USER_COLUMN_GEN)));
                user.setImage(cursor.getString(cursor.getColumnIndexOrThrow(USER_COLUMN_IMAGE)));
                user.setType(cursor.getString(cursor.getColumnIndexOrThrow(USER_COLUMN_TYPE)));
                user.setBirthday(cursor.getString(cursor.getColumnIndexOrThrow(USER_COLUMN_BIRTHDATE)));
                users.add(user);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return users;
    }
    public List<donations> getAllDonations() {
        List<donations> donationsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DONATIONS_TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                donations donation = new donations();
                donation.setAdress(cursor.getString(cursor.getColumnIndexOrThrow(DONATIONS_COLUMN_ADDRESS)));
                donation.setCatagory(cursor.getString(cursor.getColumnIndexOrThrow(DONATIONS_COLUMN_CATEGORY)));
                donation.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DONATIONS_COLUMN_DESCRIPTION)));
                donation.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(DONATIONS_COLUMN_EMAIL)));
                donation.setImg(cursor.getString(cursor.getColumnIndexOrThrow(DONATIONS_COLUMN_IMAGE)));
                donation.setName(cursor.getString(cursor.getColumnIndexOrThrow(DONATIONS_COLUMN_NAME)));
                donationsList.add(donation);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return donationsList;
    }
    public List<Professional> getAllProfessionals() {
        List<Professional> professionals = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + PROFESSIONAL_TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                Professional professional = new Professional();
                professional.setUID(cursor.getString(cursor.getColumnIndexOrThrow(PROFESSIONAL_COLUMN_ID)));
                professional.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(PROFESSIONAL_COLUMN_ADDRESS)));
                professional.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(PROFESSIONAL_COLUMN_CATEGORY)));
                professional.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(PROFESSIONAL_COLUMN_DESCRIPTION)));
                professional.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(PROFESSIONAL_COLUMN_EMAIL)));
                professional.setImageUrl(cursor.getString(cursor.getColumnIndexOrThrow(PROFESSIONAL_COLUMN_IMAGE_URL)));
                professional.setPhoneNum(cursor.getString(cursor.getColumnIndexOrThrow(PROFESSIONAL_COLUMN_PHONE_NUM)));
                professional.setPrecAvailability(cursor.getString(cursor.getColumnIndexOrThrow(PROFESSIONAL_COLUMN_PREC_AVAILABILITY)));
                professional.setUserName(cursor.getString(cursor.getColumnIndexOrThrow(PROFESSIONAL_COLUMN_USER_NAME)));
                professionals.add(professional);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return professionals;
    }
    public List<Host> getAllHosts() {
        List<Host> hosts = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + HOSTS_TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                Gson gson = new Gson();
                Type listType = new TypeToken<List<UserStorageData>>() {}.getType();
                List<UserStorageData> listOfResidentsJson = gson.fromJson(cursor.getString(cursor.getColumnIndexOrThrow(HOSTS_COLUMN_RESIDENTS)), listType);
                Host host = new Host();
                host.setHostEmail(cursor.getString(cursor.getColumnIndexOrThrow(HOSTS_COLUMN_EMAIL)));
                host.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(HOSTS_COLUMN_DESCRIPTION)));
                host.setHostAddress(cursor.getString(cursor.getColumnIndexOrThrow(HOSTS_COLUMN_ADDRESS)));
                host.setHostImg(cursor.getString(cursor.getColumnIndexOrThrow(HOSTS_COLUMN_IMAGE)));
                host.setHostName(cursor.getString(cursor.getColumnIndexOrThrow(HOSTS_COLUMN_NAME)));
                host.setHostingDate(cursor.getString(cursor.getColumnIndexOrThrow(HOSTS_COLUMN_DATE)));
                host.setHostingLocImg(cursor.getString(cursor.getColumnIndexOrThrow(HOSTS_COLUMN_LOC_IMAGE)));
                host.setListOfResidents(listOfResidentsJson);
                host.setPetsString(cursor.getString(cursor.getColumnIndexOrThrow(HOSTS_COLUMN_PETS)));
                host.setPrivateRoomString(cursor.getString(cursor.getColumnIndexOrThrow(HOSTS_COLUMN_PRIVATE_ROOM)));
                host.setSecureEnvString(cursor.getString(cursor.getColumnIndexOrThrow(HOSTS_COLUMN_SECURE_ENV)));
                host.setAccommodationString(cursor.getString(cursor.getColumnIndexOrThrow(HOSTS_COLUMN_ACCOMMODATION)));
                hosts.add(host);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return hosts;
    }


    public void compareAndUpdateUsers(List<UserStorageData> firebaseUsers) {
        List<UserStorageData> sqliteUsers = getAllUsers();

        for (UserStorageData firebaseUser : firebaseUsers) {
            boolean userExists = false;
            for (UserStorageData sqliteUser : sqliteUsers) {
                if (firebaseUser.getUID().equals(sqliteUser.getUID())) {
                    userExists = true;
                    if (!firebaseUser.getEmail().equals(sqliteUser.getEmail()) && !firebaseUser.getUserName().equals(sqliteUser.getUserName())
                    && !firebaseUser.getType().equals(sqliteUser.getType())) {
                        updateUser(firebaseUser); // You'll need to implement this method to update the user in SQLite
                    }
                    break;
                }
            }

            if (!userExists) {
                insertUserData(firebaseUser);
            }
        }
    }

    public boolean updateUser(UserStorageData user) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Delete the old user
        int deletedRows = db.delete(USER_TABLE_NAME, USER_COLUMN_ID + " = ?", new String[]{user.getUID()});

        // If the old user was deleted successfully, insert the new user
        if (deletedRows > 0) {
            return insertUserData(user);
        }

        return false;
    }
    public void compareAndUpdateHosts(List<Host> firebaseHosts) {
        List<Host> sqliteHosts = getAllHosts();

        for (Host firebaseHost : firebaseHosts) {
            boolean hostExists = false;
            for (Host sqliteHost : sqliteHosts) {
                if (firebaseHost.getHostEmail().equals(sqliteHost.getHostEmail())) {
                    hostExists = true;
                    // Compare other fields here
                    if (!firebaseHost.getDescription().equals(sqliteHost.getDescription()) || !firebaseHost.getHostName().equals(sqliteHost.getHostName()) /* other conditions */) {
                        updateHost(firebaseHost);
                    }
                    break;
                }
            }

            if (!hostExists) {
                insertHostData(firebaseHost);
            }
        }
    }

    public boolean updateHost(Host host) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Delete the old host
        int deletedRows = db.delete(HOSTS_TABLE_NAME, HOSTS_COLUMN_EMAIL + " = ?", new String[]{host.getHostEmail()});

        // If the old host was deleted successfully, insert the new host
        if (deletedRows > 0) {
            return insertHostData(host);
        }

        return false;
    }
    public void compareAndUpdateProfessionals(List<Professional> firebaseProfessionals) {
        List<Professional> sqliteProfessionals = getAllProfessionals();

        for (Professional firebaseProfessional : firebaseProfessionals) {
            boolean professionalExists = false;
            for (Professional sqliteProfessional : sqliteProfessionals) {
                if (firebaseProfessional.getUID().equals(sqliteProfessional.getUID())) {
                    professionalExists = true;
                    // Compare other fields here
                    if (!firebaseProfessional.getAddress().equals(sqliteProfessional.getAddress()) /* other conditions */) {
                        updateProfessional(firebaseProfessional);
                    }
                    break;
                }
            }

            if (!professionalExists) {
                insertProfessionalData(firebaseProfessional);
            }
        }
    }

    public boolean updateProfessional(Professional professional) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Delete the old professional
        int deletedRows = db.delete(PROFESSIONAL_TABLE_NAME, PROFESSIONAL_COLUMN_ID + " = ?", new String[]{professional.getUID()});

        // If the old professional was deleted successfully, insert the new professional
        if (deletedRows > 0) {
            return insertProfessionalData(professional);
        }

        return false;
    }

    public void compareAndUpdateDonations(List<donations> firebaseDonations) {
        List<donations> sqliteDonations = getAllDonations();

        for (donations firebaseDonation : firebaseDonations) {
            boolean donationExists = false;
            for (donations sqliteDonation : sqliteDonations) {
                if (firebaseDonation.getEmail() == sqliteDonation.getEmail()) {
                    donationExists = true;
                    // Compare other fields here
                    if (!firebaseDonation.getName().equals(sqliteDonation.getName()) /* other conditions */) {
                        updateDonation(firebaseDonation);
                    }
                    break;
                }
            }

            if (!donationExists) {
                insertDonationData(firebaseDonation);
            }
        }
    }

    public boolean updateDonation(donations donation) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Delete the old donation
        int deletedRows = db.delete(DONATIONS_TABLE_NAME, DONATIONS_COLUMN_NAME + " = ?", new String[]{String.valueOf(donation.getName())});

        // If the old donation was deleted successfully, insert the new donation
        if (deletedRows > 0) {
           return insertDonationData(donation);
        }

        return false;
    }
    public UserStorageData getUserByUID(String UID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(USER_TABLE_NAME, null, USER_COLUMN_ID + " = ?", new String[]{UID}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            UserStorageData user = new UserStorageData();
            user.setUID(cursor.getString(cursor.getColumnIndexOrThrow(USER_COLUMN_ID)));
            user.setUserName(cursor.getString(cursor.getColumnIndexOrThrow(USER_COLUMN_NAME)));
            user.setGen(cursor.getString(cursor.getColumnIndexOrThrow(USER_COLUMN_GEN)));
            user.setImage(cursor.getString(cursor.getColumnIndexOrThrow(USER_COLUMN_IMAGE)));
            user.setType(cursor.getString(cursor.getColumnIndexOrThrow(USER_COLUMN_TYPE)));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(USER_COLUMN_EMAIL));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(USER_COLUMN_EMAIL)));
            user.setBirthday(cursor.getString(cursor.getColumnIndexOrThrow(USER_COLUMN_BIRTHDATE)));// Assuming you have a setEmail method
            cursor.close();
            return user;
        }

        return null; // Return null if no user found with the given UID
    }
    public UserStorageData getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(USER_TABLE_NAME, null, USER_COLUMN_EMAIL + " = ?", new String[]{email}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            UserStorageData user = new UserStorageData();
            user.setUID(cursor.getString(cursor.getColumnIndexOrThrow(USER_COLUMN_ID)));
            user.setUserName(cursor.getString(cursor.getColumnIndexOrThrow(USER_COLUMN_NAME)));
            user.setGen(cursor.getString(cursor.getColumnIndexOrThrow(USER_COLUMN_GEN)));
            user.setImage(cursor.getString(cursor.getColumnIndexOrThrow(USER_COLUMN_IMAGE)));
            user.setType(cursor.getString(cursor.getColumnIndexOrThrow(USER_COLUMN_TYPE)));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(USER_COLUMN_EMAIL)));
            user.setBirthday(cursor.getString(cursor.getColumnIndexOrThrow(USER_COLUMN_BIRTHDATE)));// Assuming you have a setEmail method
            cursor.close();
            return user;
        }

        return null; // Return null if no user found with the given UID
    }











    // ... Add other methods for CRUD operations or specific queries as needed
}
