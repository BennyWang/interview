package test.interview.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by wanghaitao on 2017/12/12.
 */

public class DatabaseUtil {

    private static SQLiteDatabase DATABASE;

    /**
     * init
     * call in Application.onCreate
     * @param context
     */
    private static void open(Context context){
        if(DATABASE == null){
            DatabaseHelper databaseHelp = new DatabaseHelper(context);
            DATABASE = databaseHelp.getWritableDatabase();
        }

    }

//    public void query(){}
//
//    public void delete(){}
//
//    public void update(){}
//
//    public void add(){}

    public void closeDatabase(){
        DATABASE.close();
        DATABASE = null;
    }

}
