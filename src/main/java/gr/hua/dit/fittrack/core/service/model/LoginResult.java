package gr.hua.dit.fittrack.core.service.model;

public record LoginResult(
        boolean success,
        String token,
        String reason
) {

    public static LoginResult success(String token) {
        return new LoginResult(true, token, null);
    }

    public static LoginResult fail(String reason) {
        return new LoginResult(false, null, reason);
    }
}
