package com.scaler.userservicemwfeve.Services;

import com.scaler.userservicemwfeve.Configurations.BCryptConfiguration;
import com.scaler.userservicemwfeve.Exceptions.IncorrectPasswordException;
import com.scaler.userservicemwfeve.Exceptions.TokenNotExistException;
import com.scaler.userservicemwfeve.Exceptions.UserNotFoundException;
import com.scaler.userservicemwfeve.Repositories.TokenRepository;
import com.scaler.userservicemwfeve.Repositories.UserRepository;
import com.scaler.userservicemwfeve.models.Token;
import com.scaler.userservicemwfeve.models.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private TokenRepository tokenRepository;

    @Autowired
    public UserService(UserRepository userRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenRepository = tokenRepository;
    }

    public Token login(String email, String password) throws UserNotFoundException, IncorrectPasswordException {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isEmpty()) {
            throw new UserNotFoundException("User with email_id "+email+" not found.");
        }
        User user = optionalUser.get();

        if(!bCryptPasswordEncoder.matches(password, user.getHashedPassword())) {
            throw new IncorrectPasswordException("Incorrect password");
        }
        LocalDate today = LocalDate.now();
        LocalDate thirtyDaysLater = today.plus(30, ChronoUnit.DAYS);
        Date expiryDate = Date.from(thirtyDaysLater.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Token token = new Token();
        token.setUser(user);
        token.setExpiryAt(expiryDate);
        token.setValue(RandomStringUtils.randomAlphanumeric(128)); //from library apache commons lang
        return tokenRepository.save(token);
    }

    public User signup(String email, String password, String name) {
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setRoles(new ArrayList<>());
        user.setHashedPassword(bCryptPasswordEncoder.encode(password));
        return userRepository.save(user);
    }

    public void logout(String tokenValue) throws TokenNotExistException {
        Optional<Token> optionalToken = tokenRepository.findByValueAndDeletedEquals(tokenValue,
                false);

        if(optionalToken.isEmpty()) {
            throw new TokenNotExistException("Token with value "+tokenValue+ " do not exist.");
        }
        Token token = optionalToken.get();
        token.setDeleted(true);
        tokenRepository.save(token);
        return;
    }

    public User validateToken(String token) {
        Optional<Token> optionalToken = tokenRepository.findByValueAndDeletedAndExpiryAtGreaterThanEqual(token, false, new Date());

        if(optionalToken.isEmpty()) {
            return null;
        }
        return optionalToken.get().getUser();
    }
}
