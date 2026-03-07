# TC-126: Configure Custom Order Statuses

**ID:** TC-126
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** low
**Tags:** @admin @orders

---

## Feature

Order Status Configuration

## User Story

As a shop admin, I want to verify existing order statuses and that assigning them to orders changes the displayed state correctly.

## Scenario

**Given** the admin opens Order States in the admin panel
**When** the admin reviews the "Processing" status and confirms it is active
**And** the admin assigns the "Processing" status to an existing order
**Then** the order now displays "Processing" as its current status
**And** the order's status history is updated with a timestamp

---

## Acceptance Criteria

- [ ] "Processing" status is visible in the Order States list
- [ ] Assigning the status to an order is saved correctly
- [ ] Order list in admin shows the updated status
- [ ] Status history records the change with a timestamp
