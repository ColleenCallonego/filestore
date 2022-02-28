package fr.miage.filestore.neighbourhood;

import fr.miage.filestore.neighbourhood.entity.Neighbour;

import java.util.List;

public interface NeighbourhoodService {

    List<Neighbour> list() throws NeighbourhoodServiceException;

    boolean isRegistered();

    void checkin();

}
