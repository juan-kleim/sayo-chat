package com.example.sayo_chat.iniciosayochat

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sayo_chat.MainActivity
import com.example.sayo_chat.R
import com.example.sayo_chat.databinding.ActivityCadastroBinding
import com.example.sayo_chat.utils.exibirMensagem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class CadastroActivity : AppCompatActivity() {

    private val binding by lazy{
        ActivityCadastroBinding.inflate(layoutInflater)
    }

    private lateinit var nome: String
    private lateinit var email: String
    private lateinit var senha: String
    private val firebaseAuth by lazy{
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView( binding.root)

        inicializarToolbar()
        inicializarEventosClique()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        inicializarToolbar()
    }

    private fun inicializarToolbar() {
        val toolbar = binding.includeToolbar.tbPrincipal
        setSupportActionBar( toolbar )
        supportActionBar?.apply{
            title = "Cadastre-se"
            setDisplayHomeAsUpEnabled(true)
        }

    }
    private fun inicializarEventosClique(){
        binding.btnCadastrar.setOnClickListener{
            if ( validarCampos() ) {
                cadastrarUsuario(nome, email, senha)
            }
        }
    }

    private fun cadastrarUsuario(nome: String, email: String, senha: String){

        firebaseAuth.createUserWithEmailAndPassword(
            email, senha
        ).addOnCompleteListener{ resultado ->
            if (resultado.isSuccessful){
                exibirMensagem("Sucesso ao fazer seu cadastro!")
                startActivity(
                    Intent(applicationContext, MainActivity::class.java)
             )
            }
        }.addOnFailureListener { erro ->
            try {
                throw erro

            } catch (erroSenhaFraca: FirebaseAuthWeakPasswordException){
                erroSenhaFraca.printStackTrace()
                exibirMensagem("Senha fraca. Digite outra senha com letras, números e caracteres especiais")

            } catch (erroUsuarioExistente: FirebaseAuthUserCollisionException){
                erroUsuarioExistente.printStackTrace()
            exibirMensagem("E-mail já cadastrado")

            } catch (erroCredenciaisInvalidas: FirebaseAuthInvalidCredentialsException){
                erroCredenciaisInvalidas.printStackTrace()
                exibirMensagem("E-mail inválido. Tente outro")

            }

        }

    }

    private fun validarCampos(): Boolean {

        nome = binding.editNome.text.toString()
        email = binding.editEmail.text.toString()
        senha = binding.editSenha.text.toString()

        if (nome.isNotEmpty()) {
            binding.textInputLayoutNome.error = null

            if (email.isNotEmpty()) {
                binding.textInputLayoutEmail.error = null

                if (senha.isNotEmpty()) {
                    binding.textInputLayoutSenha.error = null
                    return true

                } else {
                    binding.textInputLayoutSenha.error = "Digite sua senha"
                    return false
                }
            } else {
                binding.textInputLayoutEmail.error = "Preenha o campo email"
                return false
            }
        } else {
            binding.textInputLayoutNome.error = "Preencha o campo nome"
            return false
        }
    }
}