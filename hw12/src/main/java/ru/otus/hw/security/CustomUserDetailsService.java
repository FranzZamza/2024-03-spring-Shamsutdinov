package ru.otus.hw.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.otus.hw.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

   // private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        var user = userRepository
                .findByUsername(username)
      //          .map(this::decodePassword)
                .orElseThrow(() -> new UsernameNotFoundException(username));


        return new CustomUserPrincipals(user);
    }

/*    private User decodePassword(User u) {
        var password = passwordEncoder.encode(u.getPassword());
        u.setPassword(password);
        return u;
    }*/
}
