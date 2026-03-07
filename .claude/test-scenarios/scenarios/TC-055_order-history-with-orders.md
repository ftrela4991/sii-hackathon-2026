# TC-055: Order History Shows Placed Orders

**ID:** TC-055
**Category:** ACCOUNT
**Type:** positive
**Priority:** medium
**Tags:** @account @orders

---

## Feature

Order History With Orders

## User Story

As a customer with past orders, the order history page should list them with correct details.

## Background

The customer has placed at least one order (set up via API or prior test step).

## Scenario

**Given** the customer is logged in and has placed 1 order
**When** the customer navigates to `/order-history`
**Then** the order history page lists at least 1 order
**And** each order entry shows: reference number, date, status, and total price

---

## Acceptance Criteria

- [ ] At least one order is listed
- [ ] Order reference matches the one created during setup
- [ ] Date, status, and price are all non-empty
- [ ] Test data is cleaned up via API after the test
