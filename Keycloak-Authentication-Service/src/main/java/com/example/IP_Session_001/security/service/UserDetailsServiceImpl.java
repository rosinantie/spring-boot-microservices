package com.example.IP_Session_001.security.service;

import com.example.IP_Session_001.entity.UserLogin;
import com.example.IP_Session_001.repository.UserLoginRepository;
import com.example.IP_Session_001.security.user.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserLoginRepository userLoginRepository;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserLogin userLogin = userLoginRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));
        log.info("userLogin from loadUserByUsername:{}", userLogin);
        return UserDetailsImpl.build(userLogin);
    }
}
