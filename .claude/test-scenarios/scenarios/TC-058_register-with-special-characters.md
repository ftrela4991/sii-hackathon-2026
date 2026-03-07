# TC-058: Registration Handles Special Characters Safely

**ID:** TC-058
**Category:** ACCOUNT
**Type:** negative
**Priority:** low
**Tags:** @registration @validation @security

---

## Feature

Special Character and Injection Prevention on Registration

## User Story

As a visitor, entering special characters or script tags in name fields must be safely handled without executing them.

## Scenario

**Given** the user is on the registration page
**When** the user enters "<script>alert(1)</script>" in the First name field
**And** all other required fields are valid
**And** the user clicks "Save"
**Then** the application does not execute the script
**And** either a validation error is shown or the input is safely escaped/stored

---

## Acceptance Criteria

- [ ] No JavaScript is executed from the input
- [ ] Application does not display an XSS alert
- [ ] Either validation error is shown or stored value is HTML-escaped
- [ ] No 5xx server error occurs
