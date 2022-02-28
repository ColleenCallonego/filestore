package fr.miage.filestore.auth;

import fr.miage.filestore.auth.entity.Profile;
import fr.miage.filestore.config.FileStoreConfig;
import org.wildfly.security.auth.server.SecurityDomain;
import org.wildfly.security.auth.server.SecurityIdentity;
import org.wildfly.security.http.oidc.AccessToken;
import org.wildfly.security.http.oidc.OidcPrincipal;
import org.wildfly.security.http.oidc.OidcSecurityContext;
import org.wildfly.security.http.oidc.RefreshableOidcSecurityContext;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class AuthenticationServiceBean implements AuthenticationService {

    private static final Logger LOGGER = Logger.getLogger(AuthenticationService.class.getName());

    @Resource
    private SessionContext sessionContext;

    @Inject
    private FileStoreConfig config;

    private static Map<String, Profile> profilesCache = new HashMap<>();

    @Override
    public boolean isAuthentified() {
        return !sessionContext.getCallerPrincipal().getName().equals(UNAUTHENTIFIED_IDENTIFIER);
    }

    @Override
    public String getConnectedIdentifier() {
        return sessionContext.getCallerPrincipal().getName();
    }

    @Override
    public Profile getConnectedProfile() {
        LOGGER.log(Level.INFO, "Getting connected profile");
        String connectedId = getConnectedIdentifier();
        Profile profile;
        if ( profilesCache.containsKey(connectedId) ) {
            profile = profilesCache.get(connectedId);
        } else {
            profile = new Profile();
            profile.setId(connectedId);
            profilesCache.put(connectedId, profile);
        }
        try {
            OidcPrincipal<RefreshableOidcSecurityContext> principal = (OidcPrincipal<RefreshableOidcSecurityContext>) SecurityDomain.getCurrent().getCurrentSecurityIdentity().getPrincipal();
            AccessToken token = principal.getOidcSecurityContext().getToken();
            profile.setUsername(token.getClaimValueAsString("preferred_username"));
            if ( profile.getUsername().equals(config.getOwner()) ) {
                LOGGER.log(Level.INFO, "User is owner: " + profile.getUsername());
                profile.setOwner(true);
            } else {
                LOGGER.log(Level.INFO, "User is NOT owner: " + profile.getUsername());
                profile.setOwner(false);
            }
            profile.setFullname(token.getClaimValueAsString("given_name") + " " + token.getClaimValueAsString("family_name"));
            profile.setEmail(token.getClaimValueAsString("email"));
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return profile;
    }

}
