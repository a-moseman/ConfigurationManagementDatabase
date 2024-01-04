package org.amoseman.cmdb.application.resources;

import com.codahale.metrics.MetricRegistry;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import org.amoseman.cmdb.application.authentication.User;
import org.amoseman.cmdb.application.authentication.UserAuthenticator;
import org.amoseman.cmdb.application.configuration.ConfigurationValue;
import org.amoseman.cmdb.dao.ConfigurationDAO;
import org.amoseman.cmdb.security.AccountValidator;
import org.glassfish.jersey.test.grizzly.GrizzlyTestContainerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(DropwizardExtensionsSupport.class)
class ConfigurationResourceTest {
    private static final ConfigurationDAO DAO = mock(ConfigurationDAO.class);
    private static final AccountValidator AV = mock(AccountValidator.class);
    private static final ResourceExtension EXT = ResourceExtension.builder()
            .setTestContainerFactory(new GrizzlyTestContainerFactory())
            .addProvider(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<User>()
                    .setAuthenticator(new UserAuthenticator(AV))
                    .setRealm("BASIC-AUTH-REALM")
                    .buildAuthFilter()
            ))
            .addProvider(new AuthValueFactoryProvider.Binder<>(User.class))
            .addResource(new ConfigurationResource(DAO, "none", new MetricRegistry()))
            .build();
    private ConfigurationValue configurationValue;
    private String credential;

    @BeforeEach
    void setUp() {
        when(AV.validate("account", "password")).thenReturn(true);

        String time = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        configurationValue = new ConfigurationValue("test", time, time);
        credential = "Basic " + Base64.getEncoder().encodeToString("account:password".getBytes());
    }

    @AfterEach
    void tearDown() {
        reset(DAO);
        reset(AV);
    }

    @Test
    void readSuccess() {
        when(DAO.getValue("account", "test")).thenReturn(Optional.of(configurationValue));
        ConfigurationValue response = EXT
                .target("/cmdb")
                .queryParam("label", "test")
                .request()
                .header(HttpHeaders.AUTHORIZATION, credential)
                .get(ConfigurationValue.class);
        assertEquals(configurationValue.getContent(), response.getContent());
        verify(DAO).getValue("account", "test");
    }

    @Test
    void readFail() {
        when(DAO.getValue("account", "test")).thenReturn(Optional.of(configurationValue));
        ConfigurationValue response = EXT
                .target("/cmdb")
                .queryParam("label", "nonesense")
                .request()
                .header(HttpHeaders.AUTHORIZATION, credential)
                .get(ConfigurationValue.class);
        assertEquals("none", response.getContent());
        verify(DAO).getValue("account", "nonesense");
    }


    @Test
    void delete() {
        when(DAO.removeValue("account", "test")).thenReturn(true);
        Response response = EXT
                .target("/cmdb")
                .queryParam("label", "test")
                .queryParam("value", "test")
                .request()
                .header(HttpHeaders.AUTHORIZATION, credential)
                .delete();
        assertEquals(200, response.getStatus());
    }
}