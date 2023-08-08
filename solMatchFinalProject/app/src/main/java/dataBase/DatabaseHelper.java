package dataBase;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "YourDatabaseName";
    private static final int DATABASE_VERSION = 1;

    // User table
    private static final String USER_TABLE_NAME = "user";
    private static final String USER_COLUMN_ID = "id";
    private static final String USER_COLUMN_NAME = "name";
    private static final String USER_COLUMN_GEN = "gen";
    private static final String USER_COLUMN_IMAGE = "image";
    private static final String USER_COLUMN_TYPE = "type";

    // Donations table
    private static final String DONATIONS_TABLE_NAME = "donations";
    private static final String DONATIONS_COLUMN_ID = "id";
    private static final String DONATIONS_COLUMN_ADDRESS = "address";
    private static final String DONATIONS_COLUMN_CATEGORY = "category";
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
                USER_COLUMN_TYPE + " TEXT" +
                ");");

        // Create donations table
        db.execSQL("CREATE TABLE " + DONATIONS_TABLE_NAME + "(" +
                DONATIONS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DONATIONS_COLUMN_ADDRESS + " TEXT," +
                DONATIONS_COLUMN_CATEGORY + " TEXT," +
                DONATIONS_COLUMN_DESCRIPTION + " TEXT," +
                DONATIONS_COLUMN_EMAIL + " TEXT," +
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
    public boolean insertUserData(String id, String name, String gen, String image, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_COLUMN_ID, id);
        contentValues.put(USER_COLUMN_NAME, name);
        contentValues.put(USER_COLUMN_GEN, gen);
        contentValues.put(USER_COLUMN_IMAGE, image);
        contentValues.put(USER_COLUMN_TYPE, type);
        long result = db.insert(USER_TABLE_NAME, null, contentValues);
        return result != -1;
    }

    // Donations table operations
    public boolean insertDonationData(String address, String category, String description, String email, String img, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DONATIONS_COLUMN_ADDRESS, address);
        contentValues.put(DONATIONS_COLUMN_CATEGORY, category);
        contentValues.put(DONATIONS_COLUMN_DESCRIPTION, description);
        contentValues.put(DONATIONS_COLUMN_EMAIL, email);
        contentValues.put(DONATIONS_COLUMN_IMAGE, img);
        contentValues.put(DONATIONS_COLUMN_NAME, name);
        long result = db.insert(DONATIONS_TABLE_NAME, null, contentValues);
        return result != -1;
    }

    // Hosts table operations
    public boolean insertHostData(String hostEmail, String description, String hostAddress, String hostImg,
                                  String hostName, String hostingDate, String hostingLocImg, String listOfResidents,
                                  String pets, String privateRoom, String secureEnv, String accommodation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(HOSTS_COLUMN_EMAIL, hostEmail);
        contentValues.put(HOSTS_COLUMN_DESCRIPTION, description);
        contentValues.put(HOSTS_COLUMN_ADDRESS, hostAddress);
        contentValues.put(HOSTS_COLUMN_IMAGE, hostImg);
        contentValues.put(HOSTS_COLUMN_NAME, hostName);
        contentValues.put(HOSTS_COLUMN_DATE, hostingDate);
        contentValues.put(HOSTS_COLUMN_LOC_IMAGE, hostingLocImg);
        contentValues.put(HOSTS_COLUMN_RESIDENTS, listOfResidents);
        contentValues.put(HOSTS_COLUMN_PETS, pets);
        contentValues.put(HOSTS_COLUMN_PRIVATE_ROOM, privateRoom);
        contentValues.put(HOSTS_COLUMN_SECURE_ENV, secureEnv);
        contentValues.put(HOSTS_COLUMN_ACCOMMODATION, accommodation);
        long result = db.insert(HOSTS_TABLE_NAME, null, contentValues);
        return result != -1;
    }

    // Professional table operations
    public boolean insertProfessionalData(String id, String address, String category, String description,
                                          String email, String imageUrl, String phoneNum, String precAvailability,
                                          String userName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PROFESSIONAL_COLUMN_ID, id);
        contentValues.put(PROFESSIONAL_COLUMN_ADDRESS, address);
        contentValues.put(PROFESSIONAL_COLUMN_CATEGORY, category);
        contentValues.put(PROFESSIONAL_COLUMN_DESCRIPTION, description);
        contentValues.put(PROFESSIONAL_COLUMN_EMAIL, email);
        contentValues.put(PROFESSIONAL_COLUMN_IMAGE_URL, imageUrl);
        contentValues.put(PROFESSIONAL_COLUMN_PHONE_NUM, phoneNum);
        contentValues.put(PROFESSIONAL_COLUMN_PREC_AVAILABILITY, precAvailability);
        contentValues.put(PROFESSIONAL_COLUMN_USER_NAME, userName);
        long result = db.insert(PROFESSIONAL_TABLE_NAME, null, contentValues);
        return result != -1;
    }

    // ... Add other methods for CRUD operations or specific queries as needed
}
