package fr.miage.filestore.auth;

import fr.miage.filestore.auth.entity.Profile;

public interface AuthenticationService {

    String UNAUTHENTIFIED_IDENTIFIER = "anonymous";

    boolean isAuthentified();

    String getConnectedIdentifier();

    Profile getConnectedProfile();

}
