# TC-037: Registration Fails with Invalid Email Format

**ID:** TC-037
**Category:** ACCOUNT
**Type:** negative
**Priority:** high
**Tags:** @registration @validation

---

## Feature

Email Format Validation on Registration

## User Story

As a visitor, entering an invalid email format on the registration form must show a validation error so I can correct it.

## Scenario

**Given** the user is on the registration page
**When** the user fills in all required fields correctly except Email, which is set to "notanemail"
**And** the user clicks "Save"
**Then** a validation error is shown next to the Email field
**And** the account is not created

---

## Acceptance Criteria

- [ ] "notanemail" is rejected as an invalid email
- [ ] Validation error is displayed next to the email field
- [ ] No account is created
