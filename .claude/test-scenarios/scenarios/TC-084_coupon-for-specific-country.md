# TC-084: Coupon Valid Only for Customers from a Specific Country

**ID:** TC-084
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** low
**Tags:** @coupon @geo

---

## Feature

Country-Restricted Coupon

## User Story

As a shop admin, I want a coupon available only to customers with a UK delivery address so it targets a specific market.

## Background

A cart rule restricted to country "United Kingdom" is active.

## Scenario

**Given** a customer has a UK delivery address and applies the country-specific coupon
**Then** the discount is applied to the cart
**When** a customer with a French delivery address applies the same coupon
**Then** the coupon is rejected with an appropriate error

---

## Acceptance Criteria

- [ ] UK customer receives the discount
- [ ] French customer receives an error
- [ ] Error message is clear
