package com.example.sayo_chat.iniciosayochat

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sayo_chat.R
import com.example.sayo_chat.databinding.ActivityLoginBinding
import com.example.sayo_chat.databinding.ActivityMainBinding
import com.example.sayo_chat.utils.exibirMensagem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlin.system.exitProcess

class LoginActivity : AppCompatActivity() {

    private val binding by lazy{
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private lateinit var email: String
    private lateinit var senha: String

    //Firebase
    private val firebaseAuth by lazy{
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView( binding.root)
        inicializarEventosClique()
        firebaseAuth.signOut()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onStart(){
        super.onStart()
        verificarUsuarioLogado()
    }

    private fun verificarUsuarioLogado() {
        val usuarioAtual = FirebaseAuth.getInstance().currentUser
        if( usuarioAtual != null){
            startActivity(
                Intent(this, MainActivity::class.java)
            )
        }

    }

    private fun inicializarEventosClique() {
        binding.textCadastro.setOnClickListener{
            startActivity(
                Intent(this, CadastroActivity::class.java)
            )
        }

        binding.btnLogar.setOnClickListener{
            if( validarCampos() ){
                logarUsuario()

            }
        }
    }

    private fun logarUsuario() {
        firebaseAuth.signInWithEmailAndPassword(
            email,
            senha
        ).addOnSuccessListener {
            exibirMensagem("Login efetuado")
            startActivity(
                Intent(this, MainActivity::class.java )
            )
        }.addOnFailureListener{ erro ->
            try {
                throw erro
            }catch ( erroUsuarioInvalido: FirebaseAuthInvalidUserException) {
                erroUsuarioInvalido.printStackTrace()
                exibirMensagem("E-mail não cadastrado.")
            }catch (erroCredenciaisInvalidas: FirebaseAuthInvalidCredentialsException){
                erroCredenciaisInvalidas.printStackTrace()
                exibirMensagem("E-mail ou senha estão incorretos.")
            }
        }
    }

    private fun validarCampos(): Boolean {

        email = binding.editLoginEmail.text.toString()
        senha = binding.editLoginSenha.text.toString()

        if ( email.isNotEmpty() ){
            binding.textInputLayoutLoginEmail.error = null
            if( senha.isNotEmpty()) {
                binding.textInputLayoutLoginSenha.error = null
                return true
            }else{
                binding.textInputLayoutLoginSenha.error = "Preencha a senha"
                return false
            }
        }else {
            binding.textInputLayoutLoginEmail.error = "Preencha o e-mail"
            return false
        }

    }

}