package com.gdu.wacdo.services;

import com.gdu.wacdo.model.Employe;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class PasswordEncodeur {

    private Pattern BCRYPT_PATTERN = Pattern.compile("^\\$2[ayb]\\$.{56}$");

    private final BCryptPasswordEncoder passwordEncoder;

    public PasswordEncodeur(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = (BCryptPasswordEncoder) passwordEncoder;
    }

    /**
     * Check si le mot de passe est crypté, si il ne l'est pas il le devient.
     */
    public void encrypte(Employe employe) {

        if (employe.getMotDePasse() != null && !employe.getMotDePasse().isEmpty() && !BCRYPT_PATTERN.matcher(employe.getMotDePasse()).matches()) {
            employe.setMotDePasse(passwordEncoder.encode(employe.getMotDePasse()));
        }
    }
}
