package com.example.common.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.common.pojo.User;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

public class JwtTokenProvider {
    private final String SECRET;
    private final int EXPIRATION_TIME;
    private static JwtTokenProvider tokenProvider;

    public static JwtTokenProvider getInstance() {
        if (tokenProvider == null) {
            tokenProvider = new JwtTokenProvider();
        }
        return tokenProvider;
    }

    private JwtTokenProvider() {
        ClassPathResource resource = new ClassPathResource("token.properties");
        Properties p = new Properties();
        InputStream inputStream;
        try {
            inputStream = resource.getInputStream();
            p.load(inputStream);
            inputStream.close();
        } catch ( IOException e ) {
            throw new IllegalStateException(e.getMessage());
        }

        SECRET = p.getProperty("secret");
        EXPIRATION_TIME = Integer.parseInt(p.getProperty("expirationInMs"));
    }

    public String generateToken(User user) {
        // setting expiration time
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        // generate token
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        return JWT.create()
                .withSubject(user.getId())
                .withIssuedAt(now)
                .withExpiresAt(expiryDate)
                .sign(algorithm);
    }

    public boolean isValid(String token) {
        if (!token.startsWith("Bearer ")) {
            return false;
        }

        // remove 'Bearer '
        token = token.substring(7);

        // verify token
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        JWTVerifier verifier = JWT.require(algorithm).build();
        try {
            verifier.verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getId(String token) {
        // remove 'Bearer '
        token = token.substring(7);

        DecodedJWT jwt = JWT.decode(token);
        return jwt.getSubject();
    }
}
