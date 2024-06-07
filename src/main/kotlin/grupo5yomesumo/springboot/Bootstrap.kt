package grupo5yomesumo.springboot

import grupo5yomesumo.springboot.domain.*
import grupo5yomesumo.springboot.repository.*
import grupo5yomesumo.springboot.service.EventoService
import org.springframework.beans.factory.InitializingBean
import org.springframework.data.geo.Point
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalTime


@Component
class Bootstrap(
    val eventoService: EventoService,
    val eventoRepository: EventoRepository,
    val usuarioRepository: UsuarioRepository,
    val actividadRepository: Actividadrepository,
    val solicitudRepository: SolicitudRepository,
    val mensajeRepository: MensajeRepository,
    val ubicacionRepository: UbicacionRepository
) : InitializingBean
{
    override fun afterPropertiesSet() {
        //EVENTOS
        clearMongoRepositories()

        val usuarioA = Usuario(nombre = "Felipe", apellido = "Gamalleri", username = "feli", password = "1234")
        val usuarioB = Usuario(nombre = "Nicolas", apellido = "Masuyama", username = "masu", password = "1234")
        val usuarioC = Usuario(nombre = "Lucas", apellido = "Antenni", username = "lugant", password = "1234")
        val usuarioD = Usuario(nombre = "Rodrigo", apellido = "Tartaglia", username = "rodri", password = "1234")

        arrayOf(
            usuarioA, usuarioB, usuarioC, usuarioD
        ).forEach { usuarioRepository.save(it) }


        //ACTIVIDADES
        val basquet = Actividad(nombre = "Basquet", esGrupal = true)
        val futbol = Actividad(nombre = "Futbol", esGrupal = true)

        arrayOf(
            basquet, futbol
        ).forEach { actividadRepository.save(it) }

        val sportivoBallester = Ubicacion(
            nombreLugar = "Club Sportivo Ballester, Gral. Roca 3123, Villa Ballester, Buenos Aires Province B1653, Argentina",
            coordenadas = Point(-34.550822, -58.561222),
            barrio = "Villa Ballester"
        )
        ubicacionRepository.save(sportivoBallester)


        val futbolConLosPibes = Evento(anfitrion = usuarioA, actividad = futbol, fecha = LocalDate.now(), hora = LocalTime.of(20, 30), ubicacion = sportivoBallester, capacidadMaxima = 10)
        val basquet3Vs3 = Evento(anfitrion = usuarioB, actividad = basquet, fecha = LocalDate.now(), hora = LocalTime.of(17, 0), ubicacion = sportivoBallester, capacidadMaxima = 6)
        val eventoTerminado = Evento(anfitrion = usuarioC, actividad = futbol, fecha = LocalDate.now().minusDays(1), hora = LocalTime.now(), ubicacion = sportivoBallester, capacidadMaxima = 12)

        arrayOf(
            futbolConLosPibes, basquet3Vs3, eventoTerminado
        ).forEach { eventoRepository.save(it) }


        val solicitudAceptada = Solicitud(solicitante = usuarioD, evento = basquet3Vs3)
        solicitudAceptada.responderSolicitud(true)

        arrayOf(
            solicitudAceptada
        ).forEach { solicitudRepository.save(it) }
    }

    fun clearMongoRepositories(){
        mensajeRepository.deleteAll()
    }

}
