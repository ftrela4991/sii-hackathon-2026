# TC-005: Place Order with Cash on Delivery

**ID:** TC-005
**Category:** BASIC
**Type:** positive
**Priority:** high
**Tags:** @order @checkout @smoke

---

## Feature

Place Order – Cash on Delivery

## User Story

As a logged-in customer with items in the cart, I want to complete checkout using cash on delivery so that I can finalise my purchase.

## Background

A logged-in customer account exists and "Hummingbird Printed T-Shirt" is in the cart (set up via API).

## Scenario

**Given** the customer is logged in
**And** the cart contains "Hummingbird Printed T-Shirt" with quantity 1
**When** the customer navigates to checkout
**And** the customer confirms or enters a valid delivery address
**And** the customer selects a shipping method
**And** the customer selects "Cash on Delivery" as the payment method
**And** the customer clicks "Place order"
**Then** the order confirmation page is displayed
**And** a non-empty order reference number is shown
**And** the page shows a message confirming the order was placed successfully

---

## Acceptance Criteria

- [ ] Order confirmation page loads after submission
- [ ] Order reference is present and non-empty
- [ ] Confirmation message mentions successful order placement
- [ ] Order appears in the customer's order history
- [ ] Order is cleaned up via API after the test
