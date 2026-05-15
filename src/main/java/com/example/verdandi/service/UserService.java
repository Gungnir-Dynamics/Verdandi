package com.example.verdandi.service;

import com.example.verdandi.model.User;
import com.example.verdandi.repository.UserRepo;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepo repository;

    public UserService (UserRepo repository){
        this.repository = repository;
    }

    public User findUserByEmail(String email){
        return repository.findUserByEmail(email);
    }

    public boolean login(String email, String password){
        User user = repository.findUserByEmail(email);
        if (user != null)
            // CHECKS IF INPUT EXISTS
            return user.getPassword().equals(password);
        // USER NOT FOUND
        return false;
    }


    // ( SKAL MÅSKE LAVES OM TIL PRIVATE)
    //TIDELIGERE OPGAVE LIGGER DEN I CONTROLLER
    public boolean isLoggedIn (HttpSession session) {
        return session.getAttribute("user") != null;
    }


    @Transactional
    public void saveUser(User user){
        repository.saveUser(user);
    }

    public void editProfile (User user){
        repository.editProfile(user);
    }
}
