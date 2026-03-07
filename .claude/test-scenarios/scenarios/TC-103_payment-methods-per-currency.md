# TC-103: Payment Methods Restricted by Currency

**ID:** TC-103
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** low
**Tags:** @admin @payment @currency

---

## Feature

Currency-Restricted Payment Methods

## User Story

As a shop admin, I want to restrict a payment method to GBP orders only so it does not appear for other currencies.

## Background

"Pay by Check" is configured to be available for GBP only.

## Scenario

**Given** a customer is browsing in GBP and reaches the payment step at checkout
**Then** "Pay by Check" is listed as an available option
**When** a customer switches to EUR and reaches the payment step
**Then** "Pay by Check" is not listed

---

## Acceptance Criteria

- [ ] "Pay by Check" appears for GBP customers
- [ ] "Pay by Check" does not appear for EUR customers
- [ ] Other payment methods are unaffected by this restriction
