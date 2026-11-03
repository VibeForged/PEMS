package com.example.pems.dto;

import jakarta.validation.constraints.AssertTrue;

public record DeleteAccountRequest(
        @AssertTrue(message = "Account deletion must be explicitly confirmed") boolean confirmed) {
}
