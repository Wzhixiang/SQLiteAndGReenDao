package com.wzx.sqliteandgreendao;

import android.app.Application;

import com.anye.greendao.gen.DaoMaster;
import com.anye.greendao.gen.DaoMaster.DevOpenHelper;
import com.anye.greendao.gen.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * 描述 TODO
 * Created by 王治湘 on 2017/10/23.
 * version 1.0
 */

public class App extends Application {
    /** A flag to show how easily you can switch from standard SQLite to the encrypted SQLCipher. */
    public static final boolean ENCRYPTED = true;
    private DaoSession daoSession;

    @Override
    public void onCreate(){
        super.onCreate();

        DevOpenHelper helper =
                new DevOpenHelper(this, ENCRYPTED ? "notes-db-encrypted" : "notes-db");
        Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

}
