package se.lexicon.g48todoapi.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.lexicon.g48todoapi.domain.dto.UserDTOForm;
import se.lexicon.g48todoapi.domain.dto.UserDTOView;
import se.lexicon.g48todoapi.service.UserService;

// TODO: In Postman application, create POST-requests based on this API, to seed the application with some users.

@RequestMapping("/api/v1/users") // http://localhost:8080/api/v1/users
@RestController // RestController annotation is used to create a RESTful web service
public class UserController {

    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public ResponseEntity<UserDTOView> doGetUserEmail(@RequestParam String email){
        System.out.println(" >>>>>>>> doGetUserEmail Method Executed");
        System.out.println("Email requested: " + email);

        UserDTOView dto = userService.getByEmail(email);

        return ResponseEntity.ok(dto);
    }

    @PostMapping("/")
    public ResponseEntity<UserDTOView> doRegister(@RequestBody @Valid UserDTOForm userDTOForm){

        System.out.println(" >>>>>>>> doRegister Method Executed");
        System.out.println("userDTOForm = " + userDTOForm);

        UserDTOView registered = userService.register(userDTOForm);

        return ResponseEntity.status(HttpStatus.CREATED).body(registered);
    }


    @PutMapping("/disable")
    public ResponseEntity<Void> doDisableUserByEmail(@RequestParam String email){
        userService.disableByEmail(email);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/enable")
    public ResponseEntity<Void> doEnableUserByEmail(@RequestParam String email){
        userService.enableByEmail(email);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
