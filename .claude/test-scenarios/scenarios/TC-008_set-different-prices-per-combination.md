# TC-008: Set Different Prices per Combination

**ID:** TC-008
**Category:** BASIC
**Type:** positive
**Priority:** medium
**Tags:** @admin @product @combinations @pricing

---

## Feature

Combination-Specific Price Impact

## User Story

As a shop admin, I want each product combination to have its own price so that larger sizes or premium colours cost more.

## Background

A product with Size combinations (S, M, L) and a base price of £30.00 exists.

## Scenario

**Given** the admin opens the product with combinations in the admin panel
**When** the admin sets a price impact of "+£5.00" for the "Size: L" combination
**And** the admin saves the product
**And** a customer opens the product detail page
**And** the customer selects "Size: S"
**Then** the displayed price is "£30.00" (base price)
**When** the customer selects "Size: L"
**Then** the displayed price updates to "£35.00" (base + impact)

---

## Acceptance Criteria

- [ ] Price updates dynamically when switching combinations
- [ ] Base price is correct for the default combination
- [ ] Price impact is added (not replacing) the base price
- [ ] Cart reflects the combination-specific price when added
