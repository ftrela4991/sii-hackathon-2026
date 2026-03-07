# TC-040: Login Fails with Invalid Credentials

**ID:** TC-040
**Category:** ACCOUNT
**Type:** negative
**Priority:** high
**Tags:** @login @validation

---

## Feature

Authentication Failure with Wrong Password

## User Story

As a visitor, entering incorrect credentials on the login form must show an authentication error and not grant access.

## Background

An account with email "user_<uuid>@test.com" exists (created via API).

## Scenario

**Given** the user is on the login page `/login`
**When** the user enters the correct email "user_<uuid>@test.com"
**And** the user enters an incorrect password "WrongPassword!"
**And** the user clicks "Sign in"
**Then** an error message is displayed (e.g. "Authentication failed")
**And** the user remains on the login page

---

## Acceptance Criteria

- [ ] Authentication is rejected for wrong password
- [ ] Error message is displayed (not a 5xx)
- [ ] User stays on the login page
- [ ] No session is created
- [ ] Test account is deleted via API after the test
