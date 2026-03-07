# TC-051: Password Reset Email Sent for Valid Account

**ID:** TC-051
**Category:** ACCOUNT
**Type:** positive
**Priority:** high
**Tags:** @account @password

---

## Feature

Password Reset Request

## User Story

As a registered customer, I want to request a password reset link so that I can regain access to my account.

## Background

An account with email "user_<uuid>@test.com" exists (created via API).

## Scenario

**Given** the user is on the password recovery page `/password-recovery`
**When** the user enters the registered email "user_<uuid>@test.com"
**And** the user clicks "Send reset link"
**Then** a confirmation message is displayed stating the reset email was sent

---

## Acceptance Criteria

- [ ] Confirmation message appears after submission
- [ ] No 5xx error occurs
- [ ] Message does not expose whether the email is registered (security)
- [ ] Test account is deleted via API after the test
