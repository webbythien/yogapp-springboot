package com.swp.yogaapp.controller;

import com.swp.yogaapp.jwt.JwtTokenProvider;
import com.swp.yogaapp.model.LoginDTO;
import com.swp.yogaapp.repository.AccountRepository;
import com.swp.yogaapp.model.Account;
import com.swp.yogaapp.repository.SearchRepository;
import io.swagger.annotations.Authorization;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
public class AccountControllers {
    @Autowired
    AccountRepository repo;

    @Autowired
    SearchRepository srepo;


    @Autowired
    private JwtTokenProvider tokenProvider;

    @ApiIgnore
    @RequestMapping(value="/")
    public void redirect(HttpServletResponse response) throws IOException {
        response.sendRedirect("/swagger-ui.html");
    }
    @GetMapping(value = "/accounts")
    public List<Account> getAllAccount(){
    return repo.findAll();
    }

    @GetMapping(value = "/accounts/{email}")
    public Account getAccountByEmail(@PathVariable String email){
        return srepo.findByEmail(email);
    }

    @PostMapping(value = "/account/token")
    public ResponseEntity<?> decodeToken(@RequestHeader("Authorization") String Authorization ,HttpServletRequest request){
        try{
            //`Bearer Token`
            String token = tokenProvider.getJwtFromRequest(request);
            Object userToken = tokenProvider.getUserFromJWT(token);
            return new ResponseEntity<>(userToken, HttpStatus.OK);
        }catch (Exception error){
            return new ResponseEntity<>("Authenticate Fail", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/account/register")
    public ResponseEntity<String> addAccount(@RequestBody Account account, HttpServletResponse res) throws IOException {
        try {
            if(!account.getPassword().isEmpty()){
                String password = account.getPassword();
                String hash = BCrypt.hashpw(password,BCrypt.gensalt(10));

                Date date = new Date();
                account.setPassword(hash);
                account.setStatus(true);
                account.setRole("user");
                account.setCreatedAt(date);
                account.setUpdatedAt(date);

                repo.save(account);
                return new ResponseEntity<>("User signed-up successfully!.", HttpStatus.OK);
            }else {
                return new ResponseEntity<>("Password is Required", HttpStatus.BAD_REQUEST);
            }
        }catch (Exception error){
            return new ResponseEntity<>("Email or Password is required", HttpStatus.BAD_REQUEST);

        }
    }

    @PostMapping(value = "account/login")
    public ResponseEntity<String> Login(@RequestBody LoginDTO loginDto, HttpServletResponse res) throws IOException {
        try {
            if(!loginDto.getEmail().isEmpty()){

                Account account=srepo.findByEmail(loginDto.getEmail());
                if(account!=null){
                    Boolean checkPass = BCrypt.checkpw(loginDto.getPassword(),account.getPassword());
                    if(checkPass){
                        //jwt
                        String jwt = tokenProvider.generateToken(account);
                        System.out.println(jwt);
                        return new ResponseEntity<>(jwt, HttpStatus.OK);

                    }else{
                        return new ResponseEntity<>("Password is not match", HttpStatus.BAD_REQUEST);
                    }
                }else{
                    return new ResponseEntity<>("Email is not existed", HttpStatus.BAD_REQUEST);
                }
            }else{
                return new ResponseEntity<>("Email is not existed", HttpStatus.BAD_REQUEST);
            }
        }catch (Exception error){
            return new ResponseEntity<>("Email or Password is not match", HttpStatus.BAD_REQUEST);
        }
    }
}
