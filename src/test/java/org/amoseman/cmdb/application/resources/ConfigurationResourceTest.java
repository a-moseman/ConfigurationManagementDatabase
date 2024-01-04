package org.amoseman.cmdb.application.resources;

import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import org.amoseman.cmdb.application.authentication.User;
import org.amoseman.cmdb.application.authentication.UserAuthenticator;
import org.amoseman.cmdb.application.pojo.ConfigurationValue;
import org.amoseman.cmdb.application.pojo.LabelValuePair;
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
            .addResource(new ConfigurationResource(DAO, new MetricRegistry()))
            .build();
    private ConfigurationValue configurationValue;
    private String credential;
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        when(AV.validate("account", "password")).thenReturn(true);

        String time = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        configurationValue = new ConfigurationValue("test", "test", time, time);
        credential = "Basic " + Base64.getEncoder().encodeToString("account:password".getBytes());
        mapper = new ObjectMapper();
    }

    @AfterEach
    void tearDown() {
        reset(DAO);
        reset(AV);
    }

    @Test
    void read() {
        when(DAO.getValue("account", "test")).thenReturn(Optional.of(configurationValue));
        when(DAO.getValue("account", "nonesense")).thenReturn(Optional.empty());

        ConfigurationValue successResponse = EXT
                .target("/cmdb")
                .queryParam("label", "test")
                .request()
                .header(HttpHeaders.AUTHORIZATION, credential)
                .get(ConfigurationValue.class);
        Response failureResponse = EXT
                .target("/cmdb")
                .queryParam("label", "nonesense")
                .request()
                .header(HttpHeaders.AUTHORIZATION, credential)
                .get();

        assertEquals(configurationValue.getValue(), successResponse.getValue());
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), failureResponse.getStatus());

    }


    @Test
    void delete() {
        when(DAO.removeValue("account", "test")).thenReturn(true);
        when(DAO.removeValue("account", "nonesense")).thenReturn(false);

        Response successResponse = EXT
                .target("/cmdb")
                .queryParam("label", "test")
                .request()
                .header(HttpHeaders.AUTHORIZATION, credential)
                .delete();
        Response failureResponse = EXT
                .target("/cmdb")
                .queryParam("label", "nonesense")
                .request()
                .header(HttpHeaders.AUTHORIZATION, credential)
                .delete();

        assertEquals(Response.Status.OK.getStatusCode(), successResponse.getStatus());
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), failureResponse.getStatus());
    }

    @Test
    void update() {
        LabelValuePair successPair = new LabelValuePair("test", "test");
        LabelValuePair failurePair = new LabelValuePair("nonesense", "nonesense");

        when(DAO.setValue("account", successPair.getLabel() , successPair.getValue())).thenReturn(true);
        when(DAO.setValue("account", failurePair.getLabel(), failurePair.getValue())).thenReturn(false);

        Response successResponse = EXT
                .target("/cmdb")
                .request()
                .header(HttpHeaders.AUTHORIZATION, credential)
                .put(Entity.json(successPair));
        Response failureResponse = EXT
                .target("/cmdb")
                .request()
                .header(HttpHeaders.AUTHORIZATION, credential)
                .put(Entity.json(failurePair));

        assertEquals(Response.Status.OK.getStatusCode(), successResponse.getStatus());
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), failureResponse.getStatus());
    }

    @Test
    void create() {
        LabelValuePair successPair = new LabelValuePair("test", "test");
        LabelValuePair failurePair = new LabelValuePair("nonesense", "nonesense");

        when(DAO.addValue("account", successPair.getLabel() , successPair.getValue())).thenReturn(true);
        when(DAO.addValue("account", failurePair.getLabel(), failurePair.getValue())).thenReturn(false);

        Response successResponse = EXT
                .target("/cmdb")
                .request()
                .header(HttpHeaders.AUTHORIZATION, credential)
                .post(Entity.json(successPair));
        Response failureResponse = EXT
                .target("/cmdb")
                .request()
                .header(HttpHeaders.AUTHORIZATION, credential)
                .post(Entity.json(failurePair));

        assertEquals(Response.Status.OK.getStatusCode(), successResponse.getStatus());
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), failureResponse.getStatus());
    }
}