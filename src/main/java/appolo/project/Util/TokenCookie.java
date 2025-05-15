package appolo.project.Util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Component
public class TokenCookie {
    // this will take the value from application.properties and place it in the secret_key variable
    @Value("${jwt.secret.user}")
    private String secret_key_user;
    private SecretKey keyUser;

    @Value("${jwt.secret.admin}")
    private String secret_user_admin;
    private SecretKey keyAdmin;


    // this method is responsible for creating a crypotographic string from our secret key which is a string and now this is become the major key that can be used to create and validate tokens.
    // PostConstruct is used to run the mentioned method on the time of initialisation of object.

    @PostConstruct
    public void initKey() {
        keyUser = Keys.hmacShaKeyFor(secret_key_user.getBytes(StandardCharsets.UTF_8));
        keyAdmin = Keys.hmacShaKeyFor(secret_user_admin.getBytes(StandardCharsets.UTF_8));
    }

    // validdate the token
    public String[] tokenValidation(String token, String role) {
        try {
            if (role.equals("USER")) {
                Jwts.parserBuilder().setSigningKey(keyUser).build().parseClaimsJws(token);
                return new String[]{"true", "USER"};
            } else if (role.equals("ADMIN")) {
                Jwts.parserBuilder().setSigningKey(keyAdmin).build().parseClaimsJws(token);
                return new String[]{"true", "ADMIN"};
            } else {
                return new String[]{"false", "undefined"};
            }
        } catch (JwtException | IllegalArgumentException e) {
            return new String[]{"false", "undefined"};
        }
    }

    // generate the cookie and send it with response
    public void generateCookie(String token, HttpServletResponse response) {
        Cookie cooki = new Cookie("auth_for_sec", token);
        cooki.setMaxAge(60 * 60 * 10);
        cooki.setHttpOnly(true);
        cooki.setSecure(false);
        cooki.setPath("/");
        response.addCookie(cooki);
    }

    public void clearCookie(HttpServletResponse response){
        Cookie cooki=new Cookie("auth_for_sec","");
        cooki.setMaxAge(0);
        cooki.setPath("/");
        response.addCookie(cooki);
    }

    // get the data from cookie
    public Claims getClaims(String token, String role) {
        return Jwts.parserBuilder().setSigningKey(role.equals("USER") ? keyUser : keyAdmin).build().parseClaimsJws(token).getBody();
    }
    // generate the token with the id and the role which is used to validate the user whether it is on right track or not.

    public String generateToken(String username, UUID id, String role) {
        try {
            return Jwts
                    .builder()
                    .setSubject(username)
                    .claim("role", role)
                    .claim("id", id)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                    .signWith(role.equals("USER") ? keyUser : keyAdmin, SignatureAlgorithm.HS256)
                    .compact();
        } catch (Exception e) {
            return "Incorrect Role Field";
        }
    }
}
