# TC-060: Customer Can Manage Multiple Delivery Addresses

**ID:** TC-060
**Category:** ACCOUNT
**Type:** positive
**Priority:** low
**Tags:** @account @address

---

## Feature

Multiple Delivery Addresses per Account

## User Story

As a customer, I want to save multiple delivery addresses and choose between them at checkout.

## Background

The customer is logged in with no pre-existing addresses.

## Scenario

**Given** the customer has no saved addresses
**When** the customer adds 3 different delivery addresses via the Addresses page
**Then** all 3 addresses appear in the Addresses list
**When** the customer proceeds to checkout
**Then** the customer can select any of the 3 saved addresses as the delivery address

---

## Acceptance Criteria

- [ ] All 3 addresses are saved and listed
- [ ] Address selector at checkout shows all 3 options
- [ ] Selecting each address updates the delivery details
- [ ] All test addresses are deleted via API after the test
