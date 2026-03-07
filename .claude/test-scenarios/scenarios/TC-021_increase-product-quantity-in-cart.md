# TC-021: Increase Product Quantity in Cart

**ID:** TC-021
**Category:** ORDER
**Type:** positive
**Priority:** medium
**Tags:** @cart

---

## Feature

Update Cart Item Quantity (Increase)

## User Story

As a customer, I want to increase the quantity of a cart item and see the line total and grand total update accordingly.

## Background

The cart contains 1 unit of a product priced £25.00.

## Scenario

**Given** the cart contains 1 unit of "Hummingbird Printed T-Shirt" at "£25.00"
**When** the customer increases the quantity to 3 on the cart page
**Then** the cart shows quantity "3" for that product
**And** the line item total shows "£75.00"
**And** the cart grand total reflects the updated amount

---

## Acceptance Criteria

- [ ] Quantity field accepts a value of 3
- [ ] Line total equals quantity × unit price
- [ ] Grand total is updated correctly
- [ ] No page reload required (AJAX update)
