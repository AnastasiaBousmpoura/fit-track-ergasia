package gr.hua.dit.fittrack.core.service;

import gr.hua.dit.fittrack.core.service.impl.dto.RegisterUserRequest;
import gr.hua.dit.fittrack.core.service.impl.dto.LoginRequest;
import gr.hua.dit.fittrack.core.service.impl.dto.LoginResult;

public interface AuthService {
    //Καταχωρεί νέο χρήστη ή trainer.
    void registerUser(RegisterUserRequest request);

    //Εκτελεί login
    LoginResult login(LoginRequest request);

}