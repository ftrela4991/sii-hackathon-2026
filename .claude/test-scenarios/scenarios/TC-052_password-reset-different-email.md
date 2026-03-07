# TC-052: Password Reset Does Not Reveal Account Existence

**ID:** TC-052
**Category:** ACCOUNT
**Type:** negative
**Priority:** low
**Tags:** @account @password @security

---

## Feature

Password Reset – Account Enumeration Prevention

## User Story

As a security measure, the password reset form must not reveal whether a given email is registered.

## Scenario

**Given** the user is on the password recovery page `/password-recovery`
**When** the user enters a random unregistered email "random_<uuid>@notregistered.com"
**And** the user clicks "Send reset link"
**Then** the application shows the same confirmation message as for a valid email
**And** no account information is disclosed

---

## Acceptance Criteria

- [ ] Response is identical for registered and unregistered emails
- [ ] No error reveals whether the email is in the system
- [ ] No 5xx error occurs
