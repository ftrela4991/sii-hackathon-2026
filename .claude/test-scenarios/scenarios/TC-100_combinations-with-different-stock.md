# TC-100: Product Combinations with Different Stock Levels

**ID:** TC-100
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** medium
**Tags:** @product @combinations @stock

---

## Feature

Per-Combination Stock Management

## User Story

As a shop admin, I want each product combination to have an independent stock level so customers see the correct availability per variant.

## Background

A product has combinations: Size S (stock = 10), Size L (stock = 0, ordering blocked).

## Scenario

**Given** a customer opens the product detail page
**When** the customer selects "Size: S"
**Then** the "Add to cart" button is active
**When** the customer selects "Size: L"
**Then** the "Add to cart" button is disabled or an out-of-stock message appears

---

## Acceptance Criteria

- [ ] Size S shows as available (Add to cart active)
- [ ] Size L shows as out of stock (Add to cart disabled/hidden)
- [ ] Switching between combinations updates availability dynamically
- [ ] Product is deleted via API after the test
