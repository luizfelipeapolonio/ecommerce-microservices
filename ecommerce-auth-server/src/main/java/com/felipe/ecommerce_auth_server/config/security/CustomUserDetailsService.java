package com.felipe.ecommerce_auth_server.config.security;

import com.felipe.ecommerce_auth_server.persistence.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
  private final UserRepository userRepository;

  public CustomUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return this.userRepository.findByEmail(username)
      .map(UserPrincipal::new)
      .orElseThrow(() -> new UsernameNotFoundException("Usuário com e-mail: '" + username + "' não econtrado"));
  }
}
