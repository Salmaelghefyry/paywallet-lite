package com.paylogic.paywalletlite.dto.request;

/**
 * DTO pour la mise à jour du profil utilisateur par l'utilisateur lui-même.
 *
 * Seuls les champs non sensibles sont modifiables.
 * Le rôle, le statut, le KYC ne peuvent pas être modifiés par l'utilisateur.
 */
public class UpdateProfileRequestDto {

    private String firstName;
    private String lastName;
    private String email;

    public UpdateProfileRequestDto() {}

    // Getters et Setters
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}