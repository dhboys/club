package org.dongho.club.security;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
@Log4j2
public class PasswordTests {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void testEncode(){
        // 1111
        // $2a$10$w//BSOK7W1kMb8TZb/lhXOkkSUvTZxHBZzGfvrHxMV4TT4pmUe1zy
        // $2a$10$sO6Q3fBeKazZPFZ5IE/NYe8bLAPL3urSC5I3n.q5qzbRrb/H12CxW

        String password = "1111";
        String enPw = passwordEncoder.encode(password);
        log.info(enPw);
    }
}
