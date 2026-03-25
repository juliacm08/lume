package Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.univap.lume.R;
import Model.UserModel;

public class Register extends AppCompatActivity {

    private EditText edt_nome_register, edt_email_register,
            edt_senha_register, edt_confirmar_senha_register;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        // 1. Inicializa os campos de texto
        edt_nome_register = findViewById(R.id.edt_nome_register);
        edt_email_register = findViewById(R.id.edt_email_register);
        edt_senha_register = findViewById(R.id.edt_senha_register);
        edt_confirmar_senha_register = findViewById(R.id.edt_confirmar_senha_register);

        // 2. Configura o botão de Cadastro
        Button btn_register = findViewById(R.id.btn_register);
        if (btn_register != null) {
            btn_register.setOnClickListener(v -> {
                Log.d("DEBUG_LUME", "BOTÃO CLICADO!");
                registrarUsuario();
            });
        }

        // 3. Configura o clique para ir para a tela de Login
        TextView txt_ir_login = findViewById(R.id.txt_ir_login);
        if (txt_ir_login != null) {
            txt_ir_login.setOnClickListener(v -> {
                // Abre a classe Login que acabamos de padronizar
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            });
        }
    }

    private void registrarUsuario() {
        String nome = edt_nome_register.getText().toString().trim();
        String email = edt_email_register.getText().toString().trim();
        String senha = edt_senha_register.getText().toString();
        String confirmar = edt_confirmar_senha_register.getText().toString();

        if (TextUtils.isEmpty(nome) || TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(senha) || TextUtils.isEmpty(confirmar)) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!senha.equals(confirmar)) {
            Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Processando cadastro...", Toast.LENGTH_SHORT).show();

        mAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && mAuth.getCurrentUser() != null) {
                        String id = mAuth.getCurrentUser().getUid();

                        UserModel user = new UserModel();
                        user.setId(id);
                        user.setNome(nome);
                        user.setEmail(email);
                        user.setSenha(senha);
                        user.salvar();

                        Toast.makeText(this, "Cadastro realizado!", Toast.LENGTH_SHORT).show();

                        // Opcional: Ir para a Login após cadastrar
                        finish();
                    } else {
                        String erro = task.getException() != null ? task.getException().getMessage() : "Erro desconhecido";
                        Toast.makeText(this, "Erro: " + erro, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}