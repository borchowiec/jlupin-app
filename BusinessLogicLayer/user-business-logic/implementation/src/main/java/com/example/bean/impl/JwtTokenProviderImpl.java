package com.example.bean.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.bean.interfaces.TokenProvider;
import com.example.common.pojo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Properties;

@Component(value = "tokenProvider")
public class JwtTokenProviderImpl implements TokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProviderImpl.class);
    private final String SECRET;
    private final int EXPIRATION_TIME;

    public JwtTokenProviderImpl() {
        // load file with properties
        File root = new File(System.getProperty("user.dir"));
        String propertiesFilePath = root.getAbsolutePath() + File.separator + "application.properties";
        InputStream inputStream;
        try {
            inputStream = Files.newInputStream(Paths.get(propertiesFilePath));
        } catch (IOException e) {
            throw new IllegalStateException("Cannot find " + propertiesFilePath + " file.");
        }

        // load properties
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot load properties from file " + propertiesFilePath + ".");
        }

        // get properties
        SECRET = properties.getProperty("secret");
        EXPIRATION_TIME = Integer.parseInt(properties.getProperty("expirationInMs"));
    }

    @Override
    public String generateToken(User user) {
        // setting expiration time
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        // generate token
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        return JWT.create()
                .withSubject(String.valueOf(user.getId()))
                .withIssuedAt(now)
                .withExpiresAt(expiryDate)
                .sign(algorithm);
    }

    @Override
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
}
