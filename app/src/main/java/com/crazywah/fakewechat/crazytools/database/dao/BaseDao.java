package com.crazywah.fakewechat.crazytools.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by FungWah on 2017/10/27.
 */

public class BaseDao {

    private SQLiteOpenHelper helper;

    public BaseDao(SQLiteOpenHelper helper) {
        this.helper = helper;
    }

    /**
     * 
     * @param table 表名
     * @param contentValues 需要被插入的一行数据的所有必须字段与字段值对
     * @return 操作是否成功
     */
    protected boolean insert(String table, ContentValues contentValues) {
        boolean flag = false;
        long index = -1L;
        SQLiteDatabase database = null;
        try {
            database = helper.getWritableDatabase();
            //第二个参数用于把没有指定字段的数据插入到第二个参数的列中,可为null
            index = database.insert(table, null, contentValues);
            if (index != -1) {
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        } finally {
            database.close();
        }

        return flag;
    }

    /**
     *
     * @param table 表名
     * @param contentValues 需要被修改的字段与字段值对
     * @param where 约束字段 " name = ? "、" name = ? AND num = ? "、" name = 'Tom' " ，注意若在此参数指明了参数值请在Args参数处传null
     * @param whereArgs 约束字段对应的值
     * @return 操作是否成功
     */
    protected boolean update(String table, ContentValues contentValues, String where, String whereArgs[]) {
        boolean flag = false;
        int index = 0;
        SQLiteDatabase database = null;
        try {
            database = helper.getWritableDatabase();
            /**
             * Convenience method for updating rows in the com.example.fungwahtools.database.
             *
             * @param table the table to update in 表名
             * @param values a map from column names to new column values. null is a
             *            valid value that will be translated to NULL.
             * @param whereClause the optional WHERE clause to apply when updating.
             *            Passing null will update all rows. 查找参数字段
             * @param whereArgs You may include ?s in the where clause, which
             *            will be replaced by the values from whereArgs. The values
             *            will be bound as Strings. 查找参数值
             * @return the number of rows affected 受影响的行数
             */
            index = database.update(table, contentValues, where, whereArgs);
            if (index != -1) {
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        } finally {
            database.close();
        }
        return flag;
    }

    /**
     * 基础的删除功能
     * @param tableName 表名
     * @param where 约束字段名 " name = ? "、" name = ? AND num = ? "、" name = 'Tom' " ，注意若在此参数指明了参数值请在Args参数处传null
     * @param whereArgs 字段对应的值 对应的值 对应多少个问号就多少个值
     * @return 操作是否成功
     */
    private boolean delete(String tableName, String where, String whereArgs[]) {
        boolean flag = false;
        int index = -1;
        SQLiteDatabase database = null;
        try {
            database = helper.getWritableDatabase();
            /**
             * Convenience method for deleting rows in the com.example.fungwahtools.database.
             *
             * @param table the table to delete from
             * @param whereClause the optional WHERE clause to apply when deleting.
             *            Passing null will delete all rows.
             * @param whereArgs You may include ?s in the where clause, which
             *            will be replaced by the values from whereArgs. The values
             *            will be bound as Strings.
             * @return the number of rows affected if a whereClause is passed in, 0
             *         otherwise. To remove all rows and get a count pass "1" as the
             *         whereClause.
             */
            index = database.delete(tableName, where, whereArgs);
            if (index != -1) {
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        } finally {
            database.close();
        }
        return flag;
    }

    /**
     * 搜索全表
     *
     * @param table 表名
     * @return 返回所有符合条件的数据
     */
    private List<Map> selectAll(String table) {
        return select(table, null, null, null, null, null, null);
    }

    /**
     * 基础的 Select 方法
     *
     * @param table         表名
     * @param columns       选择要获取到的字段
     * @param selection     约束字段名 如： " name = ? "、" name = ? AND num = ? "、" name = 'Tom' "，注意若在此参数指明了参数值请在Args参数处传null
     * @param selectionArgs 对应的值 对应多少个问号就多少个值
     * @param groupBy       用于分组的字段名 " sex "
     * @param having        选择字段对应值的分组 " '男' "
     * @param orderBy       排序 " num ASC " " num DESC "
     * @return 返回所有符合条件的数据
     */
    public List<Map> select(String table, String[] columns, String selection,
                            String[] selectionArgs, String groupBy, String having,
                            String orderBy) {
        List<Map> mapList = new ArrayList<>();
        SQLiteDatabase database = null;
        /**
         * getType()返回的类型与对应的 int 值
         * int FIELD_TYPE_BLOB = 4;
         * int FIELD_TYPE_FLOAT = 2;
         * int FIELD_TYPE_INTEGER = 1;
         * int FIELD_TYPE_NULL = 0;
         * int FIELD_TYPE_STRING = 3;
         */
        Cursor cursor = null;
        try {
            database = helper.getReadableDatabase();
            /**
             * Query the given table, returning a {@link Cursor} over the result set.
             *
             * @param table The table name to compile the query against.
             * @param columns A list of which columns to return. Passing null will
             *            return all columns, which is discouraged to prevent reading
             *            data from storage that isn't going to be used.
             * @param selection A filter declaring which rows to return, formatted as an
             *            SQL WHERE clause (excluding the WHERE itself). Passing null
             *            will return all rows for the given table. 约束条件
             * @param selectionArgs You may include ?s in selection, which will be
             *         replaced by the values from selectionArgs, in order that they
             *         appear in the selection. The values will be bound as Strings. 约束条件参数
             * @param groupBy A filter declaring how to group rows, formatted as an SQL
             *            GROUP BY clause (excluding the GROUP BY itself). Passing null
             *            will cause the rows to not be grouped. 对某属性相等的行进行分组
             * @param having A filter declare which row groups to include in the cursor,
             *            if row grouping is being used, formatted as an SQL HAVING
             *            clause (excluding the HAVING itself). Passing null will cause
             *            all row groups to be included, and is required when row
             *            grouping is not being used.  选择对应属性值的分组
             * @param orderBy How to order the rows, formatted as an SQL ORDER BY clause
             *            (excluding the ORDER BY itself). Passing null will use the
             *            default sort order, which may be unordered. 排序方式
             * @return A {@link Cursor} object, which is positioned before the first entry. Note that
             * {@link Cursor}s are not synchronized, see the documentation for more details.
             * @see Cursor
             */
            cursor = database.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
            while (cursor.moveToNext()) {
                Map map = new HashMap<>();
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    //判断数据类型再按相应的类型进行存储
                    switch (cursor.getType(i)) {
                        case 0:
                            map.put(cursor.getColumnName(i), null);
                            break;
                        case 1:
                            map.put(cursor.getColumnName(i), cursor.getInt(i));
                            break;
                        case 2:
                            map.put(cursor.getColumnName(i), cursor.getFloat(i));
                            break;
                        case 3:
                            map.put(cursor.getColumnName(i), cursor.getString(i));
                            break;
                        case 4:
                            map.put(cursor.getColumnName(i), cursor.getBlob(i));
                            break;
                    }
                }
                mapList.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.close();
        }
        return mapList;
    }

}
