
package universidad.tpteColaborativo.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServicio {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailServicio(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(String correo, String codigo) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(correo);
        message.setSubject("Transporte Colaborativo: Código de Verificación");
        message.setText("Debes verificar tu dirección de correo electrónico para comenzar a usar "
                + "tu cuenta de Transaporte Colaborativo. Vuelve a la aplicación e introduce el siguiente código: " + codigo);
        message.setFrom("tptecolaborativo@gmail.com");
        mailSender.send(message);
    }

}
