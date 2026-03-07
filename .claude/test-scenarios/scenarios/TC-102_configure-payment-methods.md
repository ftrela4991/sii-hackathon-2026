# TC-102: Configure Payment Method Settings

**ID:** TC-102
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** medium
**Tags:** @admin @payment

---

## Feature

Payment Method Configuration

## User Story

As a shop admin, I want to configure payment method details so that checkout instructions are accurate for customers.

## Scenario

**Given** the admin opens the "Pay by Check" module configuration
**When** the admin sets the payee name to "Test Payee Ltd"
**And** saves the configuration
**Then** the payment instructions shown at checkout display "Test Payee Ltd" as the payee name

---

## Acceptance Criteria

- [ ] Configuration is saved without errors
- [ ] Payee name appears in checkout payment instructions
- [ ] No other payment settings are affected
