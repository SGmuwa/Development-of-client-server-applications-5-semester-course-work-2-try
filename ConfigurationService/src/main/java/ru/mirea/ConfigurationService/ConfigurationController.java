package ru.mirea.ConfigurationService;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileInputStream;
import java.io.InputStream;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.web.bind.annotation.RequestMethod.GET;


@Controller
@RequestMapping("/manual/*")
public class ConfigurationController {

    @RequestMapping(value = "{filename}", method = GET)
    @ResponseBody
    public ResponseEntity<String> PropertiesCollector(@PathVariable String filename) {
        // Проверка, что файл начинается с буквы. Допускаются английские названия, точки.
        // Заканчивается всегда на .properties.
        if(!filename.matches("[a-zA-Z]([a-zA-Z.0-9]+)?\\.properties"))
            return new ResponseEntity<>(BAD_REQUEST); // Файл не прошёл проверку. Защита от иньекций.
        try(InputStream stream =
                    new FileInputStream(new ClassPathXmlApplicationContext().getResource(filename + ".txt").getFile())) {
            String content = new String(stream.readAllBytes());
            return ResponseEntity.ok()
                    .contentLength(content.length())
                    .contentType(MediaType.TEXT_EVENT_STREAM)
                    .body(content);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), NOT_FOUND); // Такого файла не существует.
        }
    }
}
