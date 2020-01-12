package aplicacion.android.kvn.touruta;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDBHandler extends SQLiteOpenHelper {

    //TODO una clase que contenga la info de la BD

    private static final int DATABASE_VERSION=1;

    //DATABASE
    private static final String DATABASE_NAME="TOURUTA.db";

    //TABLES
    public static final String TABLE_USERS="users";
    public static final String TABLE_TOURS="tours";
    public static final String TABLE_COMMENTS="comments";

    //USER COLUMS
    public static final String COLUMN_USER_ID ="userId";
    public static final String COLUMN_USER_EMAIL ="email";
    public static final String COLUMN_USER_PASSWORD ="password";
    public static final String COLUMN_USER_NAME ="name";
    public static final String COLUMN_USER_LAST_NAME ="lastName";
    public static final String COLUMN_USER_NICK_NAME ="nicNname";
    public static final String COLUMN_USER_PICTURE ="picture";

    //TOURS COLUMNS
    public static final String COLUMN_TOUR_ID = "tourId";
    public static final String COLUMN_TOUR_NAME="name";
    public static final String COLUMN_TOUR_DESCRIPTION="description";
    public static final String COLUMN_TOUR_COUNTRY="country";
    public static final String COLUMN_TOUR_DISTANCE="distance";
    public static final String COLUMN_TOUR_DURATION="duration";
    public static final String COLUMN_TOUR_NUM_CHECKPOINTS="numCheckpoints";
    public static final String COLUMN_TOUR_PICTURE="picture";

    //COMMENTS COLUMNS
    public static final String COLUMN_COMMENT_ID="commentId";
    public static final String COLUMN_COMMENT_TOUR_ID="tourId";
    public static final String COLUMN_COMMENT_USER_ID="userId";
    public static final String COLUMN_COMMENT_CONTENT="content";

    Context context;


    public MyDBHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String creationQueryComments = " CREATE TABLE " + TABLE_COMMENTS
                +" ( "
                + COLUMN_COMMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_COMMENT_TOUR_ID + " INTEGER, "
                + COLUMN_COMMENT_USER_ID + "INTEGER, "
                + COLUMN_COMMENT_CONTENT + "TEXT"
                +" ); ";
        db.execSQL(creationQueryComments);


        String creationQueryUsers = " CREATE TABLE " + TABLE_USERS
                + " ( "
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USER_EMAIL + " TEXT NOT NULL UNIQUE ON CONFLICT ROLLBACK, "
                + COLUMN_USER_PASSWORD + " TEXT NOT NULL, "
                + COLUMN_USER_NAME + " TEXT, "
                + COLUMN_USER_LAST_NAME + " TEXT, "
                + COLUMN_USER_NICK_NAME + " TEXT, "
                + COLUMN_USER_PICTURE + " TEXT, "
                + "FOREIGN KEY (" + COLUMN_USER_ID + ") REFERENCES " + TABLE_COMMENTS +"("+COLUMN_COMMENT_USER_ID+") "
                + " ); ";
        db.execSQL(creationQueryUsers);


        String creationQueryTours = " CREATE TABLE " + TABLE_TOURS
                +" ( "
                + COLUMN_TOUR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TOUR_NAME + " TEXT, "
                + COLUMN_TOUR_DESCRIPTION + " TEXT, "
                + COLUMN_TOUR_COUNTRY + " TEXT, "
                + COLUMN_TOUR_DISTANCE + " REAL, "
                + COLUMN_TOUR_DURATION + " REAL, "
                + COLUMN_TOUR_NUM_CHECKPOINTS + " INTEGER, "
                + COLUMN_TOUR_PICTURE + " TEXT, "
                + "FOREIGN KEY (" + COLUMN_TOUR_ID + ") REFERENCES " + TABLE_COMMENTS +"("+COLUMN_COMMENT_TOUR_ID+") "
                +" ); ";
        db.execSQL(creationQueryTours);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public int AddUser(User user){
        ContentValues values = new ContentValues();

        values.put(COLUMN_USER_EMAIL,user.getEmail());
        values.put(COLUMN_USER_PASSWORD,user.getPassword());
        values.put(COLUMN_USER_NAME,user.getName());
        values.put(COLUMN_USER_LAST_NAME,user.getLastName());
        values.put(COLUMN_USER_NICK_NAME,user.getNickName());

        SQLiteDatabase db = getWritableDatabase();

        if(db.insert(TABLE_USERS,null,values) != -1){
            //USUARIO INSERTADO
            db.close();
            return 1;
        }else{
            db.close();
            return -1;
        }
    }
}
