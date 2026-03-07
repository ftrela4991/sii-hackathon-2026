# TC-094: Create Customer Groups: VIP, Regular, Wholesale

**ID:** TC-094
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** medium
**Tags:** @admin @groups

---

## Feature

Customer Group Creation

## User Story

As a shop admin, I want to create distinct customer groups for segmentation and group-specific pricing.

## Scenario

**Given** the admin is in the Customers > Groups section of the admin panel
**When** the admin creates group "VIP" with 10% discount
**And** the admin creates group "Regular" with 0% discount
**And** the admin creates group "Wholesale" with 15% discount
**Then** all three groups appear in the groups list
**And** each group shows the correct discount percentage

---

## Acceptance Criteria

- [ ] All 3 groups are saved without errors
- [ ] Groups appear in the admin list with correct discount values
- [ ] Groups are available for customer assignment
- [ ] Test groups are deleted via API after the test
