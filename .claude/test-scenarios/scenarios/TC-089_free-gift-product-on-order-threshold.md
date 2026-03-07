# TC-089: Free Gift Product Added When Order Reaches £80

**ID:** TC-089
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** medium
**Tags:** @coupon @gift

---

## Feature

Free Gift Threshold Promotion

## User Story

As a customer, when my cart total reaches £80, a free gift product should be automatically added to my cart.

## Background

A cart rule: free product "Gift Item" when cart >= £80, is active.

## Scenario

**Given** the customer's cart total is "£79.99"
**Then** the free gift "Gift Item" is not in the cart
**When** the customer adds another item bringing the total to "£80.00" or more
**Then** the free gift "Gift Item" is automatically added to the cart with price "£0.00"

---

## Acceptance Criteria

- [ ] Gift is not added below the £80 threshold
- [ ] Gift is added automatically at £80.00+
- [ ] Gift item shows as £0.00 in the cart
- [ ] Removing other items below the threshold removes the gift
