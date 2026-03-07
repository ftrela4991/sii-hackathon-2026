# TC-047: Edit Address Fails When Required Fields Are Cleared

**ID:** TC-047
**Category:** ACCOUNT
**Type:** negative
**Priority:** medium
**Tags:** @account @address @validation

---

## Feature

Required Field Validation on Address Edit

## User Story

As a logged-in customer, saving an address with a required field cleared must show a validation error.

## Background

The customer has at least one saved address.

## Scenario

**Given** the customer opens the address edit form for an existing address
**When** the customer clears the required "Address" (street) field
**And** the customer clicks "Save"
**Then** a validation error is shown for the empty "Address" field
**And** the address is not updated

---

## Acceptance Criteria

- [ ] Empty required field triggers a validation error
- [ ] Error message is displayed next to the affected field
- [ ] Address data is not changed in the system
