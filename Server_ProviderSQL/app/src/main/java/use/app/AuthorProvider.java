package use.app;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AuthorProvider extends ContentProvider {

    static final String AUTHORITY = "content://use.app.AuthorProvider/author";
    static final Uri CONTENT_URI = Uri.parse(AUTHORITY);

    static final String DB_NAME = "authordb";

    private SQLiteDatabase database;

    @Override
    public boolean onCreate() {
        Context context = getContext();

        SQLiteOpenHelper helper = new SQLiteOpenHelper(context, DB_NAME, null, 1) {
            @Override
            public void onCreate(SQLiteDatabase sqLiteDatabase) {
                sqLiteDatabase.execSQL("create table author(id integer primary key, name nvarchar(50), sdt varchar(10))");
                sqLiteDatabase.execSQL("create table book(id integer primary key, tensach nvarchar(150), " +
                                        "id_author integer not null constraint FK_Books references author(id) on delete cascade)");
            }

            @Override
            public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
                sqLiteDatabase.execSQL("Drop table if exists book");
                sqLiteDatabase.execSQL("Drop table if exists author");
                onCreate(sqLiteDatabase);
            }
        };

        database = helper.getWritableDatabase();
        if (database != null)
            return true;
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String query, @Nullable String[] strings1, @Nullable String s1) {
        Cursor cursor = database.rawQuery(query, null);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        long rowID = database.insert("author", null, contentValues);
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            Log.d("Thêm thành công", "---------------");
            return _uri;
        }
        throw new SQLException("Failed to add a record id " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String id, @Nullable String[] strings) {
        int count = database.delete("author", "id = " + Integer.parseInt(id), strings);
        getContext().getContentResolver().notifyChange(uri, null);
        Log.d("Xóa thành công", "----------");
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String id, @Nullable String[] strings) {
        int count = database.update("author", contentValues, "id = " + Integer.parseInt(id), strings);
        getContext().getContentResolver().notifyChange(uri, null);
        Log.d("Cập nhật thành công", "-----------");
        return count;
    }
}
