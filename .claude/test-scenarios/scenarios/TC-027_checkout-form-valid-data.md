# TC-027: Complete Checkout Form with Valid Data

**ID:** TC-027
**Category:** ORDER
**Type:** positive
**Priority:** high
**Tags:** @checkout @order

---

## Feature

Successful Checkout with Valid Form Data

## User Story

As a logged-in customer, I want to fill in the checkout form with valid data and successfully place my order.

## Background

The customer is logged in and the cart contains one product.

## Scenario

**Given** the customer is on the checkout page
**When** the customer confirms or selects a valid delivery address
**And** the customer selects a shipping method
**And** the customer selects "Pay by Check" as the payment method
**And** the customer clicks "Place order"
**Then** the order confirmation page is displayed
**And** a valid, non-empty order reference number is shown

---

## Acceptance Criteria

- [ ] All checkout steps complete without error
- [ ] Order confirmation page is reached
- [ ] Order reference number is non-empty
- [ ] Order appears in the admin panel order list
