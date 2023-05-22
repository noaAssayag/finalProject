package dataBase;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import Model.UserStorageData;

public class MyInfoManager {
    private static MyInfoManager instance = null;
    private Context context = null;
    private MyInfoDataBase db = null;
    private UserStorageData selectedUser = null;


    public static MyInfoManager getInstance() {
        if (instance == null) {
            instance = new MyInfoManager();
        }
        return instance;
    }

    public static void releaseInstance() {
        if (instance != null) {
            instance.clean();
            instance = null;
        }
    }

    private void clean() {

    }


    public Context getContext() {
        return context;

    }

    public void openDataBase(Context context) {
        this.context = context;
        if (context != null) {
            db = new MyInfoDataBase(context);
            db.open();
        }
    }
    public void closeDataBase() {
        if(db!=null){
            db.close();
        }
    }

    public void createUser(UserStorageData user) {
        if (db != null) {
            db.createUser(user);
        }
    }

    public UserStorageData readUser(int id) {
        UserStorageData result = null;
        if (db != null) {
            result = db.readUser(id);
        }
        return result;
    }


    public List<UserStorageData> getAllUser() {
        List<UserStorageData> result = new ArrayList<UserStorageData>();
        if (db != null) {
            result = db.getAllUsers();
        }
        return result;
    }
    public void updateUser(UserStorageData user) {
        if (db != null && user != null) {
            db.updateUser(user);
        }
    }

    public void deleteUser(UserStorageData user) {
        if (db != null) {
            db.deleteUser(user);
        }
    }
    public UserStorageData getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(UserStorageData selectedUser) {
        this.selectedUser =selectedUser;
    }

    public UserStorageData readUserByEmail(String email) {
        UserStorageData result = null;
        if (db != null) {
            result = db.readUserByEmail(email);
        }
        return result;
    }
}
