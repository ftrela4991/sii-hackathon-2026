# TC-113: Phone Number Format Validation on Address Form

**ID:** TC-113
**Category:** PRODUCTS_AND_API
**Type:** negative
**Priority:** low
**Tags:** @validation @address

---

## Feature

Phone Number Format Validation

## User Story

As a customer, entering an invalid phone number in the address form must show a validation error.

## Scenario

**Given** the customer is on the add address form
**When** the customer enters "ABCDEF" in the phone field
**And** the customer submits the form
**Then** a validation error is shown for the phone number field

---

## Acceptance Criteria

- [ ] Alphabetical input in the phone field is rejected
- [ ] Validation error appears next to the phone field
- [ ] Address is not saved with invalid phone number
