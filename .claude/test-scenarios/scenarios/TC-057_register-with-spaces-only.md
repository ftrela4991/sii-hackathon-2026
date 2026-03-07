# TC-057: Registration Fails with Spaces-Only in Required Fields

**ID:** TC-057
**Category:** ACCOUNT
**Type:** negative
**Priority:** medium
**Tags:** @registration @validation

---

## Feature

Whitespace-Only Input Rejected on Registration

## User Story

As a visitor, entering only spaces in required name fields must be treated as empty input and rejected.

## Scenario

**Given** the user is on the registration page
**When** the user enters "   " (three spaces) in the First name field
**And** the user enters "   " in the Last name field
**And** all other fields (email, password, T&C checkbox) are valid
**And** the user clicks "Save"
**Then** validation errors are shown for First name and Last name
**And** the account is not created

---

## Acceptance Criteria

- [ ] Spaces-only first name is treated as empty/invalid
- [ ] Spaces-only last name is treated as empty/invalid
- [ ] Validation errors are field-specific
- [ ] No account is created
