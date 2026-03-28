package com.secureauth.system;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = {
        "security.jwt.secret=VGhpc0lzQVRlc3RCYXNlNjRTZWNyZXRLZXlGb3JKV1QxMjM0NTY3ODkwMTIzNDU2",
        "spring.datasource.url=jdbc:h2:mem:testdb;MODE=MySQL;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=password",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@ActiveProfiles("test")
class SecureAuthSystemApplicationTests {

    @Test
    void contextLoads() {
    }
}
