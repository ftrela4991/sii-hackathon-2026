# TC-111: Enable and Disable Guest Checkout

**ID:** TC-111
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** medium
**Tags:** @admin @checkout @guest

---

## Feature

Guest Checkout Toggle

## User Story

As a shop admin, I want to control whether unauthenticated customers can checkout as guests.

## Scenario

**Given** the admin enables guest checkout in shop settings
**When** an unauthenticated customer proceeds to checkout
**Then** a guest checkout option is presented
**When** the admin disables guest checkout
**And** an unauthenticated customer proceeds to checkout
**Then** the guest checkout option is not available
**And** the customer is prompted to log in or register

---

## Acceptance Criteria

- [ ] Guest checkout option appears when enabled
- [ ] Guest checkout option disappears when disabled
- [ ] Unauthenticated customer is prompted to log in when guest checkout is disabled
