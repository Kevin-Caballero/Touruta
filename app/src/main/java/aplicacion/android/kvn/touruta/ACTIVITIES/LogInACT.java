package aplicacion.android.kvn.touruta.ACTIVITIES;

import androidx.appcompat.app.AppCompatActivity;
import aplicacion.android.kvn.touruta.MyDBHandler;
import aplicacion.android.kvn.touruta.R;
import aplicacion.android.kvn.touruta.OBJECTS.User;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LogInACT extends AppCompatActivity implements View.OnClickListener {

    Button btnLoginLI, btnSignupLI;
    EditText txtEmailLI, txtPasswordLI;
    SQLiteDatabase db;
    MyDBHandler dbHandler;
    public static User logedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        txtEmailLI =findViewById(R.id.txtEmailLI);
        txtPasswordLI =findViewById(R.id.txtPassword);
        btnLoginLI =findViewById(R.id.btnLoginLI);
        btnSignupLI =findViewById(R.id.btnSignupLI);
        btnLoginLI.setOnClickListener(this);
        btnSignupLI.setOnClickListener(this);

        dbHandler=new MyDBHandler(this,MyDBHandler.DATABASE_NAME,null,1);
    }

    @Override
    public void onClick(View v) {
        if(v== btnLoginLI){Login();}
        else if(v== btnSignupLI){Signup();}
    }

    private void Signup() {
        Intent SignUpIntent = new Intent(this, SignUpACT.class);
        startActivity(SignUpIntent);
    }

    private void Login() {
        String insertedEmail="";
        String returnedPassword;
        try {
            insertedEmail = txtEmailLI.getText().toString().toLowerCase();
        }catch (Exception ex){
            ex.printStackTrace();
        }

        db = dbHandler.getReadableDatabase();
        Cursor c = db.query(MyDBHandler.TABLE_USERS,null ,MyDBHandler.COLUMN_USER_EMAIL + "=?",new String[]{insertedEmail},null,null,null);

        if(c.moveToFirst()){
            returnedPassword=c.getString(2);
            if(returnedPassword.compareTo(txtPasswordLI.getText().toString())==0){
                //Toast.makeText(this,"ADELANTE",Toast.LENGTH_SHORT).show();

                logedUser=new User();
                logedUser.setEmail(c.getString(1));
                logedUser.setName(c.getString(3));
                logedUser.setLastName(c.getString(4));
                logedUser.setNickName(c.getString(5));

                Intent ToursIntent = new Intent(this, ToursACT.class);
                startActivity(ToursIntent);
            }
        }
    }
}
