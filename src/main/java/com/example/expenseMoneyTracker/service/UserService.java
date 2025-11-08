package com.example.expenseMoneyTracker.service;

import com.example.expenseMoneyTracker.dto.CustomUserDetail;
import com.example.expenseMoneyTracker.entity.User;
import com.example.expenseMoneyTracker.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user=  userRepository.findByEmail(email);
        if (user == null) {
            throw  new UsernameNotFoundException(email);
        }
        return new CustomUserDetail(user);
    }
}
