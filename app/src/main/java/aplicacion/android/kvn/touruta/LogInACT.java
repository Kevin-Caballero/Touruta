package aplicacion.android.kvn.touruta;

import androidx.appcompat.app.AppCompatActivity;

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
        String insertedEmail = txtEmailLI.getText().toString().toLowerCase();
        String returnedPassword;
        db= SignUpACT.dbHandler.getReadableDatabase();
        Cursor c = db.query(MyDBHandler.TABLE_USERS,new String[] {MyDBHandler.COLUMN_USER_PASSWORD},MyDBHandler.COLUMN_USER_EMAIL + "=?",new String[]{insertedEmail},null,null,null);
        if(c.moveToFirst()){
            returnedPassword=c.getString(0);
            if(returnedPassword.compareTo(txtPasswordLI.getText().toString())==0){
                //Toast.makeText(this,"ADELANTE",Toast.LENGTH_SHORT).show();
                Intent ToursIntent = new Intent(this, ToursACT.class);
                startActivity(ToursIntent);
            }
        }
    }


}
