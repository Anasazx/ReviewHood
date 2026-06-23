package tn.anasazx.tunirate.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tn.anasazx.tunirate.enums.GlobalRole;
import tn.anasazx.tunirate.user.entity.User;
import tn.anasazx.tunirate.user.repository.UserRepository;


import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = authHeader.substring(7);
        if (!jwtService.isTokenValid(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        //Extract USER ID
        Long currentUserId = jwtService.extractUserId(token);

        //Extract Role
        GlobalRole role = extractUserRole(currentUserId);

        List<GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        currentUserId,
                        null,
                        authorities
                );

        authToken.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );
        SecurityContextHolder.getContext().setAuthentication(authToken);


        filterChain.doFilter(request, response);

    }

    GlobalRole extractUserRole(Long currentUserId){
        User user = userRepository.findFirstById(currentUserId).orElseThrow(()-> new RuntimeException("User Not Found"));
        return user.getGlobalRole();
    }

}