package com.kurt.gym.infrastructure.jwt;

import java.io.Serializable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.kurt.gym.auth.model.services.user.UserRepository;
import com.kurt.gym.auth.model.user.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import java.util.*;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class Jwt implements Serializable {

//    @Value("${secret.key}")
    private String SECRET_KEY = "secret";
    public final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

    private final UserRepository userRepository;

    public User getUserInToken(String token) {
        Long id = getClaims(token).get("userId", Long.class);

        return userRepository.getReferenceById(id);
    }

    public String getUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date getExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    public Boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }

    public String generateToken(User user, boolean expiry) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("role", user.getRole());

        if (expiry)
            return createToken(claims, user.getEmail());
        else
            return createTokenNoExpiry(claims, user.getEmail());
    }

    public String generateTokenResetPassword(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        return createTokenReset(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claim(subject, subject)
                .addClaims(claims)
                .setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY).compact();
    }

    private String createTokenReset(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY).compact();
    }

    private String createTokenNoExpiry(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsername(token);
        return (username.equals(userDetails.getUsername()));
    }

    public Boolean removeToken(String token) {
        Claims claims = extractAllClaims(token);
        claims.setSubject(null);
        claims.clear();
        return true;
    }

    public Date extendDay(int day) {
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DATE, day);

        return calendar.getTime();
    }

    private Date modifyDateToday(int day) {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, day);
        return cal.getTime();
    }

    private void removeToken() {
        // OAuth2Authentication authentication =
        // super.readAuthentication(token.getValue());
        // String username = authentication.getUserAuthentication().getName();
        // User user = userRepository.findByEmail(username);
        // user.setToken(null);
        // userRepository.save(user);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token).getBody();
    }

}
