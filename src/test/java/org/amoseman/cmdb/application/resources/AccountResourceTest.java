package org.amoseman.cmdb.application.resources;

import com.codahale.metrics.MetricRegistry;
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
import org.amoseman.cmdb.application.pojo.Account;
import org.amoseman.cmdb.dao.AccountDAO;
import org.amoseman.cmdb.dao.ConfigurationDAO;
import org.amoseman.cmdb.security.AccountValidator;
import org.glassfish.jersey.test.grizzly.GrizzlyTestContainerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.reset;

@ExtendWith(DropwizardExtensionsSupport.class)
class AccountResourceTest {
    private static final AccountDAO DAO = mock(AccountDAO.class);
    private static final AccountValidator AV = mock(AccountValidator.class);
    private static final ResourceExtension EXT = ResourceExtension.builder()
            .setTestContainerFactory(new GrizzlyTestContainerFactory())
            .addProvider(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<User>()
                    .setAuthenticator(new UserAuthenticator(AV))
                    .setRealm("BASIC-AUTH-REALM")
                    .buildAuthFilter()
            ))
            .addProvider(new AuthValueFactoryProvider.Binder<>(User.class))
            .addResource(new AccountResource(DAO, new MetricRegistry()))
            .build();
    private String credential;

    @BeforeEach
    void setUp() {
        when(AV.validate("account", "password")).thenReturn(true);
        credential = "Basic " + Base64.getEncoder().encodeToString("account:password".getBytes());
    }

    @AfterEach
    void tearDown() {
        reset(DAO);
        reset(AV);
    }

    @Test
    void addAccount() {
        Account successAccount = new Account("a", "a");
        Account failureAccount = new Account("b", "b");

        when(DAO.addAccount(successAccount.getName(), successAccount.getPass())).thenReturn(true);
        when(DAO.addAccount(failureAccount.getName(), failureAccount.getPass())).thenReturn(false);

        Response successResponse = EXT
                .target("/account")
                .request()
                .post(Entity.json(successAccount));
        Response failureResponse = EXT
                .target("/account")
                .request()
                .post(Entity.json(failureAccount));

        assertEquals(Response.Status.OK.getStatusCode(), successResponse.getStatus());
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), failureResponse.getStatus());
    }

    @Test
    void removeAccount() {
        String badCredentials = "Basic " + Base64.getEncoder().encodeToString("nonesense:password".getBytes());

        when(DAO.deleteAccount("account")).thenReturn(true);
        when(DAO.deleteAccount("nonesense")).thenReturn(false);

        Response successResponse = EXT
                .target("/account")
                .request()
                .header(HttpHeaders.AUTHORIZATION, credential)
                .delete();
        Response failureResponse = EXT
                .target("/account")
                .request()
                .header(HttpHeaders.AUTHORIZATION, badCredentials)
                .delete();

        assertEquals(Response.Status.OK.getStatusCode(), successResponse.getStatus());
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), failureResponse.getStatus());
    }
}