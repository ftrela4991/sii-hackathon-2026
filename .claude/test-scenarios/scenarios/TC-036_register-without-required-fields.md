# TC-036: Registration Fails Without Required Fields

**ID:** TC-036
**Category:** ACCOUNT
**Type:** negative
**Priority:** high
**Tags:** @registration @validation

---

## Feature

Required Fields Enforcement on Registration

## User Story

As a visitor, submitting the registration form with missing required fields must display validation errors so I know what to correct.

## Scenario

**Given** the user is on the registration page `/registration`
**When** the user submits the form without filling in any fields
**Then** validation error messages are displayed for: First name, Last name, Email, and Password
**And** the account is not created

---

## Acceptance Criteria

- [ ] All required field errors appear on empty submission
- [ ] No account is created
- [ ] Error messages are descriptive and field-specific
