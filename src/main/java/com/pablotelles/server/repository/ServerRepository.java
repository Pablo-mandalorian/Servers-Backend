package com.pablotelles.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pablotelles.server.model.Server;

public interface ServerRepository extends JpaRepository<Server,Long>{
    Server findByIpAddress(String ipAddress);
    Server findByName(String name);
}
