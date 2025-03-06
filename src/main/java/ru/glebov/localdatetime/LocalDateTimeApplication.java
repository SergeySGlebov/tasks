package ru.glebov.localdatetime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;

@SpringBootApplication
public class LocalDateTimeApplication {

    public static void main(String[] args) throws JsonProcessingException {
        SpringApplication.run(LocalDateTimeApplication.class, args);

        LocalDateTime ldt = LocalDateTime.now();
        LocalDateTimeDTO ldtDTO = new LocalDateTimeDTO(ldt);

        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        System.out.println(mapper.writeValueAsString(ldtDTO));

    }

    public static class LocalDateTimeDTO {
        @JsonFormat(pattern = "yyyy:MM:dd:HH:mm:ss:SSS")
        private final LocalDateTime ldt;

        public LocalDateTimeDTO(LocalDateTime ldt) {
            this.ldt = ldt;
        }

        public LocalDateTime getLdt() {
            return ldt;
        }

    }

}
