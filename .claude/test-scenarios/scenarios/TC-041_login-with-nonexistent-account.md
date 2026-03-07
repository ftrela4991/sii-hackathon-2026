# TC-041: Login Fails for Non-Existent Account

**ID:** TC-041
**Category:** ACCOUNT
**Type:** negative
**Priority:** high
**Tags:** @login @validation

---

## Feature

Authentication Failure for Unknown Email

## User Story

As a visitor, attempting to log in with an email that has no associated account must show an authentication error.

## Scenario

**Given** the user is on the login page `/login`
**When** the user enters email "ghost_<uuid>@test.com" (no account exists for this email)
**And** the user enters any password
**And** the user clicks "Sign in"
**Then** an authentication error message is displayed
**And** the user remains on the login page

---

## Acceptance Criteria

- [ ] Login is rejected for an unregistered email
- [ ] Error message does not distinguish between wrong email and wrong password (security best practice)
- [ ] User stays on the login page
- [ ] No session is created
