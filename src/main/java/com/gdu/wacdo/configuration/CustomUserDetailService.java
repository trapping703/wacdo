package com.gdu.wacdo.configuration;

import com.gdu.wacdo.repositories.EmployeRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final EmployeRepository employeRepository;

    public CustomUserDetailService(EmployeRepository employeRepository) {
        this.employeRepository = employeRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return employeRepository.findByEmail(username).map(employe -> User
                        .builder()
                        .username(employe.getEmail())
                        .password(employe.getMotDePasse())
                        .authorities(employe.isAdmin() ? "ROLE_ADMIN" : "ROLE_SER")
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
