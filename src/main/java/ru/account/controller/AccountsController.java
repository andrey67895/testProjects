package ru.account.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import ru.account.models.Account;
import ru.account.models.ErrorModel;
import ru.account.services.AccountsService;

@RequiredArgsConstructor
@RestController
@Log4j2
public class AccountsController {
    @Autowired
    private AccountsService accountsService;


    @ApiResponse(responseCode = "200")
    @GetMapping("/accounts")
    public ResponseEntity<Object> getAccounts(
            @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
            @RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit
    ) {
        return ResponseEntity.ok().body(accountsService.getAccounts(offset, limit));
    }

    @ApiResponse(responseCode = "200")
    @GetMapping("/accounts/{id}")
    public ResponseEntity<Object> getAccountsById(
            @PathVariable(value = "id") Long id
    ) {
        try {
            Account account = accountsService.getAccountById(id).orElse(null);
            if (account != null) {
                return ResponseEntity.ok().body(accountsService.saveAccount(account));
            } else {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ErrorModel(HttpStatus.NOT_FOUND.value(), String.format("Отсуствует юзер с id: %s", id)));

            }
        } catch (RestClientException | IllegalArgumentException e) {
            log.info(e);
            return ResponseEntity.badRequest().body(new ErrorModel(HttpStatus.BAD_REQUEST.value(), e.toString()));
        } catch (Exception e) {
            log.info(e);
            return ResponseEntity.internalServerError().body(new ErrorModel(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.toString()));
        } }

    @ApiResponses({
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400")
    })
    @PostMapping("/accounts")
    public ResponseEntity<Object> postAccount(
            @RequestBody Account account
    ) {
        try {
            account.checkAccount();
            account.setId(null);
            return ResponseEntity.status(HttpStatus.CREATED).body(accountsService.saveAccount(account));
        } catch (RestClientException | IllegalArgumentException e) {
            log.info(e);
            return ResponseEntity.badRequest().body(new ErrorModel(HttpStatus.BAD_REQUEST.value(), e.toString()));
        } catch (Exception e) {
            log.info(e);
            return ResponseEntity.internalServerError().body(new ErrorModel(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.toString()));
        }
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404"),
            @ApiResponse(responseCode = "400")
    })
    @PutMapping("/accounts/{id}")
    public ResponseEntity<Object> putAccountById(
            @PathVariable(value = "id") Long id,
            @RequestBody Account account
    ) {
        try {
            if (accountsService.getAccountById(id).orElse(null) != null) {
                account.checkAccount();
                account.setId(id);
                return ResponseEntity.ok().body(accountsService.saveAccount(account));
            } else {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ErrorModel(HttpStatus.NOT_FOUND.value(), String.format("Отсуствует юзер с id: %s", id)));

            }
        } catch (RestClientException | IllegalArgumentException e) {
            log.info(e);
            return ResponseEntity.badRequest().body(new ErrorModel(HttpStatus.BAD_REQUEST.value(), e.toString()));
        } catch (Exception e) {
            log.info(e);
            return ResponseEntity.internalServerError().body(new ErrorModel(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.toString()));
        }
    }



    @ApiResponses({
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "404")
    })
    @DeleteMapping("/accounts/{id}")
    public ResponseEntity<Object> deleteAccountById(
            @PathVariable(value = "id") Long id
    ) {
        Account account = accountsService.getAccountById(id).orElse(null);
        if (account != null) {
            accountsService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorModel(HttpStatus.NOT_FOUND.value(), String.format("Отсуствует юзер с id: %s", id)));
        }
    }
}
