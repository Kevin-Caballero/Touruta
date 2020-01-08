package aplicacion.android.kvn.touruta;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.text.UnicodeSetSpanner;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION=1;

    private static final String DATABASE_NAME="TOURUTA.db";

    public static final String TABLE_USERS="users";

    public static final String COLUMN_ID="userId";
    public static final String COLUMN_EMAIL="email";
    public static final String COLUMN_PASSWORD="password";
    public static final String COLUMN_NAME="name";
    public static final String COLUMN_LAST_NAME="lastName";
    public static final String COLUMN_NICK_NAME="nicNname";

    Context context;

    public MyDBHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String creationQuery = " CREATE TABLE " + TABLE_USERS
                + " ( "
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_EMAIL + " TEXT NOT NULL UNIQUE ON CONFLICT ROLLBACK, "
                + COLUMN_PASSWORD + " TEXT NOT NULL, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_LAST_NAME + " TEXT, "
                + COLUMN_NICK_NAME + " TEXT "
                + " ); ";

                db.execSQL(creationQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public int AddUser(User user){
        ContentValues values = new ContentValues();

        values.put(COLUMN_EMAIL,user.getEmail());
        values.put(COLUMN_PASSWORD,user.getPassword());
        values.put(COLUMN_NAME,user.getName());
        values.put(COLUMN_LAST_NAME,user.getLastName());
        values.put(COLUMN_NICK_NAME,user.getNickName());

        SQLiteDatabase db = getWritableDatabase();

        if(db.insert(TABLE_USERS,null,values)!=0.0){
            //USUARIO INSERTADO
            db.close();
            return 1;
        }else{
            db.close();
            return 0;
        }
    }
}
