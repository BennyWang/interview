package Android.Android;



import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseRepository<T>  {
	public class SqliteHelper extends SQLiteOpenHelper {
	    public SqliteHelper(Context context,String dbname,int version) {
	        super(context, dbname, null, version);
	    }
	    @Override
	    public void onCreate(SQLiteDatabase db) {
	        String sql_user="create table if not exists user("
	                +"username text primary key,"
	                +"userpwd text)";
	        db.execSQL(sql_user);// 创建一个用户表
	    }
	    @Override
	    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	        // TODO Auto-generated method stub
	    }
	    public void deleteTable(SQLiteDatabase db, String name) {
	        String sql_delteTable = "DROP TABLE if exists " + name;
	        db.execSQL(sql_delteTable);
	    }
	}
     
    protected SqliteHelper  dbHelper;  
  
    public BaseRepository(Context context,String dbname,int version) {  
        dbHelper = new SqliteHelper(context,dbname,version);  
    }  
	
    public long insert(String table, String nullColumnHack, ContentValues values) {  
        long ret = 0;  
        SQLiteDatabase database = dbHelper.getWritableDatabase();  
        database.beginTransaction();  
        try {  
            ret = database.insert(table, nullColumnHack, values);  
            database.setTransactionSuccessful();  
        } catch (RuntimeException e) {  
            e.printStackTrace();
        } finally {  
            database.endTransaction();  
        }  
        return ret;  
    }  
  
    public <T> List<T> query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, Integer limit) {  
        List<T> results = new ArrayList<T>();  
        Cursor cursor = null;  
        try {  
            if (limit != null) {  
                cursor = dbHelper.getReadableDatabase().query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit + "");  
            } else {  
                cursor = dbHelper.getReadableDatabase().query(table, columns, selection, selectionArgs, groupBy, having, orderBy);  
            }  
            results = queryResult(cursor);  
        } catch (RuntimeException e) {  
        	e.printStackTrace();
        } finally {  
            if (cursor != null) {  
                cursor.close();  
            }  
        }  
        return results;  
    }  
  
    public <T> List<T> queryResult(Cursor cursor) {  
        throw new RuntimeException("Please overwrite method.");  
    }  
    
    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {  
        int ret = 0;  
        SQLiteDatabase database = dbHelper.getWritableDatabase();  
        database.beginTransaction();  
        try {  
            ret = database.update(table, values, whereClause, whereArgs);  
            database.setTransactionSuccessful();  
        } catch (RuntimeException e) {  
        	e.printStackTrace(); 
        } finally {  
            database.endTransaction();  
        }  
        return ret;  
    }  
    
    public int delete(String table, String whereClause, String[] whereArgs) {  
    	  
        int ret = 0;  
        SQLiteDatabase database = dbHelper.getWritableDatabase();  
        database.beginTransaction();  
        try {  
            ret = database.delete(table, whereClause, whereArgs);  
            database.setTransactionSuccessful();  
        } catch (RuntimeException e) {  
        	e.printStackTrace(); 
        } finally {  
            database.endTransaction();  
        }  
        return ret;  
    }  
        
}
