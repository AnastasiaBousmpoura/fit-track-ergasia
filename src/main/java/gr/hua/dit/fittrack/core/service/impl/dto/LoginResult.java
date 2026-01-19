package gr.hua.dit.fittrack.core.service.impl.dto;

public record LoginResult(
        boolean success, // αν πέτυχε το login
        String reason, // λόγος αποτυχίας
        String accessToken, // JWT token
        String tokenType, // τύπος token
        long expiresIn, // χρόνος λήξης σε δευτερόλεπτα
        String role, // ρόλος χρήστη
        Long userId // id χρήστη
) {
    // Επιτυχές login
    public static LoginResult success(String accessToken,
                                      long expiresIn,
                                      String role,
                                      Long userId) {
        return new LoginResult(true, null, accessToken, "Bearer", expiresIn, role, userId);
    }

    // Αποτυχημένο login
    public static LoginResult fail(String reason) {
        return new LoginResult(false, reason, null, null, 0, null, null);
    }
}
