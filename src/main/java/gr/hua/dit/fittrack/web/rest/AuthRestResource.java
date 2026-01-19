package gr.hua.dit.fittrack.web.rest;

import gr.hua.dit.fittrack.core.service.AuthService;
import gr.hua.dit.fittrack.core.service.impl.dto.LoginRequest;
import gr.hua.dit.fittrack.core.service.impl.dto.LoginResult;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;


/**
 * REST controller για authentication (JWT login).
 */
@RestController
@RequestMapping(value = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthRestResource {

    private final AuthService authService;

    public AuthRestResource(AuthService authService) {
        if (authService == null) throw new NullPointerException();
        this.authService = authService;
    }

    // --- Login χρήστη (JWT) ---
    @Operation(summary = "JWT login")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login success"),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value =
                                    "{\"timestamp\":\"2026-01-10T00:00:00Z\",\"status\":400,\"error\":\"validation_error\",\"message\":\"Validation failed\",\"path\":\"/api/auth/login\",\"fields\":{\"email\":\"must not be blank\",\"password\":\"must not be blank\"}}"
                            ))),
            @ApiResponse(responseCode = "401", description = "Wrong credentials",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value =
                                    "{\"timestamp\":\"2026-01-10T00:00:00Z\",\"status\":401,\"error\":\"Unauthorized\",\"message\":\"Invalid credentials\",\"path\":\"/api/auth/login\",\"fields\":null}"
                            )))
    })

    @PostMapping("/login")
    public ResponseEntity<LoginResult> login(@RequestBody @Valid LoginRequest request) {

        LoginResult result = authService.login(request);

        if (!result.success()) {
            // 401
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, result.reason());
        }

        return ResponseEntity.ok(result);
    }
}
