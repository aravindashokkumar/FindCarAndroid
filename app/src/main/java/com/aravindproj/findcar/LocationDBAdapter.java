package com.aravindproj.findcar;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by aravindashokkumar on 27/7/15.
 */
public class LocationDBAdapter {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_LAT = "Lat";
    public static final String KEY_LOCATION = "Location";
    public static final String KEY_LONG = "Long";

    private static final String DATABASE_TABLE = "Location";
    private Context context;
    private SQLiteDatabase database;
    private LocationDatabaseHelper dbHelper;

    public LocationDBAdapter(Context context) {
        this.context = context;
    }

    public LocationDBAdapter open() throws SQLException {
        dbHelper = new LocationDatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public long createLocation(double lat, double longi,
                               String location) {
        ContentValues initialValues = createContentValues(lat, longi,
                location);

        long row = database.insert(DATABASE_TABLE, null, initialValues);
        Log.i("DATABASE", Long.toString(row));
        return row;
    }

    public boolean updateLocation(long rowId,double lat, double longi,
                                  String location) {
        ContentValues updateValues = createContentValues(lat, longi,
                location);

        return database.update(DATABASE_TABLE, updateValues, KEY_ROWID + "="
                + rowId, null) > 0;
    }


    public Cursor fetchAllLocations() {
        return database.query(DATABASE_TABLE, new String[] { KEY_ROWID,
                        KEY_LAT,  KEY_LONG,KEY_LOCATION},
                null, null, null, null, null);
    }

    public Cursor fetchLocation(long rowId) throws SQLException {
        Cursor mCursor = database.query(true, DATABASE_TABLE, new String[] {
                        KEY_ROWID, KEY_LAT, KEY_LONG, KEY_LOCATION },
                KEY_ROWID + "=?", new String[] { Long.toString(rowId) } ,
                null, null, null, null);
        if (mCursor != null)
            mCursor.moveToFirst();
        return mCursor;
    }

    private ContentValues createContentValues(double lat, double longi,
                                              String location) {
        ContentValues values = new ContentValues();
        values.put(KEY_LAT, lat);

        values.put(KEY_LONG, longi);
        values.put(KEY_LOCATION, location);
        return values;
    }

}