# TC-048: Delete a Delivery Address

**ID:** TC-048
**Category:** ACCOUNT
**Type:** positive
**Priority:** medium
**Tags:** @account @address

---

## Feature

Delete Delivery Address

## User Story

As a logged-in customer, I want to delete a saved delivery address I no longer need.

## Background

The customer has at least two saved addresses.

## Scenario

**Given** the customer is on the Addresses page with 2 saved addresses
**When** the customer clicks "Delete" on one of the addresses
**And** confirms the deletion if a confirmation dialog is shown
**Then** the deleted address is no longer listed
**And** the remaining address is still shown

---

## Acceptance Criteria

- [ ] Deleted address is removed from the list
- [ ] Remaining addresses are unaffected
- [ ] Deleted address is no longer available at checkout
- [ ] No 5xx error occurs during deletion
