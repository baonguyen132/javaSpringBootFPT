package net.codejava.Application.services;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import net.codejava.Application.dto.request.AuthenticationRequest;
import net.codejava.Application.dto.request.IntrospectRequest;
import net.codejava.Application.dto.response.AuthenticationResponse;
import net.codejava.Application.dto.response.IntrospectResponse;
import net.codejava.Application.entity.User;
import net.codejava.Application.exception.AppException;
import net.codejava.Application.exception.ErrorCode;
import net.codejava.Application.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
@RequiredArgsConstructor
public class AuthenticationService
{
    UserRepository userRepository ;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                                 .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated =  passwordEncoder.matches(request.getPassword() , user.getPassword()) ;
        if(!authenticated) throw  new AppException(ErrorCode.UN_AUTHENTICATED);

        String token = generateToken(user.getUsername()) ;



        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build() ;

    }

    public IntrospectResponse introspect(IntrospectRequest request) {
        String token = request.getToken() ;

        try {
            JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes()) ;
            SignedJWT signedJWT = SignedJWT.parse(token) ;

            Date expityTime = signedJWT.getJWTClaimsSet().getExpirationTime() ;


            var verified = signedJWT.verify(verifier) ;

            return  IntrospectResponse.builder().valid(verified && expityTime.after(new Date())).build();
        } catch (JOSEException | ParseException e) {
            throw new RuntimeException(e);
        }

    }

    private String generateToken(String username) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512) ;

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issuer("xxx.xxx")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1 , ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("x" , "xxx")
                .build() ;

        Payload payload = new Payload(jwtClaimsSet.toJSONObject()) ;

        JWSObject jwsObject = new JWSObject(header , payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize() ;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
