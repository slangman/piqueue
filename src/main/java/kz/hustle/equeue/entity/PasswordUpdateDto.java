package kz.hustle.equeue.entity;

public class PasswordUpdateDto {
    private Long userId;
    private String currentPassword;
    private String newPassword;
    private String confirmNewPassword;

    public PasswordUpdateDto() {
    }

    public PasswordUpdateDto(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCurrentPassword() {
        return currentPassword;
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

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public void setConfirmNewPassword(String confirmPassword) {
        this.confirmNewPassword = confirmPassword;
    }
}
