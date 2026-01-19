package gr.hua.dit.fittrack.core.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * JWT authentication filter για το FitTrack.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        if (jwtService == null) throw new NullPointerException("jwtService is null");
        this.jwtService = jwtService;
    }

    private void writeError(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"invalid_token\"}");
    }

    /**
     * Ποια paths ΔΕΝ θα περνάνε από το JWT φίλτρο.
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();


        if (path.startsWith("/api/auth")) {
            return true;
        }


        return !path.startsWith("/api");
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws IOException, ServletException {

        // Παίρνει το Authorization header
        String authorizationHeader = request.getHeader("Authorization");

        // Αν δεν υπάρχει Bearer token → συνέχισε
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.substring(7);

        try {

            Claims claims = this.jwtService.parse(token);
            String subject = claims.getSubject(); // π.χ. email

            Collection<String> roles = (Collection<String>) claims.get("roles");


            List<GrantedAuthority> authorities = new ArrayList<>();
            if (roles != null) {
                for (String role : roles) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
                }
            }

            // Δημιουργία authenticated χρήστη
            User principal = new User(subject, "", authorities);

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(principal, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        } catch (Exception ex) {
            // Άκυρο token
            LOGGER.warn("JwtAuthenticationFilter failed", ex);
            SecurityContextHolder.clearContext();
            throw new BadCredentialsException("Invalid token");

        }


        filterChain.doFilter(request, response);
    }
}
