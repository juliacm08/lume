package Activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.univap.lume.R;

public class Login extends AppCompatActivity {

    private EditText edt_email_login, edt_senha_login;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        edt_email_login = findViewById(R.id.edt_email_login);
        edt_senha_login = findViewById(R.id.edt_senha_login);
        Button btn_login = findViewById(R.id.btn_login_acao);
        TextView txt_ir_cadastro = findViewById(R.id.txt_ir_cadastro);

        // Ação do botão de Login
        if (btn_login != null) {
            btn_login.setOnClickListener(v -> {
                String email = edt_email_login.getText().toString().trim();
                String senha = edt_senha_login.getText().toString().trim();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(senha)) {
                    Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                } else {
                    logarUsuario(email, senha);
                }
            });
        }

        // Ação para voltar ao cadastro
        if (txt_ir_cadastro != null) {
            txt_ir_cadastro.setOnClickListener(v -> {
                finish(); // Fecha o login e volta para a tela anterior (Register)
            });
        }
    }

    private void logarUsuario(String email, String senha) {
        mAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Login com sucesso!", Toast.LENGTH_SHORT).show();
                    } else {
                        String erro = task.getException() != null ? task.getException().getMessage() : "Erro";
                        Toast.makeText(this, "Erro: " + erro, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}