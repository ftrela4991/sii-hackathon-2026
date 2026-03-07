# TC-073: 25% Promotion Active Only in USD Currency

**ID:** TC-073
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** low
**Tags:** @promotion @currency

---

## Feature

Currency-Restricted Discount

## User Story

As a shop admin, I want a promotion that only applies when the customer is shopping in USD.

## Background

USD is an active currency. A cart rule with 25% discount restricted to USD is configured.

## Scenario

**Given** a product is priced at "$100.00 USD"
**And** the customer is browsing in USD
**When** the customer adds the product to the cart
**Then** the cart shows a 25% discount: "-$25.00"
**When** the customer switches to GBP
**Then** the 25% discount is no longer applied

---

## Acceptance Criteria

- [ ] 25% discount applies in USD only
- [ ] Discount disappears when currency changes to GBP
- [ ] Cart total recalculates correctly on currency switch
