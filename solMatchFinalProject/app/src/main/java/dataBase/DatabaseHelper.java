package dataBase;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

import com.example.solmatchfinalproject.ReviewProffessionalActivity;
import com.example.solmatchfinalproject.notifications;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import Model.Host;
import Model.Professional;
import Model.Review;
import Model.UserStorageData;
import Model.donations;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "DB11";
    private static final int DATABASE_VERSION = 28;

    // notification table

    private static final String PROFESSIONALREVIEW_TABLE_NAME = "professionalReview";

    private static final String PROFESSIONALREVIEW_COLUMN_ID = "UID";

    private static final String PROFESSIONALREVIEW_COLUMN_REVIEWERNAME = "nameOfReviwer";

    private static final String PROFESSIONALREVIEW_COLUMN_review = "review";

    private static final String PROFESSIONALREVIEW_COLUMN_RATING = "rate";
    private static final String NOTIFICATION_TABLE_NAME = "notification";
    private static final String NOTIFICATION_COLUMN_ID = "UID";
    private static final String NOTIFICATION_COLUMN_AUTOINCREMENT = "ID";

    private static final String NOTIFICATION_COLUMN_message = "message";

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

    private static final String DONATIONS_COLUMN_ID = "id";
    private static final String DONATIONS_COLUMN_ADDRESS = "adress";
    private static final String DONATIONS_COLUMN_CATEGORY = "catagory";
    private static final String DONATIONS_COLUMN_DESCRIPTION = "description";
    private static final String DONATIONS_COLUMN_EMAIL = "email";
    private static final String DONATIONS_COLUMN_IMAGE = "img";
    private static final String DONATIONS_COLUMN_NAME = "name";

    // Hosts table
    private static final String HOSTS_TABLE_NAME = "hosts";
    private static final String HOSTS_COLUMN_ID = "id";
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

        db.execSQL("CREATE TABLE " + NOTIFICATION_TABLE_NAME + " (" +
                NOTIFICATION_COLUMN_AUTOINCREMENT + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NOTIFICATION_COLUMN_ID + " TEXT, " +
                NOTIFICATION_COLUMN_message + " TEXT, " +
                "UNIQUE (" + NOTIFICATION_COLUMN_ID + ", " + NOTIFICATION_COLUMN_AUTOINCREMENT + "))");


        db.execSQL("CREATE TABLE " + PROFESSIONALREVIEW_TABLE_NAME + " (" +
                PROFESSIONALREVIEW_COLUMN_ID + " TEXT, " +
                PROFESSIONALREVIEW_COLUMN_REVIEWERNAME + " TEXT, " +
                PROFESSIONALREVIEW_COLUMN_review + " TEXT, " +
                PROFESSIONALREVIEW_COLUMN_RATING + " REAL, " +
                "PRIMARY KEY (" + PROFESSIONALREVIEW_COLUMN_ID + ", " + PROFESSIONALREVIEW_COLUMN_REVIEWERNAME + "))");




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
                DONATIONS_COLUMN_ID + " TEXT PRIMARY KEY," +
                DONATIONS_COLUMN_ADDRESS + " TEXT," +
                DONATIONS_COLUMN_CATEGORY + " TEXT," +
                DONATIONS_COLUMN_DESCRIPTION + " TEXT," +
                DONATIONS_COLUMN_EMAIL + " TEXT," +
                DONATIONS_COLUMN_IMAGE + " TEXT," +
                DONATIONS_COLUMN_NAME + " TEXT" +
                ");");

        // Create hosts table
        db.execSQL("CREATE TABLE " + HOSTS_TABLE_NAME + "(" +
                HOSTS_COLUMN_ID + " TEXT PRIMARY KEY," +
                HOSTS_COLUMN_EMAIL + " TEXT," +
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
        db.execSQL("DROP TABLE IF EXISTS " + NOTIFICATION_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PROFESSIONALREVIEW_TABLE_NAME);
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

    public boolean insertProfessionalReviewData(Review review) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(PROFESSIONALREVIEW_COLUMN_ID, review.getUID());
        contentValues.put(PROFESSIONALREVIEW_COLUMN_REVIEWERNAME, review.getNameReview());
        contentValues.put(PROFESSIONALREVIEW_COLUMN_review, review.getComments());
        contentValues.put(PROFESSIONALREVIEW_COLUMN_RATING, review.getRate());

        long result = db.insert(PROFESSIONALREVIEW_TABLE_NAME, null, contentValues);
        return result != -1;
    }


    public boolean insertNotificationData(notifications notification) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTIFICATION_COLUMN_ID, notification.getId());
        contentValues.put(NOTIFICATION_COLUMN_message, notification.getMessage());
        long result = db.insert(NOTIFICATION_TABLE_NAME, null, contentValues);
        return result != -1;
    }

    // Donations table operations
    public boolean insertDonationData(donations donation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DONATIONS_COLUMN_ID, donation.getUid());
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
        contentValues.put(HOSTS_COLUMN_ID, host.getUid());
        contentValues.put(HOSTS_COLUMN_EMAIL, host.getHostEmail());
        contentValues.put(HOSTS_COLUMN_DESCRIPTION, host.getDescription());
        contentValues.put(HOSTS_COLUMN_ADDRESS, host.getHostAddress());
        contentValues.put(HOSTS_COLUMN_IMAGE, host.getHostImg());
        contentValues.put(HOSTS_COLUMN_NAME, host.getHostName());
        contentValues.put(HOSTS_COLUMN_DATE, host.getHostingDate());
        contentValues.put(HOSTS_COLUMN_LOC_IMAGE, host.getHostingLocImg());
        contentValues.put(HOSTS_COLUMN_RESIDENTS, listOfResidentsJson); // Assuming this is a serialized list
        contentValues.put(HOSTS_COLUMN_PETS, host.getPets());
        contentValues.put(HOSTS_COLUMN_PRIVATE_ROOM, host.getPrivateRoom());
        contentValues.put(HOSTS_COLUMN_SECURE_ENV, host.getSecureEnv());
        contentValues.put(HOSTS_COLUMN_ACCOMMODATION, host.getAccommodation());
        long result = db.insert(HOSTS_TABLE_NAME, null, contentValues);
        return result != -1;
    }

    // Professional table operations
    public boolean insertProfessionalData(Professional professional) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PROFESSIONAL_COLUMN_ID, professional.getUID());
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
    public List<notifications> getAllNotifications() {
        List<notifications> notificationsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + NOTIFICATION_TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndexOrThrow(NOTIFICATION_COLUMN_ID));
                String message = cursor.getString(cursor.getColumnIndexOrThrow(NOTIFICATION_COLUMN_message));
                notifications notificationItem = new notifications(id, message);
                notificationsList.add(notificationItem);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return notificationsList;
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
                donation.setUid(cursor.getString(cursor.getColumnIndexOrThrow(DONATIONS_COLUMN_ID)));
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
               ArrayList<Review> reviews = (ArrayList<Review>) getProfessionalReviewsByUserID(professional.getUID());
               if(!reviews.isEmpty())
               {
                   professional.setReviews(reviews);
               }
               else {
                   professional.setReviews(new ArrayList<>());
               }
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
                host.setUid(cursor.getString(cursor.getColumnIndexOrThrow(HOSTS_COLUMN_ID)));
                host.setHostEmail(cursor.getString(cursor.getColumnIndexOrThrow(HOSTS_COLUMN_EMAIL)));
                host.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(HOSTS_COLUMN_DESCRIPTION)));
                host.setHostAddress(cursor.getString(cursor.getColumnIndexOrThrow(HOSTS_COLUMN_ADDRESS)));
                host.setHostImg(cursor.getString(cursor.getColumnIndexOrThrow(HOSTS_COLUMN_IMAGE)));
                host.setHostName(cursor.getString(cursor.getColumnIndexOrThrow(HOSTS_COLUMN_NAME)));
                host.setHostingDate(cursor.getString(cursor.getColumnIndexOrThrow(HOSTS_COLUMN_DATE)));
                host.setHostingLocImg(cursor.getString(cursor.getColumnIndexOrThrow(HOSTS_COLUMN_LOC_IMAGE)));
                host.setListOfResidents(listOfResidentsJson);
                host.setPets(cursor.getString(cursor.getColumnIndexOrThrow(HOSTS_COLUMN_PETS)));
                host.setPrivateRoom(cursor.getString(cursor.getColumnIndexOrThrow(HOSTS_COLUMN_PRIVATE_ROOM)));
                host.setSecureEnv(cursor.getString(cursor.getColumnIndexOrThrow(HOSTS_COLUMN_SECURE_ENV)));
                host.setAccommodation(cursor.getString(cursor.getColumnIndexOrThrow(HOSTS_COLUMN_ACCOMMODATION)));
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
    public void compareAndUpdateNotifications(List<notifications> firebaseNotifications) {
        List<notifications> sqliteNotifications = getAllNotifications();

        for (notifications firebaseNotification : firebaseNotifications) {
            boolean notificationExists = false;
            for (notifications sqliteNotification : sqliteNotifications) {
                if (firebaseNotification.getId().equals(sqliteNotification.getId()) && firebaseNotification.getMessage().equals(sqliteNotification.getMessage())) {
                    notificationExists = true;
                    // Compare other fields here
                    break;
                }
            }

            if (!notificationExists) {
                insertNotificationData(firebaseNotification);
            }
        }
    }

    public boolean updateNotification(notifications notification) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTIFICATION_COLUMN_ID, notification.getId());
        contentValues.put(NOTIFICATION_COLUMN_message, notification.getMessage());

        // Update the notification based on both ID and message
        int updatedRows = db.update(NOTIFICATION_TABLE_NAME, contentValues,
                NOTIFICATION_COLUMN_ID + " = ? AND " + NOTIFICATION_COLUMN_message + " = ?",
                new String[]{notification.getId(), notification.getMessage()});

        return updatedRows > 0;
    }



    public void compareAndUpdateHosts(List<Host> firebaseHosts) {
        List<Host> sqliteHosts = getAllHosts();

        for (Host firebaseHost : firebaseHosts) {
            boolean hostExists = false;
            for (Host sqliteHost : sqliteHosts) {
                if (firebaseHost.getUid().equals(sqliteHost.getUid())) {
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
        if(firebaseProfessionals == null)
        {
            return;
        }
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
                if (firebaseDonation.getUid().equals(sqliteDonation.getUid())) {
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
    // proffesional review
    public List<Review> getProfessionalReviewsByUserID(String UID) {
        List<Review> reviewList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query the database for all reviews related to the given UID
        Cursor cursor = db.query(PROFESSIONALREVIEW_TABLE_NAME, null, PROFESSIONALREVIEW_COLUMN_ID + " = ?", new String[]{UID}, null, null, null);

        // Iterate through the results and add them to the list
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Review review = new Review(
                        cursor.getString(cursor.getColumnIndexOrThrow(PROFESSIONALREVIEW_COLUMN_REVIEWERNAME)),
                        cursor.getFloat(cursor.getColumnIndexOrThrow(PROFESSIONALREVIEW_COLUMN_RATING)),
                        cursor.getString(cursor.getColumnIndexOrThrow(PROFESSIONALREVIEW_COLUMN_review)),
                        cursor.getString(cursor.getColumnIndexOrThrow(PROFESSIONALREVIEW_COLUMN_ID))
                );
                reviewList.add(review);
            }
            cursor.close();
        }

        return reviewList;
    }


    public List<notifications> getNotificationsByUserID(String UID) {
        List<notifications> notificationList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query the database for all notifications related to the given UID
        Cursor cursor = db.query(NOTIFICATION_TABLE_NAME, null, NOTIFICATION_COLUMN_ID + " = ?", new String[]{UID}, null, null, null);

        // Iterate through the results and add them to the list
        if (cursor != null) {
            while (cursor.moveToNext()) {
                notifications notification = new notifications(
                        cursor.getString(cursor.getColumnIndexOrThrow(NOTIFICATION_COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(NOTIFICATION_COLUMN_message))
                );
                notificationList.add(notification);
            }
            cursor.close();
        }

        return notificationList;
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



    // Method to remove a host by its ID
    public boolean removeHostById(String hostId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int deletedRows = db.delete(HOSTS_TABLE_NAME, HOSTS_COLUMN_ID + " = ?", new String[]{hostId});
        if (deletedRows > 0)
        {
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            firestore.collection("Host").document(hostId).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                }

            });
            return true;
        }
        return false;
    }
    public Professional getProfessionalByUID(String UID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(PROFESSIONAL_TABLE_NAME, null, PROFESSIONAL_COLUMN_ID + " = ?", new String[]{UID}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Professional professional = new Professional();
            professional.setUID(cursor.getString(cursor.getColumnIndexOrThrow(PROFESSIONAL_COLUMN_ID)));
            professional.setUserName(cursor.getString(cursor.getColumnIndexOrThrow(PROFESSIONAL_COLUMN_USER_NAME)));
            professional.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(PROFESSIONAL_COLUMN_EMAIL)));
            professional.setImageUrl(cursor.getString(cursor.getColumnIndexOrThrow(PROFESSIONAL_COLUMN_IMAGE_URL)));
            professional.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(PROFESSIONAL_COLUMN_CATEGORY)));
            professional.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(PROFESSIONAL_COLUMN_ADDRESS)));
            professional.setPhoneNum(cursor.getString(cursor.getColumnIndexOrThrow(PROFESSIONAL_COLUMN_PHONE_NUM)));
            professional.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(PROFESSIONAL_COLUMN_DESCRIPTION)));
            professional.setPrecAvailability(cursor.getString(cursor.getColumnIndexOrThrow(PROFESSIONAL_COLUMN_PREC_AVAILABILITY)));
            professional.setReviews(getProfessionalReviewsByUserID(professional.getUID()));
            // Note: The reviews list is not populated here as it's not part of the table structure you provided.
            cursor.close();
            return professional;
        }

        return null; // Return null if no professional found with the given UID
    }


    public boolean removeNotification(notifications notificationToDelete) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Delete the notification based on both ID and message
        int deletedRows = db.delete(NOTIFICATION_TABLE_NAME,
                NOTIFICATION_COLUMN_ID + " = ? AND " + NOTIFICATION_COLUMN_message + " = ?",
                new String[]{notificationToDelete.getId(), notificationToDelete.getMessage()});

        if (deletedRows > 0) {
            // If you want to also delete from Firestore, you can do so here.
            // Note: You might need to adjust how you identify the document in Firestore if it's based on both ID and message.
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            firestore.collection("Notifications").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot snapshot: queryDocumentSnapshots) {
                        notifications notification = snapshot.toObject(com.example.solmatchfinalproject.notifications.class);
                        if(notificationToDelete.getMessage().equals(notification.getMessage()) && notificationToDelete.getId().equals(notification.getId())) {
                            snapshot.getReference().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Document successfully deleted
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle the failure
                                }
                            });
                            break;  // Exit the loop once you've found and attempted to delete the matching document
                        }
                    }
                }
            });

            return true;
        }
        return false;
    }

    // Method to remove a donation by its ID
    public boolean removeDonationById(String donationId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int deletedRows = db.delete(DONATIONS_TABLE_NAME, DONATIONS_COLUMN_ID + " = ?", new String[]{donationId});
        if (deletedRows > 0)
        {
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            firestore.collection("Donations").document(donationId).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                }

            });
            return true;
        }
        return false;
    }








    // ... Add other methods for CRUD operations or specific queries as needed
}
