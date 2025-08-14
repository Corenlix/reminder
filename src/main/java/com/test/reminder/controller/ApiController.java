package com.test.reminder.controller;

import com.test.reminder.dto.CreateReminderDto;
import com.test.reminder.dto.GetReminderDto;
import com.test.reminder.dto.RemindersFilter;
import com.test.reminder.service.ReminderService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class ApiController {
    private final ReminderService reminderService;

    @GetMapping("/v1/token")
    public String getToken(OAuth2AuthenticationToken authentication) {
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();

        return oidcUser.getIdToken().getTokenValue();
    }

    @PostMapping("/v1/reminder/create")
    public void createReminder(@RequestBody @Validated CreateReminderDto reminderDto) {
        reminderService.createReminder(reminderDto);
    }

    @DeleteMapping("/v1/reminder/delete/{id}")
    public void deleteReminder(@PathVariable("id") Long reminderId) {
        reminderService.deleteReminder(reminderId);
    }

    @GetMapping("/v1/reminder/sort")
    public Page<GetReminderDto> getRemindersSorted(
            Sort sort
    ) {
        return reminderService.getReminders(Pageable.unpaged(sort), null);
    }

    @GetMapping("/v1/reminder/filtr")
    public Page<GetReminderDto> getRemindersFiltered(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime to
    ) {
        return reminderService.getReminders(Pageable.unpaged(), new RemindersFilter(from, to, null, null));
    }

    @GetMapping("/v1/reminders/list")
    public Page<GetReminderDto> getReminders(Pageable pageable) {
        return reminderService.getReminders(pageable, null);
    }
}
