# TC-122: Set Different Product Prices for Different Countries

**ID:** TC-122
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** low
**Tags:** @admin @pricing @geo

---

## Feature

Country-Specific Product Pricing

## User Story

As a shop admin, I want to set a specific price for UK and a different one for Germany on the same product.

## Background

UK and Germany are active countries with separate currencies (GBP and EUR).

## Scenario

**Given** the admin sets a specific price for Product A: "£80.00" for UK and "€90.00" for Germany
**When** a UK customer views the product
**Then** the price shown is "£80.00"
**When** a German customer views the same product
**Then** the price shown is "€90.00"

---

## Acceptance Criteria

- [ ] UK-specific price is displayed for UK customers
- [ ] German-specific price is displayed for German customers
- [ ] Prices are independent and not exchange-rate-derived
