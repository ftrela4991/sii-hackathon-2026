# TC-054: Order History is Empty for New Customer

**ID:** TC-054
**Category:** ACCOUNT
**Type:** positive
**Priority:** medium
**Tags:** @account @orders

---

## Feature

Empty Order History State

## User Story

As a newly registered customer with no past orders, the order history page should show a clear empty state.

## Background

The customer is newly registered with no past orders (account created via API).

## Scenario

**Given** the customer is logged in and has no orders
**When** the customer navigates to `/order-history`
**Then** the order history page is displayed without errors
**And** a message indicates there are no orders yet

---

## Acceptance Criteria

- [ ] Order history page loads successfully
- [ ] Empty state message is visible
- [ ] No order rows or references are shown
- [ ] Test account is deleted via API after the test
