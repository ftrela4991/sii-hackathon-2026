# TC-053: Password Reset for Non-Existent Email Shows Generic Message

**ID:** TC-053
**Category:** ACCOUNT
**Type:** negative
**Priority:** medium
**Tags:** @account @password

---

## Feature

Password Reset – Unknown Email Handling

## User Story

As a visitor, entering an unregistered email in the password reset form must not crash the application or expose system details.

## Scenario

**Given** the user is on the password recovery page `/password-recovery`
**When** the user enters "ghost_<uuid>@test.com" (no account exists)
**And** the user clicks "Send reset link"
**Then** the application responds without a 5xx error
**And** a generic message is displayed (does not confirm or deny account existence)

---

## Acceptance Criteria

- [ ] No server error on submission
- [ ] Generic message is shown
- [ ] Form does not expose registration status
