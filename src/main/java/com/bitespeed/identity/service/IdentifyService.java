package com.bitespeed.identity.service;

import com.bitespeed.identity.model.*;
import com.bitespeed.identity.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IdentifyService {
    private final ContactRepository repository;

    public Map<String, Object> identify(String email, String phoneNumber) {
        List<Contact> existing = repository.findByEmailOrPhoneNumber(email, phoneNumber);

        Contact primary = null;
        if (existing.isEmpty()) {
            primary = repository.save(Contact.builder()
                    .email(email)
                    .phoneNumber(phoneNumber)
                    .linkPrecedence(LinkPrecedence.primary)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build());
        } else {
            primary = existing.stream()
                    .filter(c -> c.getLinkPrecedence() == LinkPrecedence.primary)
                    .min(Comparator.comparing(Contact::getCreatedAt))
                    .orElse(existing.get(0));

            for (Contact c : existing) {
                if (!Objects.equals(c.getId(), primary.getId()) && c.getLinkPrecedence() == LinkPrecedence.primary) {
                    c.setLinkPrecedence(LinkPrecedence.secondary);
                    c.setLinkedId(primary.getId());
                    c.setUpdatedAt(LocalDateTime.now());
                    repository.save(c);
                }
            }

            boolean alreadyExists = existing.stream().anyMatch(c ->
                    Objects.equals(c.getEmail(), email) && Objects.equals(c.getPhoneNumber(), phoneNumber));
            if (!alreadyExists) {
                repository.save(Contact.builder()
                        .email(email)
                        .phoneNumber(phoneNumber)
                        .linkPrecedence(LinkPrecedence.secondary)
                        .linkedId(primary.getId())
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build());
            }
        }

        List<Contact> related = repository.findByLinkedIdOrId(primary.getId(), primary.getId());

        Set<String> emails = related.stream().map(Contact::getEmail).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<String> phones = related.stream().map(Contact::getPhoneNumber).filter(Objects::nonNull).collect(Collectors.toSet());
        List<Long> secondaryIds = related.stream()
                .filter(c -> c.getLinkPrecedence() == LinkPrecedence.secondary)
                .map(Contact::getId).sorted().toList();

        Map<String, Object> response = new HashMap<>();
        Map<String, Object> contact = new HashMap<>();
        contact.put("primaryContactId", primary.getId());
        contact.put("emails", emails);
        contact.put("phoneNumbers", phones);
        contact.put("secondaryContactIds", secondaryIds);
        contact.put("createdAt", primary.getCreatedAt());
        contact.put("updatedAt", primary.getUpdatedAt());
        response.put("contact", contact);
        

        return response;
    }
}

