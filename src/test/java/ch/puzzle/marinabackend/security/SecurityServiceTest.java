package ch.puzzle.marinabackend.security;

import ch.puzzle.marinabackend.KeycloakRolesExtractor;
import ch.puzzle.marinabackend.MarinaBackendApplication;
import ch.puzzle.marinabackend.TestConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MarinaBackendApplication.class, TestConfiguration.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(secure = true)
@ActiveProfiles("test")
public class SecurityServiceTest {

    @MockBean
    private KeycloakRolesExtractor rolesExtractor;

    @Autowired
    private SecurityService service;
    
    private OAuth2Authentication principal;

    @Before
    public void setup() {
        principal = SecurityTestUtils.getTestOAuth2Authentication();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldCastPrincipal() {
        // given
        HashMap<String, String> details = (HashMap<String, String>)
                principal.getUserAuthentication().getDetails();

        // when
        User u = service.convertPrincipal(principal);

        // then
        assertEquals(details.get("preferred_username"), u.getUsername());
        assertEquals(details.get("sub"), u.getId());
        assertEquals(details.get("given_name"), u.getFirstName());
        assertEquals(details.get("family_name"), u.getLastName());
        assertEquals(details.get("email"), u.getEmail());
        assertEquals("baererToken", u.getBearerToken());
    }

    @Test
    public void shouldNotCastPrincipal() {
        // given
        Authentication p = mock(Authentication.class);

        // when
        User u = service.convertPrincipal(p);

        // then
        assertNull(u);

    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldCastPrincipalWithoutBearerToken() {
        // given
        WebAuthenticationDetails authDetails = mock(WebAuthenticationDetails.class);
        when(principal.getDetails()).thenReturn(authDetails);
        HashMap<String, String> details = (HashMap<String, String>)
                principal.getUserAuthentication().getDetails();

        // when
        User u = service.convertPrincipal(principal);

        // then
        assertEquals(details.get("preferred_username"), u.getUsername());
        assertEquals(details.get("sub"), u.getId());
        assertEquals(details.get("given_name"), u.getFirstName());
        assertEquals(details.get("family_name"), u.getLastName());
        assertEquals(details.get("email"), u.getEmail());
        assertNull(u.getBearerToken());
    }

    @Test
    public void shouldCastPrincipalOnlyBearerToken() {
        // given
        Authentication a = mock(AnonymousAuthenticationToken.class);
        when(principal.getUserAuthentication()).thenReturn(a);

        // when
        User u = service.convertPrincipal(principal);

        // then
        assertNull(u.getUsername());
        assertNull(u.getId());
        assertNull(u.getFirstName());
        assertNull(u.getLastName());
        assertNull(u.getEmail());
        assertEquals("baererToken", u.getBearerToken());
    }

    @Test
    public void shouldCastPrincipalOnlyBearerToken_UserPasswordToken() {
        // given
        when(principal.getUserAuthentication().getDetails()).thenReturn(new Object());


        // when
        User u = service.convertPrincipal(principal);

        // then
        assertNull(u.getUsername());
        assertNull(u.getId());
        assertNull(u.getFirstName());
        assertNull(u.getLastName());
        assertNull(u.getEmail());
        assertEquals("baererToken", u.getBearerToken());
    }

}
