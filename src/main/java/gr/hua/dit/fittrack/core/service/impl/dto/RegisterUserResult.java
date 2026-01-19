package gr.hua.dit.fittrack.core.service.impl.dto;

public record RegisterUserResult(
        boolean created, // αν δημιουργήθηκε ο χρήστης
        String reason, // λόγος αποτυχίας
        UserView userView // στοιχεία χρήστη
) {
    // Επιτυχής εγγραφή
    public static RegisterUserResult success(UserView userView) {
        return new RegisterUserResult(true, null, userView);
    }
    // Αποτυχημένη εγγραφή
    public static RegisterUserResult fail(String reason) {
        return new RegisterUserResult(false, reason, null);
    }
}
