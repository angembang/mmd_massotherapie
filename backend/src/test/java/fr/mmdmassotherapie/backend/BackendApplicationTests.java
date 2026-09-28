package fr.mmdmassotherapie.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(properties = {
		"spring.datasource.url=jdbc:h2:mem:mmd_test;MODE=MySQL;DB_CLOSE_DELAY=-1",
		"spring.datasource.driver-class-name=org.h2.Driver",
		"spring.datasource.username=sa",
		"spring.datasource.password=",
		"spring.jpa.hibernate.ddl-auto=create-drop",
		"spring.flyway.enabled=false",
		"app.security.admin.registration-invitation-code=test-admin-invitation-code",
		"app.booking.public-token.secret=${BOOKING_PUBLIC_TOKEN_SECRET:MDEyMzQ1Njc4OWFiY2RlZjAxMjM0NTY3ODlhYmNkZWY=}",
		"app.booking.public-token.expiration-days=90",
		"app.booking.management-base-url=http://localhost:4200",
		"app.booking.slot-step-minutes=15"
})
class BackendApplicationTests {

	@Test
	void contextLoads() {
	}

}
