package com.pablotelles.server.service.implementation;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Collection;
import java.util.Random;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.pablotelles.server.enumeration.Status;
import com.pablotelles.server.model.Server;
import com.pablotelles.server.repository.ServerRepository;
import com.pablotelles.server.service.ServerService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class ServerServiceImpl implements ServerService{

    private final ServerRepository serverRepository;

    //Dependency Injection manual, or @RequiredArgsConstructor with lombok
    public ServerServiceImpl(final ServerRepository serverRepository){
        this.serverRepository = serverRepository;
    }

    @Override
    public Server create(Server server) {
        log.info("Saving new server: {}", server.getName());
        server.setImageUrl(setServerImageUrl());
        return serverRepository.save(server);
    }

    @Override
    public Server ping(String ipAddress) throws IOException {
        log.info("Pinging server: {}", ipAddress);
        Server server = serverRepository.findByIpAddress(ipAddress);
        InetAddress address = InetAddress.getByName(ipAddress);
        server.setStatus(address.isReachable(10000) ? Status.SERVER_UP : Status.SERVER_DOWN);
        serverRepository.save(server);
        return server;
    }

    @Override
    public Collection<Server> list(int limit) {
        log.info("Fetching all servers...");
        return serverRepository.findAll(PageRequest.of(0, limit)).toList();
    }

    @Override
    public Server getServer(Long id) {
        log.info("Fetching server by id: {}", id);
        /*
        Optional<Server> optionaServer = serverRepository.findById(id);
        if(!optionaServer.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Server server = optionaServer.get();
        */
        return serverRepository.findById(id).get();
    }

    @Override
    public Server update(Server server) {
        log.info("Updating server: {}", server.getName());
        return serverRepository.save(server);
    }

    @Override
    public Boolean delete(Long id) {
        log.info("Deleting server by ID: {}", id);
        serverRepository.deleteById(id);
        return true;
    }

    private String setServerImageUrl(){
        String[] imageNames = {"server1.png","server2.png","server3.png","server4.png"};
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/servers/image/"+imageNames[new Random().nextInt(4)]).toUriString();
    }

    private boolean isReachable(String ipAddress, int port, int timeOut){
        try {
            try(Socket socket = new Socket()){
                socket.connect(new InetSocketAddress(ipAddress, port), timeOut);
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
}
