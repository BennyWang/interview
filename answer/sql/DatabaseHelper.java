package test.interview.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by wanghaitao on 2017/12/12.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static final int VERSION = 1;

    private static final String NAME = "main";

    public DatabaseHelper(Context context) {
        super(context, NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // create table
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //upgrade table
    }



}
