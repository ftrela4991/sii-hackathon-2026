# TC-121: Shipping Restricted to Specific Countries

**ID:** TC-121
**Category:** PRODUCTS_AND_API
**Type:** negative
**Priority:** medium
**Tags:** @admin @shipping @geo

---

## Feature

Shipping Country Restriction

## User Story

As a shop admin, I want to prevent shipping to countries not in the allowed list so orders are only placed for serviceable regions.

## Background

Shipping is only enabled for UK and Germany.

## Scenario

**Given** a customer enters a delivery address in "Brazil"
**When** the customer proceeds to the shipping step at checkout
**Then** no carriers are available for that address
**And** an appropriate message is shown that shipping is not available to that country

---

## Acceptance Criteria

- [ ] No carriers are listed for Brazil
- [ ] Informative message is shown to the customer
- [ ] No order can be placed for a restricted country
