package com.example.ddcabe.Service;


import com.example.ddcabe.User.User;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;

@Service
public class JWTAuthenticationService {

    private static final Logger LOG = LoggerFactory.getLogger(JWTAuthenticationService.class);


    static final long JWT_EXPIRATION = 864_000_00; // equal 1 day in milliseconds
    static final String JWT_SECRET = "ZGRjYQ==";


    static final String PREFIX = "Bearer";

    //Currently this function is not in use
    public void addToken(HttpServletResponse res, String email){

        String JwtToken = Jwts.builder().setSubject(email)
                //Set the expiration time, add 1 day time
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET) // changed from HS512 to HS256 makes the application works ? will look more in to this
                .compact();

        res.addHeader("Authorization", PREFIX + " " + JwtToken);

        res.addHeader("Access-Control-Expose-Headers", "Authorization");

    }


    //Function to check if the incoming header has the token or not
    public String extractTokenStringFromHeader(HttpServletRequest request){
        String requestHeader = request.getHeader("Authorization"); //Authorization is a constant naming convention
        //Split the bearer and the tokenID

        LOG.info("requestHeader: " + requestHeader);
        try {
            String[] headerElement = requestHeader.split(" ");
            LOG.info(Arrays.toString(headerElement));
            //Get the token
            return headerElement[1];
        }
        catch (NullPointerException e){
            return null;
        }

    }

    public String generateToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);


        //We need to use the decoded secret for the signing, and JAXB API » 2.4.0-b180830.0359 library
        String base64EncodedSecret = "ZGRjYQ==";
        byte[] decodedSecret = Base64.getDecoder().decode(base64EncodedSecret);

        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .setClaims(Map.of("username", user.getUsername(), "password", user.getPassword(),"role",user.getRole(),"session",user.getSessions()))
                .signWith(HS256,decodedSecret)
                .compact();
    }

    //Check if the token from the front end is valid
    public boolean isValidToken(String token) throws Exception {
        try {
            LOG.info("inside isValidToken, checking token string: " + token);
            LOG.info("TOKEN RECEIVED: " + token);
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            LOG.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            LOG.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            LOG.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            LOG.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            LOG.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }


}