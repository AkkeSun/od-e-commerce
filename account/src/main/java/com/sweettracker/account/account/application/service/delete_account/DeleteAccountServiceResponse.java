package com.sweettracker.account.account.application.service.delete_account;

import lombok.Builder;

@Builder
public record DeleteAccountServiceResponse(Long id, String email, String result) {

}
