# TC-056: Registration Fails with Oversized First Name (255+ Characters)

**ID:** TC-056
**Category:** ACCOUNT
**Type:** negative
**Priority:** low
**Tags:** @registration @boundary @validation

---

## Feature

Maximum Field Length on Registration

## User Story

As a visitor, entering a first name exceeding the maximum field length must be rejected or safely truncated.

## Scenario

**Given** the user is on the registration page
**When** the user enters a first name of 256 characters (all letter "a")
**And** all other required fields are valid
**And** the user clicks "Save"
**Then** either a validation error is shown for the first name field
**Or** the input is truncated to the maximum allowed length and the account is created with the truncated name

---

## Acceptance Criteria

- [ ] 256-character first name is either rejected with a clear error or safely truncated
- [ ] No 5xx server error occurs
- [ ] If truncated: stored name matches the allowed maximum, not the full 256 characters
