# TC-038: Registration Fails with Already Registered Email

**ID:** TC-038
**Category:** ACCOUNT
**Type:** negative
**Priority:** high
**Tags:** @registration @validation

---

## Feature

Duplicate Email Prevention on Registration

## User Story

As a visitor, attempting to register with an already-used email must show an error so no duplicate accounts are created.

## Background

An account with email "existing_<uuid>@test.com" already exists (created via API).

## Scenario

**Given** the user is on the registration page
**When** the user fills in all fields correctly using the already-registered email "existing_<uuid>@test.com"
**And** the user clicks "Save"
**Then** an error message is displayed stating the email address is already registered
**And** no duplicate account is created

---

## Acceptance Criteria

- [ ] Duplicate email is rejected
- [ ] Error message is clear and user-friendly
- [ ] Only one account exists for that email in the system
- [ ] Pre-existing test account is deleted via API after the test
