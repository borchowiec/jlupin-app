package com.example.bean.impl;

import com.auth0.jwt.JWT;
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

    @Override
    public String generateToken(User user) {
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
        String secret = properties.getProperty("secret");
        int expirationTime = Integer.parseInt(properties.getProperty("expirationInMs"));

        // setting expiration time
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        // generate token
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
                .withSubject(String.valueOf(user.getId()))
                .withIssuedAt(now)
                .withExpiresAt(expiryDate)
                .sign(algorithm);
    }
}
