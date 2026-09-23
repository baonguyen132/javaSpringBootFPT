package net.codejava.Application.services;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import net.codejava.Application.dto.request.AuthenticationRequest;
import net.codejava.Application.dto.response.AuthenticationResponse;
import net.codejava.Application.entity.User;
import net.codejava.Application.exception.AppException;
import net.codejava.Application.exception.ErrorCode;
import net.codejava.Application.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    protected  static final String SIGNER_KEY = "yzKGm9S4n5T2VC0HaJ4IGcz1LWlI90FhCWAtw3vST7Z7zoASXzMMMvnlVdTYFM8lSLyz75jjAXYfHWfzSnHcckutdyGMsxHAtt30fzk2yPiRNnDfE7B2zTfewW2Uj8hqPVRB5jXA4LXOTUx0Q71UQf0DeFHDGNnTjgeoWYnetvMYEeJAPjKBIdzTmBez2kK22BLfrKE3w4xASDfbDgRpXjFWsRKG5hp4m3ZVRTCqAzOYJbUwzHqqPzMoBGAs1rRK" ;

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
