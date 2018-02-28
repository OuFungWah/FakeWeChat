package com.crazywah.fakewechat.crazytools.database.helper;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by FungWah on 2017/10/25.
 */

public class BaseDatabaseHelper extends SQLiteOpenHelper {

    /**
     * Create a helper object to create, open, and/or manage a com.example.fungwahtools.database.
     * This method always returns very quickly.  The com.example.fungwahtools.database is not actually
     * created or opened until one of {@link #getWritableDatabase} or
     * {@link #getReadableDatabase} is called.
     *
     * 在调用 getWritableDatabase 或 getReadableDatabase 之前数据库都未被正式创建
     *
     * @param context to use to open or create the com.example.fungwahtools.database
     * @param name of the com.example.fungwahtools.database file, or null for an in-memory com.example.fungwahtools.database
     * @param factory to use for creating cursor objects, or null for the default
     * @param version number of the com.example.fungwahtools.database (starting at 1); if the com.example.fungwahtools.database is older,
     *     {@link #onUpgrade} will be used to upgrade the com.example.fungwahtools.database; if the com.example.fungwahtools.database is
     *     newer, {@link #onDowngrade} will be used to downgrade the com.example.fungwahtools.database
     */
    public BaseDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public BaseDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    /**
     * Called when the com.example.fungwahtools.database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The com.example.fungwahtools.database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    /**
     * Called when the com.example.fungwahtools.database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     *
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db The com.example.fungwahtools.database.
     * @param oldVersion The old com.example.fungwahtools.database version.
     * @param newVersion The new com.example.fungwahtools.database version.
     */
    /**
     * 更新数据库
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
