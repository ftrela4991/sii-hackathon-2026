# TC-072: 15% Promotion for Logged-In Customer Group

**ID:** TC-072
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** medium
**Tags:** @promotion @groups

---

## Feature

Group-Based Percentage Discount

## User Story

As a shop admin, I want logged-in customers to automatically receive 15% off so returning customers feel rewarded.

## Background

The "Customer" group has a 15% group discount configured.

## Scenario

**Given** a product is priced at "£100.00"
**When** a logged-in customer (in "Customer" group) views the product
**Then** the price shown is "£85.00" (15% off)
**When** a guest customer views the same product
**Then** the full price "£100.00" is shown

---

## Acceptance Criteria

- [ ] 15% discount is applied automatically for logged-in customers
- [ ] Guest customers see the full price
- [ ] Difference between guest and logged-in price is exactly 15%
