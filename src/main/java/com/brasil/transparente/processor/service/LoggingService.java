package com.brasil.transparente.processor.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class LoggingService {

    public void logInvalidLine(String line) {
        log.error("Linha inválida = {}", line);
    }

    public void logExceptionMainFile(IOException e) {
        log.error("Exceção durante leitura do arquivo principal = {}", String.valueOf(e));
    }

    public void logNumberFormatException(NumberFormatException e) {
        log.error("Formato de número inválido = {} ", String.valueOf(e));
    }

}
