# TC-107: Free Shipping When Order Total Exceeds Threshold

**ID:** TC-107
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** medium
**Tags:** @admin @shipping @boundary

---

## Feature

Free Shipping Threshold

## User Story

As a customer, I want shipping to be free when my cart total exceeds £100 so I am incentivised to spend more.

## Background

A carrier is configured with free shipping above £100.00.

## Scenario

**Given** the customer's cart total is "£99.99"
**Then** the shipping cost is shown (e.g. "£5.00")
**When** the customer adds an item bringing the total to "£100.01"
**Then** the shipping cost is shown as "£0.00" (Free)

---

## Acceptance Criteria

- [ ] Shipping cost appears below the threshold
- [ ] Shipping becomes £0.00 at or above £100.01
- [ ] Cart total reflects the removal of the shipping charge
