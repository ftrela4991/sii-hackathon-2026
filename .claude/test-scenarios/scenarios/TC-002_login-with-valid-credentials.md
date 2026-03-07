# TC-002: Login with Valid Credentials

**ID:** TC-002
**Category:** BASIC
**Type:** positive
**Priority:** high
**Tags:** @login @account @smoke

---

## Feature

Customer Login

## User Story

As a registered customer, I want to log in so that I can access my account and order history.

## Background

A customer account exists with email "existing_user@test.com" and password "Test@1234!" (created via API in setup).

## Scenario

**Given** the user is on the login page `/login`
**And** the user is not logged in
**When** the user enters email "existing_user@test.com"
**And** the user enters password "Test@1234!"
**And** the user clicks "Sign in"
**Then** the user remains on the homepage `http://145.239.29.235/`
**And** the header shows "Sign out" instead of "Sign in"
**And** the header displays the customer's full name next to the sign out link

---

## Acceptance Criteria

- [ ] User remains on the homepage after successful login
- [ ] Header shows "Sign out" instead of "Sign in"
- [ ] Header displays the customer's full name next to "Sign out"
- [ ] Test account created and removed via API
