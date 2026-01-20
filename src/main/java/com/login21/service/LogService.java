package com.login21.service;

import com.login21.entity.Log;
import com.login21.repository.LogRepository;
import org.springframework.stereotype.Service;

@Service
public class LogService {

    private final LogRepository logRepository;

    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public void log(
            Integer idUser,
            String action,
            String entity,
            Integer idEntity,
            String description,
            String ipAddress
    ) {
        Log log = new Log();
        log.setIdUser(idUser);
        log.setAction(action);
        log.setEntity(entity);
        log.setIdEntity(idEntity);
        log.setDescription(description);
        log.setIpAddress(ipAddress);

        logRepository.save(log);
    }
}
