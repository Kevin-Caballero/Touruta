package aplicacion.android.kvn.touruta.ACTIVITIES;

import androidx.appcompat.app.AppCompatActivity;
import aplicacion.android.kvn.touruta.MyDBHandler;
import aplicacion.android.kvn.touruta.R;
import aplicacion.android.kvn.touruta.OBJECTS.User;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

public class SignUpACT extends AppCompatActivity implements View.OnClickListener {

    Button btnSignup;
    EditText txtEmailSU,txtPasswordSU,txtPassword2SU,txtNameSU,txtLastnameSU,txtNicknameSU;
    User newUser;
    public static MyDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        txtEmailSU=findViewById(R.id.txtEmailSU);
        txtPasswordSU=findViewById(R.id.txtPasswordSU);
        txtPassword2SU=findViewById(R.id.txtRepeatPasswordSU);
        txtNameSU=findViewById(R.id.txtNameSU);
        txtLastnameSU=findViewById(R.id.txtLastNameSU);
        txtNicknameSU=findViewById(R.id.txtNickNameSU);

        btnSignup=findViewById(R.id.btnSignUp);
        btnSignup.setOnClickListener(this);

        dbHandler= new MyDBHandler(this,MyDBHandler.DATABASE_NAME,null,1);

    }

    /**Al hacer click en el boton de registro se validan tanto contraseña como email y se crea un
     * objeto de la clase User para añadir su contenido a la base de datos*/
    @Override
    public void onClick(View view) {
        String email=null,password=null,name,lastname,nickname;

        //Validar email
        if(EmailValidation(txtEmailSU.getText().toString())){
            email=txtEmailSU.getText().toString().toLowerCase();
        }
        else{
            Toast.makeText(this,"EMAIL ERROR",Toast.LENGTH_SHORT).show();
        }

        //Validar Contraseña
        if(PasswordValidation(txtPasswordSU.getText().toString(), txtPassword2SU.getText().toString())){
            password=txtPasswordSU.getText().toString();
        }
        else{
            Toast.makeText(this,"PASSWORD ERROR",Toast.LENGTH_SHORT).show();
        }

        if(!EmailValidation(txtEmailSU.getText().toString()) && !PasswordValidation(txtPasswordSU.getText().toString(), txtPassword2SU.getText().toString())){
            Toast.makeText(this,"EMAIL AND PASSWORD ERROR",Toast.LENGTH_SHORT).show();
        }

        name=txtNameSU.getText().toString();
        lastname=txtLastnameSU.getText().toString();
        nickname=txtNicknameSU.getText().toString();

        newUser=new User(email,password,name,lastname,nickname);

        if(dbHandler.AddUser(newUser)==1){
            Intent LogInIntent = new Intent(this, LogInACT.class);
            startActivity(LogInIntent);
        }else{
            Toast.makeText(this,"ERROR", Toast.LENGTH_SHORT).show();
        }

        CleanForm();
    }

    /**Funcion para limpiar el formulario*/
    private void CleanForm() {
        txtEmailSU.setText("");
        txtPasswordSU.setText("");
        txtPassword2SU.setText("");
        txtNameSU.setText("");
        txtLastnameSU.setText("");
        txtNicknameSU.setText("");
    }

    /**Validacion de contraseña.
     * Primeramente se validara que tanto el valor de la primera caja de contraseña como el segundo
     * tengan la misma longitud, si eso se cumple se valida si ambas contraseñas son iguales
     * y retorna un booleano en base a dicha comprobacion*/
    private boolean PasswordValidation(String pw1, String pw2) {
        if(pw1.length()>=8 && pw2.length()>=8)
            return pw1.equals(pw2);
        return false;
    }

    /**Validacion del campo email gracias a la clase Pattern que devolvera cierto si se cumple el patron*/
    private boolean EmailValidation(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
}
