package com.pablotelles.server.controller;

import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pablotelles.server.enumeration.Status;
import com.pablotelles.server.model.Response;
import com.pablotelles.server.model.Server;
import com.pablotelles.server.service.implementation.ServerServiceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/servers")
@RequiredArgsConstructor
public class ServerController {
    
    private final ServerServiceImpl serverServiceImpl;

    @GetMapping("/list")
    public ResponseEntity<Response> getServers(){
        return ResponseEntity.ok(
                Response.builder()
                .timeStamp(now())
                .data(of("servers", serverServiceImpl.list(30)))
                .message("Servers retrieved")
                .httpStatus(OK)
                .statusCode(OK.value())
                .build()      
        );
    }

    @GetMapping("/ping/{ipAddress}")
    public ResponseEntity<Response> pingServer(@PathVariable("ipAddress") String ipAddress) throws IOException{
        Server server = serverServiceImpl.ping(ipAddress);
        return ResponseEntity.ok(
                Response.builder()
                .timeStamp(now())
                .data(of("server", server))
                .message(server.getStatus() == Status.SERVER_UP ? "Ping Success" : "Ping Failed")
                .httpStatus(OK)
                .statusCode(OK.value())
                .build()      
        );
    }

    @PostMapping("/save")
    public ResponseEntity<Response> saveServer(@RequestBody @Valid Server server) throws IOException{
        return ResponseEntity.ok(
                Response.builder()
                .timeStamp(now())
                .data(of("server", serverServiceImpl.create(server)))
                .message("Server Created")
                .httpStatus(CREATED)
                .statusCode(CREATED.value())
                .build()      
        );
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Response> getServer(@PathVariable("id") Long id){
        return ResponseEntity.ok(
                Response.builder()
                .timeStamp(now())
                .data(of("server", serverServiceImpl.getServer(id)))
                .message("Server Retrieved")
                .httpStatus(OK)
                .statusCode(OK.value())
                .build()      
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteServer(@PathVariable("id") Long id){
        return ResponseEntity.ok(
                Response.builder()
                .timeStamp(now())
                .data(of("deleted", serverServiceImpl.delete(id)))
                .message("Server Deleted")
                .httpStatus(OK)
                .statusCode(OK.value())
                .build()      
        );
    }

    @GetMapping(path ="/image/{fileName}", produces = IMAGE_PNG_VALUE)
    public byte[] getServerImage(@PathVariable("fileName") String fileName) throws IOException {
        return Files.readAllBytes(Paths.get(System.getProperty("user.home") + "/Downloads/images/" + fileName));
    }
    

}
