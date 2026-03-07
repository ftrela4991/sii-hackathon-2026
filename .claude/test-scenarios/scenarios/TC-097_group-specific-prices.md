# TC-097: Set Group-Specific Prices for a Product

**ID:** TC-097
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** medium
**Tags:** @admin @groups @pricing

---

## Feature

Group-Specific Price Override per Product

## User Story

As a shop admin, I want to set a custom price for VIP customers on a specific product that overrides the group discount.

## Background

VIP group exists. Product base price is £100.00.

## Scenario

**Given** a product is priced at "£100.00" for Regular customers
**When** the admin sets a specific price of "£70.00" for the VIP group on that product
**And** a VIP customer views the product page
**Then** the price shown is "£70.00", not £100.00 or the 10%-discounted price

---

## Acceptance Criteria

- [ ] VIP-specific price overrides both the base price and group discount percentage
- [ ] Regular customer still sees £100.00
- [ ] Price is correct in the cart as well as on the product page
