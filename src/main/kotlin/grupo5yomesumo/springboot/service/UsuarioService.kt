package grupo5yomesumo.springboot.service

import grupo5yomesumo.springboot.domain.Usuario
import grupo5yomesumo.springboot.domain.exceptions.BusinessException
import grupo5yomesumo.springboot.domain.exceptions.NotFoundException
import grupo5yomesumo.springboot.repository.UsuarioRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class UsuarioService(
    val usuarioRepository: UsuarioRepository
){

    fun logIn(username: String, password: String): Usuario {
        val usuario = usuarioRepository.findByUsernameAndPassword(username, password)
            ?: throw NotFoundException("Usuario no encontrado")
        return usuario
    }
    @Transactional
    fun save(usuario: Usuario) = usuarioRepository.save(usuario)

    fun getAllUsuarios(): List<Usuario> = usuarioRepository.findAll().toList()

    fun getUsuario(usuarioId: Long): Usuario = usuarioRepository.findById(usuarioId).orElseThrow{NotFoundException("No se encontro el usuario con el id $usuarioId")}

    @Transactional
    fun signUp(usuario: Usuario) {
        validarUsername(usuario.username)
        save(usuario)
    }

    fun validarUsername(username: String){
        val existe: Boolean = usuarioRepository.existsUsuarioByUsername(username)
        if (existe) throw BusinessException("Ya existe un usuario con ese nombre")
    }

}