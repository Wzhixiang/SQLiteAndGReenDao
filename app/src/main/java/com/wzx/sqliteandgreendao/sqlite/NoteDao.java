package com.wzx.sqliteandgreendao.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.wzx.sqliteandgreendao.Note;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 描述 TODO
 * Created by 王治湘 on 2017/10/23.
 * version 1.0
 */

public class NoteDao {
    private static final String TAG = "NoteDao";
    // 列定义
    private final String[] ORDER_COLUMNS = new String[]{DBHelper.ID, DBHelper.TEXT, DBHelper.COMMENT, DBHelper.DATE, DBHelper.TYPE};
    private Context context;
    private DBHelper ordersDBHelper;

    public NoteDao(Context context) {
        this.context = context;
        ordersDBHelper = new DBHelper(context);
    }


    /**
     * 判断表中是否有数据
     */
    public boolean isDataExist() {
        int count = 0;
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = ordersDBHelper.getReadableDatabase();
            // select count(Id) from Orders
            cursor = db.query(DBHelper.TABLE_NAME, new String[]{"COUNT(" + DBHelper.ID + ")"}, null, null, null, null, null);

            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            if (count > 0) return true;
        } catch (Exception e) {

            Log.e(TAG, "", e);

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return false;

    }


    /**
     * 初始化数据
     */
    public void initTable() {
        SQLiteDatabase db = null;

        try {
            db = ordersDBHelper.getWritableDatabase();
            db.beginTransaction();
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }


    /**
     * 执行自定义SQL语句
     */

    public void execSQL(String sql) {
        SQLiteDatabase db = null;

        try {
            if (sql.contains("select")) {
                Toast.makeText(context, "未处理select语句", Toast.LENGTH_SHORT).show();
            } else if (sql.contains("insert") || sql.contains("update") || sql.contains("delete")) {
                db = ordersDBHelper.getWritableDatabase();
                db.beginTransaction();
                db.execSQL(sql);
                db.setTransactionSuccessful();
                Toast.makeText(context, "执行SQL语句成功", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Toast.makeText(context, "执行出错，请检查SQL语句", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }


    /**
     * 查询数据库中所有数据
     */
    public List<Note> getAllDate() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<Note> orderList = new ArrayList<>();
        try {
            db = ordersDBHelper.getReadableDatabase();
            // select * from Note
            cursor = db.query(DBHelper.TABLE_NAME, ORDER_COLUMNS, null, null, null, null, null);

            if (cursor.getCount() > 0) {

                while (cursor.moveToNext()) {
                    orderList.add(parseNote(cursor));
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return orderList;
    }

    /**
     * 新增一条数据
     */
    public boolean insert(Note note) {
        SQLiteDatabase db = null;
        try {
            db = ordersDBHelper.getWritableDatabase();
            db.beginTransaction();
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.TEXT, note.getText());
            contentValues.put(DBHelper.COMMENT, note.getComment());
            contentValues.put(DBHelper.DATE, String.valueOf(note.getDate()));
            contentValues.put(DBHelper.TYPE, String.valueOf(note.getType()));
            db.insertOrThrow(DBHelper.TABLE_NAME, null, contentValues);

            db.setTransactionSuccessful();
            return true;
        } catch (SQLiteConstraintException e) {
            Toast.makeText(context, "主键重复", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return false;
    }


    /**
     * 删除一条数据  此处删除Id为7的数据
     */
    public boolean delete(Long id) {
        SQLiteDatabase db = null;

        try {
            db = ordersDBHelper.getWritableDatabase();
            db.beginTransaction();

            db.delete(DBHelper.TABLE_NAME, DBHelper.ID + " = ?", new String[]{String.valueOf(id)});
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
        return false;
    }


    /**
     * 修改一条数据  此处将Id为6的数据的OrderPrice修改了800
     */
    public boolean update(Note note) {
        SQLiteDatabase db = null;
        try {
            db = ordersDBHelper.getWritableDatabase();
            db.beginTransaction();

            // update Orders set OrderPrice = 800 where Id = 6
            ContentValues cv = new ContentValues();
            cv.put(DBHelper.TEXT, note.getText());
            cv.put(DBHelper.COMMENT, note.getComment());
            cv.put(DBHelper.DATE, String.valueOf(note.getDate()));
            cv.put(DBHelper.TYPE, String.valueOf(note.getType()));
            db.update(DBHelper.TABLE_NAME,
                    cv,
                    DBHelper.ID + " = ?",
                    new String[]{String.valueOf(note.getId())});
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }

        return false;
    }


    /**
     * 数据查询
     */
    public List<Note> query(String condition) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<Note> orderList = new ArrayList<>();
        try {
            db = ordersDBHelper.getReadableDatabase();

            // select * from Orders where CustomName = 'Bor'
            cursor = db.query(DBHelper.TABLE_NAME,
                    ORDER_COLUMNS,
                    DBHelper.TEXT + " = ?",
                    new String[]{condition},
                    null, null, null);

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    Note order = parseNote(cursor);
                    orderList.add(order);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return orderList;
    }

    public List<Note> likeQuery(String condition) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<Note> orderList = new ArrayList<>();
        try {
            db = ordersDBHelper.getReadableDatabase();

            // select * from Orders where CustomName = 'Bor'
            cursor = db.query(DBHelper.TABLE_NAME,
                    ORDER_COLUMNS,
                    DBHelper.TEXT + " like ?",
                    new String[]{condition},
                    null, null, null);

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    Note order = parseNote(cursor);
                    orderList.add(order);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return orderList;
    }

    /**
     * 将查找到的数据转换成Order类
     */
    private Note parseNote(Cursor cursor) {
        Note note = new Note();
        note.setId(cursor.getLong(cursor.getColumnIndex(DBHelper.ID)));
        note.setText(cursor.getString(cursor.getColumnIndex(DBHelper.TEXT)));
        note.setComment(cursor.getString(cursor.getColumnIndex(DBHelper.COMMENT)));

        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
        Date date = null;
        try {
            date = df.parse(cursor.getString(cursor.getColumnIndex(DBHelper.DATE)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        note.setDate(date);

        return note;
    }

}
