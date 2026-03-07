# TC-030: Order Confirmation Message is Displayed

**ID:** TC-030
**Category:** ORDER
**Type:** positive
**Priority:** high
**Tags:** @checkout @order @confirmation

---

## Feature

Order Confirmation Page Content

## User Story

As a customer, after placing an order I should see a clear confirmation with an order reference so I know the order was received.

## Background

The customer has completed all checkout steps and clicked "Place order".

## Scenario

**Given** the customer has filled in checkout address and selected "Pay by Check" as the payment method
**When** the customer clicks "Place order"
**Then** the page heading confirms the order (e.g. "Your order is confirmed")
**And** a non-empty order reference number is displayed
**And** the customer's email address is shown in the confirmation details

---

## Acceptance Criteria

- [ ] Confirmation heading is visible
- [ ] Order reference is non-empty and formatted correctly
- [ ] Customer email appears in the confirmation
- [ ] No error banners or 5xx pages are shown
