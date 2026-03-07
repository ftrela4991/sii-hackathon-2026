# TC-105: Activate Standard and Express Carriers

**ID:** TC-105
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** high
**Tags:** @admin @shipping

---

## Feature

Carrier Activation

## User Story

As a shop admin, I want to enable Standard and Express shipping carriers so customers can choose their preferred delivery speed.

## Scenario

**Given** the admin activates "Standard Delivery" and "Express Delivery" carriers in the shipping section
**When** a customer reaches the shipping step at checkout
**Then** both "Standard Delivery" and "Express Delivery" are listed as options
**And** the customer can select either carrier to proceed

---

## Acceptance Criteria

- [ ] Both carriers are visible at the shipping step
- [ ] Each carrier shows its name and price
- [ ] Selecting each carrier allows checkout to proceed
