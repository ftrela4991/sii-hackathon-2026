# TC-045: Add a New Delivery Address

**ID:** TC-045
**Category:** ACCOUNT
**Type:** positive
**Priority:** high
**Tags:** @account @address

---

## Feature

Add Delivery Address

## User Story

As a logged-in customer, I want to add a new delivery address to my account so I can use it at checkout.

## Background

The customer is logged in.

## Scenario

**Given** the customer is on the Addresses page `/addresses`
**When** the customer clicks "Create new address"
**And** the customer fills in Address "10 Downing Street"
**And** the customer fills in City "London"
**And** the customer fills in Postcode "SW1A 2AA"
**And** the customer selects Country "United Kingdom"
**And** the customer clicks "Save"
**Then** the new address "10 Downing Street, London" appears in the list of saved addresses

---

## Acceptance Criteria

- [ ] Address is saved without errors
- [ ] New address appears in the address list
- [ ] Address is available for selection at checkout
- [ ] Address is deleted via API after the test
