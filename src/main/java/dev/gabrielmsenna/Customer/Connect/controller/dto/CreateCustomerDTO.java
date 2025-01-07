package dev.gabrielmsenna.Customer.Connect.controller.dto;

public record CreateCustomerDTO (String fullName,
                                 String cpf,
                                 String email,
                                 String phoneNumber) {
}
