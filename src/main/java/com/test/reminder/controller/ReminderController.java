package com.test.reminder.controller;

import com.test.reminder.dto.CreateReminderDto;
import com.test.reminder.dto.GetReminderDto;
import com.test.reminder.dto.RemindersFilter;
import com.test.reminder.dto.UpdateReminderDto;
import com.test.reminder.service.ReminderService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/reminders")
@AllArgsConstructor
public class ReminderController {
    private final ReminderService reminderService;

    @GetMapping
    public String getReminders(Model model,
                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime from,
                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime to,
                               @RequestParam(required = false) String title,
                               @RequestParam(required = false) String description,
                               @PageableDefault(size = 5) Pageable pageable) {
        RemindersFilter filter = new RemindersFilter(from, to, title, description);
        Page<GetReminderDto> page = reminderService.getReminders(pageable, filter);

        model.addAttribute("reminders", page.getContent());
        model.addAttribute("page", page);
        model.addAttribute("filter", filter);
        return "reminders/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("reminder", new CreateReminderDto());
        return "reminders/create";
    }

    @PostMapping("/create")
    public String createReminder(@ModelAttribute("reminder") @Validated CreateReminderDto dto,
                                 RedirectAttributes redirectAttributes) {
        reminderService.createReminder(dto);
        redirectAttributes.addFlashAttribute("success", "Reminder created successfully!");
        return "redirect:/reminders";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        UpdateReminderDto reminder = reminderService.getReminderForUpdate(id);
        model.addAttribute("reminder", reminder);
        return "reminders/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateReminder(@PathVariable Long id,
                                 @ModelAttribute("reminder") @Validated UpdateReminderDto updatedReminder,
                                 RedirectAttributes redirectAttributes) {
        reminderService.updateReminder(updatedReminder);
        redirectAttributes.addFlashAttribute("success", "Reminder updated successfully!");
        return "redirect:/reminders";
    }

    @PostMapping("/delete/{id}")
    public String deleteReminder(@PathVariable Long id,
                                 RedirectAttributes redirectAttributes) {
        reminderService.deleteReminder(id);
        redirectAttributes.addFlashAttribute("success", "Reminder deleted successfully!");
        return "redirect:/reminders";
    }
}
