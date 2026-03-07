# TC-001: Register New Customer Successfully

**ID:** TC-001
**Category:** BASIC
**Type:** positive
**Priority:** high
**Tags:** @registration @account @smoke

---

## Feature

User Registration

## User Story

As a new visitor, I want to create an account so that I can place orders and manage my profile.

## Scenario

**Given** the user is on the registration page `/registration`
**And** the user is not logged in
**When** the user selects Social Title "Mr."
**And** the user enters First Name "John"
**And** the user enters Last Name "Doe"
**And** the user enters a unique Email "user_<uuid>@test.com"
**And** the user enters Password "Test@1234!" (meets the "Strong" requirement)
**And** the user checks "I agree to the terms and conditions and the privacy policy"
**And** the user clicks "Save"
**Then** the user is redirected to `/my-account`
**And** the page displays a greeting containing "John Doe"
**And** the header shows "Sign out" instead of "Sign in"

---

## Acceptance Criteria

- [ ] User is redirected to My Account after successful registration
- [ ] Greeting includes the registered name
- [ ] Session is active (Sign out visible)
- [ ] Test email is unique per run (UUID/timestamp)
- [ ] Account is cleaned up via API after the test
