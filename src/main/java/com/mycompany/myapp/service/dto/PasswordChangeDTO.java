package com.mycompany.myapp.service.dto;

/**
 * A DTO representing a password change required data - current and new password.
 */
public class PasswordChangeDTO {
    private String currentPassword;
    private String newPassword;
    private boolean authenticate;
    
    public PasswordChangeDTO() {
        // Empty constructor needed for Jackson.
    }

    public PasswordChangeDTO(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }

    public String getCurrentPassword() {

        return currentPassword;
    }
    public static boolean authenticate(String password)
    {
        // The password should be at least ten characters long.
        // The password should contain at least one letter.
        // The password should have at least one digit.
        if ((password.length() > 10) &&
            (password.length() < 100) &&
            (password.matches("[a-z]")) &&
            (password.matches("[0-9]")))

            return true;
        else
            return false;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
