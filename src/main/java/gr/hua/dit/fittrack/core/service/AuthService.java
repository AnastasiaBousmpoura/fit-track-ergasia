package gr.hua.dit.fittrack.core.service;

import gr.hua.dit.fittrack.core.service.model.RegisterUserRequest;
import gr.hua.dit.fittrack.core.service.model.RegisterUserResult;
import gr.hua.dit.fittrack.core.service.model.LoginRequest;
import gr.hua.dit.fittrack.core.service.model.LoginResult;

public interface AuthService {

    RegisterUserResult registerUser(RegisterUserRequest request);

    LoginResult login(LoginRequest request);

}
