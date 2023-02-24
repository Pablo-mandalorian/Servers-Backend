package com.pablotelles.server.service;

import java.io.IOException;
import java.util.Collection;

import com.pablotelles.server.model.Server;

public interface ServerService {
    Server create(Server server);
    Server ping(String ipAddress) throws IOException;
    Collection<Server> list(int limit);
    Server getServer(Long id);
    Server update(Server server);
    Boolean delete(Long id);
}
