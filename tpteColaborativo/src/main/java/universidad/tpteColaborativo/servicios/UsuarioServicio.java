
package universidad.tpteColaborativo.servicios;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import universidad.tpteColaborativo.entidades.Calificacion;
import universidad.tpteColaborativo.entidades.Usuario;
import universidad.tpteColaborativo.excepciones.MiException;
import universidad.tpteColaborativo.repositorios.UsuarioRepositorio;

@Service
public class UsuarioServicio implements UserDetailsService {
    
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    
    @Transactional
    public void crearUsuario(String nombre, String telefono, String localidad, String domicilio, 
                             String email, String password, String password2) throws MiException {

        String nombreMay = nombre.toUpperCase();
        String localidadMay = localidad.toUpperCase();
        String domicilioMay = domicilio.toUpperCase();
        
        validarDatosRegistro(email, password, password2);
        
        Usuario user = new Usuario();

        user.setNombre(nombreMay);
        user.setTelefono(telefono);
        user.setLocalidad(localidadMay);
        user.setDomicilio(domicilioMay);
        user.setEmail(email);
        user.setFechaAlta(new Date());
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        user.setRol("USER");
        user.setPuntuacion(3);

        usuarioRepositorio.save(user);

    }
    
    public void modificarUsuario(Long idUsuario, String nombre, String telefono, String localidad, 
            String domicilio, String email) throws MiException{
        
        Usuario usuario = buscarUsuario(idUsuario);
        
        validarDatosModifica(usuario, email);
        
        String nombreMay = nombre.toUpperCase();
        String localidadMay = localidad.toUpperCase();
        String domicilioMay = domicilio.toUpperCase();

        usuario.setNombre(nombreMay);
        usuario.setTelefono(telefono);
        usuario.setLocalidad(localidadMay);
        usuario.setDomicilio(domicilioMay);
        usuario.setEmail(email);
        
        usuarioRepositorio.save(usuario);
        
    }
    
    public Usuario buscarUsuario(Long id) {

        return usuarioRepositorio.getById(id);

    }
    
    public List<Usuario> buscarTodos(){
        
        return usuarioRepositorio.findAll();
        
    }
    
    public Long buscarUltimo() {

        return usuarioRepositorio.ultimoUsuario();
        
    }
    
    public void validarDatosRegistro(String email, String password, String password2) throws MiException{
        
        List<Usuario> lista = usuarioRepositorio.findAll();
        for (Usuario usuario : lista) {
            if (usuario.getEmail().equalsIgnoreCase(email)) {
                throw new MiException("El Email ya está registrado, por favor ingrese otro");
            }
        }
        
        if (!password.equals(password2)) {
            throw new MiException("Las Contraseñas ingresadas deben ser iguales");
        }
        
    }
    
    public void validarDatosModifica(Usuario usuario, String email) throws MiException{
        
        List<Usuario> listaUsuarios = buscarTodos();
        
        if(!usuario.getEmail().equalsIgnoreCase(email)){
        for (Usuario user : listaUsuarios) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                throw new MiException("El Email ya está registrado, por favor ingrese otro");
            }
        }
        }
        
    }
    
    @Transactional
    public void crearUsuarioAdmin(String nombre, String email, String rol, String password, String password2){
        
        String nombreMay = nombre.toUpperCase();
        
        Usuario admin = new Usuario();
        
        admin.setNombre(nombreMay);
        admin.setEmail(email);
        admin.setFechaAlta(new Date());
        admin.setPassword(new BCryptPasswordEncoder().encode(password));
        admin.setRol(rol);
        
        usuarioRepositorio.save(admin);
        
    }
    
    @Transactional
    public void agregarCalificacionConductor(Calificacion calificacion){
        
        Integer puntuacion = 3;
        Integer cantidad = 1;

        Usuario conductor = usuarioRepositorio.getById(calificacion.getUsuarioConductor().getIdUsuario());
        
        List<Calificacion> lista = conductor.getCalificacion();
        
        lista.add(calificacion);
        
        for(Calificacion c : lista){
            cantidad = cantidad + 1;
            puntuacion = puntuacion + c.getPuntuacion();
        }
        
        Integer puntuacionTotal = (int) Math.ceil((double) puntuacion/cantidad);
        
        conductor.setPuntuacion(puntuacionTotal);
        conductor.setCalificacion(lista);
        
        usuarioRepositorio.save(conductor);
        
    }      
    
    @Transactional
    public void agregarCalificacionViajero(Calificacion calificacion){
        
        Integer puntuacion = 3;
        Integer cantidad = 1;
       
        Usuario viajero = usuarioRepositorio.getById(calificacion.getUsuarioViajero().getIdUsuario());
        
        List<Calificacion> lista = viajero.getCalificacion();
        
        lista.add(calificacion);
        
        for(Calificacion c : lista){
            cantidad = cantidad + 1;
            puntuacion = puntuacion + c.getPuntuacion();
        }
        
        Integer puntuacionTotal = (int) Math.ceil((double) puntuacion/cantidad);
        
        viajero.setPuntuacion(puntuacionTotal);
        viajero.setCalificacion(lista);
        
        usuarioRepositorio.save(viajero);
        
    }
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepositorio.buscarUsuarioPorEmail(email);

        if (usuario != null) {

            List<GrantedAuthority> permisos = new ArrayList();

            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());

            permisos.add(p);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

            HttpSession session = attr.getRequest().getSession(true);

            session.setAttribute("usuariosession", usuario);

            return new User(usuario.getEmail(), usuario.getPassword(), permisos);

        } else {

            return null;

        }

    }
    
}
