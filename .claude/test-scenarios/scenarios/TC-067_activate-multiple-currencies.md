# TC-067: Activate Multiple Currencies (EUR, USD, GBP, PLN)

**ID:** TC-067
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** medium
**Tags:** @admin @currency

---

## Feature

Multi-Currency Activation

## User Story

As a shop admin, I want to enable multiple currencies so customers can shop in their preferred currency.

## Scenario

**Given** the admin enables currencies EUR, USD, GBP, and PLN in Localisation > Currencies
**When** a customer views the storefront
**Then** a currency selector is visible
**And** the customer can switch between EUR, USD, GBP, and PLN
**And** prices update according to the configured exchange rates when switching currency

---

## Acceptance Criteria

- [ ] All 4 currencies are available in the selector
- [ ] Switching currency updates prices on the page
- [ ] Currency symbol changes accordingly (€, $, £, zł)
