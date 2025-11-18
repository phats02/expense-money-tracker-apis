package com.ferb.expenseMoneyTracker.service;

import com.ferb.expenseMoneyTracker.dto.CustomUserDetail;
import com.ferb.expenseMoneyTracker.entity.User;
import com.ferb.expenseMoneyTracker.repository.UserRepository;
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
        User user=  userRepository.findOneByEmail(email);
        if (user == null) {
            throw  new UsernameNotFoundException(email);
        }
        return new CustomUserDetail(user);
    }

    public User findOneUserByEmail(String email) {
        return userRepository.findOneByEmail(email);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public boolean isEmailExist(String email) {
        return userRepository.existsByName(email);
    }
}
