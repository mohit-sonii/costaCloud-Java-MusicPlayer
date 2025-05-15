package appolo.project.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/k")
public class TestingController {
    
    @GetMapping
    public ResponseEntity<String> checking(){
        return new ResponseEntity<>("Wroking fine", HttpStatus.OK);
    }
}
