# TC-116: Different Tax Rates Applied per Customer Country

**ID:** TC-116
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** medium
**Tags:** @admin @tax @geo

---

## Feature

Country-Specific Tax Application

## User Story

As a shop admin, I want UK customers to be charged 20% VAT and German customers 19% VAT on the same product.

## Background

Tax rules for UK (20%) and Germany (19%) are configured. Product net price is £100.00.

## Scenario

**Given** a product has a net price of "£100.00"
**When** a UK customer adds it to the cart
**Then** the tax shown is "£20.00 (20%)"
**When** a German customer adds the same product to the cart
**Then** the tax shown is "€19.00 (19%)"

---

## Acceptance Criteria

- [ ] UK customer is charged 20% VAT
- [ ] German customer is charged 19% VAT
- [ ] Tax is calculated on the net price, not the gross price
