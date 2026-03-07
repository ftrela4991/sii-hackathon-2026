# TC-104: Payment Methods Restricted by Country

**ID:** TC-104
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** low
**Tags:** @admin @payment @geo

---

## Feature

Country-Restricted Payment Methods

## User Story

As a shop admin, I want a payment method available only for UK customers so it targets a specific delivery region.

## Background

A payment method is restricted to "United Kingdom" only.

## Scenario

**Given** a customer has a UK delivery address and reaches the payment step at checkout
**Then** the UK-restricted payment method is listed as an option
**When** a customer has a French delivery address and reaches the payment step
**Then** the UK-restricted payment method is not listed

---

## Acceptance Criteria

- [ ] Method is available for UK delivery addresses
- [ ] Method is hidden for non-UK delivery addresses
- [ ] Other payment methods are not affected
