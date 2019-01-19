package ru.mirea.ConfigurationService;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.io.InputStream;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@Controller
@RequestMapping("/*")
public class ConfigurationController {

    @RequestMapping("{filename}")
    public ResponseEntity<String> PropertiesCollector(@PathVariable String filename) {
        // Проверка, что файл начинается с буквы. Допускаются английские названия, точки.
        // Заканчивается всегда на .properties.
        if(!filename.matches("[a-zA-Z]([a-zA-Z.0-9])?+\\.properties"))
            return new ResponseEntity<>(BAD_REQUEST); // Файл не прошёл проверку. Защита от иньекций.
        try(InputStream stream =
                    ConfigurationApplication.class.getResourceAsStream(filename + ".txt")) {
            return new ResponseEntity<>(new String(stream.readAllBytes()), OK);
        } catch (IOException e) {
            return new ResponseEntity<>(NOT_FOUND); // Такого файла не существует.
        }
    }
}
