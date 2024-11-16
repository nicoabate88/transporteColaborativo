package universidad.tpteColaborativo.controladores;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import universidad.tpteColaborativo.entidades.Usuario;
import universidad.tpteColaborativo.excepciones.MiException;
import universidad.tpteColaborativo.servicios.CalificacionServicio;
import universidad.tpteColaborativo.servicios.EmailServicio;
import universidad.tpteColaborativo.servicios.ReservaServicio;
import universidad.tpteColaborativo.servicios.UsuarioServicio;
import universidad.tpteColaborativo.servicios.ViajeServicio;

@Controller
@RequestMapping("/usuario")

public class UsuarioControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private ViajeServicio viajeServicio;
    @Autowired
    private ReservaServicio reservaServicio;
    @Autowired
    private CalificacionServicio calificacionServicio;
    @Autowired
    private EmailServicio emailServicio;

    @GetMapping("/registrar")
    public String registrarUsuario(ModelMap modelo) {

        String localidad = "";

        modelo.put("localidad", localidad);

        return "usuario_registrar.html";
    }

    @PostMapping("/verificar")
    public String registroUsuario(@RequestParam String nombre, @RequestParam String telefono, @RequestParam String localidad, @RequestParam String domicilio,
            @RequestParam String email, @RequestParam String password, @RequestParam String password2, ModelMap modelo) {

        try {

            usuarioServicio.validarDatosRegistro(email, password, password2);

            String codigo = String.valueOf((int) (Math.random() * 1_000_000));

            emailServicio.sendVerificationEmail(email, codigo);

            modelo.put("nombre", nombre);
            modelo.put("telefono", telefono);
            modelo.put("localidad", localidad);
            modelo.put("domicilio", domicilio);
            modelo.put("email", email);
            modelo.put("password", password);
            modelo.put("verificar", codigo);

            return "usuario_ingresarCodigo.html";

        } catch (MiException ex) {

            modelo.put("nombre", nombre);
            modelo.put("telefono", telefono);
            modelo.put("localidad", localidad);
            modelo.put("domicilio", domicilio);
            modelo.put("email", email);
            modelo.put("error", ex.getMessage());

            return "usuario_registrar.html";
        }

    }

    @PostMapping("/verificarCodigo")
    public String verificarCodigo(@RequestParam String codigo, @RequestParam String nombre, @RequestParam String telefono, @RequestParam String localidad,
            @RequestParam String domicilio, @RequestParam String email, @RequestParam String password, @RequestParam String verificar, ModelMap modelo) {

        if (codigo.equals(verificar)) {

            usuarioServicio.crearUsuario(nombre, telefono, localidad, domicilio, email, password);

            Long id = usuarioServicio.buscarUltimo();

            modelo.put("usuario", usuarioServicio.buscarUsuario(id));
            modelo.put("exito", "Usuario Registrado con éxito");

            return "usuario_registrado.html";

        } else {

            modelo.put("error", "El código es incorrecto. Inténtalo de nuevo.");
            modelo.put("nombre", nombre);
            modelo.put("telefono", telefono);
            modelo.put("localidad", localidad);
            modelo.put("domicilio", domicilio);
            modelo.put("email", email);
            modelo.put("password", password);
            modelo.put("verificar", verificar);

            return "usuario_ingresarCodigo.html";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/perfil")
    public String mostrarPerfil(HttpSession session, ModelMap modelo) {

        Usuario logueado = (Usuario) session.getAttribute("usuariosession");

        Integer conductor = viajeServicio.buscarCantidadViajesEjecutadoUsuario(logueado.getIdUsuario());
        Integer viajero = reservaServicio.buscarCantidadConfirmadoViajero(logueado.getIdUsuario());
        boolean flag = false;

        if (conductor == 0 && viajero == 0) {
            flag = true;
        }

        modelo.put("usuario", usuarioServicio.buscarUsuario(logueado.getIdUsuario()));
        modelo.put("conductor", conductor);
        modelo.put("viajero", viajero);
        modelo.put("flag", flag);

        return "usuario_mostrarPerfil.html";

    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/mostrarViajes/{idUsuario}")
    public String mostrarViajesUsuario(@PathVariable Long idUsuario, ModelMap modelo) {

        modelo.addAttribute("viajes", viajeServicio.buscarViajesEjecutadoUsuario(idUsuario));

        return "usuario_mostrarViajes.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/mostrarReservas/{idUsuario}")
    public String mostrarReservasUsuario(@PathVariable Long idUsuario, HttpSession session, ModelMap modelo) {

        Usuario logueado = (Usuario) session.getAttribute("usuariosession");

        modelo.addAttribute("viajes", viajeServicio.buscarViajesEjecutadoUsuarioViajero(idUsuario));
        modelo.put("idUsuario", logueado.getIdUsuario());

        return "usuario_mostrarViajesReserva.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/modificar/{idUsuario}")
    public String modificar(@PathVariable Long idUsuario, ModelMap modelo) {

        modelo.put("usuario", usuarioServicio.buscarUsuario(idUsuario));

        return "usuario_modificar.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("/modifica")
    public String registroUsuario(@RequestParam Long idUsuario, @RequestParam String nombre, @RequestParam String telefono,
            @RequestParam String localidad, @RequestParam String domicilio, @RequestParam String email, ModelMap modelo) {

        try {

            usuarioServicio.modificarUsuario(idUsuario, nombre, telefono, localidad, domicilio, email);

            modelo.put("usuario", usuarioServicio.buscarUsuario(idUsuario));
            modelo.put("exito", "Usuario Modificado con éxito");

            return "usuario_modificado.html";

        } catch (MiException ex) {

            modelo.put("usuario", usuarioServicio.buscarUsuario(idUsuario));
            modelo.put("error", ex.getMessage());

            return "usuario_modificar.html";
        }

    }
}
