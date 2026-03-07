# TC-046: Edit an Existing Delivery Address

**ID:** TC-046
**Category:** ACCOUNT
**Type:** positive
**Priority:** medium
**Tags:** @account @address

---

## Feature

Edit Delivery Address

## User Story

As a logged-in customer, I want to update one of my saved delivery addresses.

## Background

The customer has at least one saved address.

## Scenario

**Given** the customer is on the Addresses page `/addresses`
**When** the customer clicks "Edit" next to an existing address
**And** the customer changes the City to "Manchester"
**And** the customer clicks "Save"
**Then** the address list shows the updated City "Manchester" for that address

---

## Acceptance Criteria

- [ ] Edit form pre-fills with existing address data
- [ ] City field update is saved correctly
- [ ] Updated address appears in the list
- [ ] Other address fields remain unchanged
