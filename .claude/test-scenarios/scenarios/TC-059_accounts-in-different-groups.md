# TC-059: Accounts Assigned to Different Customer Groups

**ID:** TC-059
**Category:** ACCOUNT
**Type:** positive
**Priority:** medium
**Tags:** @account @groups

---

## Feature

Customer Group Assignment

## User Story

As a shop admin, I want to assign customers to groups (VIP, Regular, Wholesale) and verify group membership.

## Background

Groups VIP, Regular, and Wholesale exist in the admin panel.

## Scenario

**Given** three customer accounts exist
**When** the admin assigns Customer A to group "VIP"
**And** the admin assigns Customer B to group "Regular"
**And** the admin assigns Customer C to group "Wholesale"
**Then** the admin panel shows Customer A in group "VIP"
**And** the admin panel shows Customer B in group "Regular"
**And** the admin panel shows Customer C in group "Wholesale"

---

## Acceptance Criteria

- [ ] Each customer is shown in their assigned group in the admin panel
- [ ] Group assignment is saved without errors
- [ ] Group-specific discounts apply when those customers log in
- [ ] All test accounts are deleted via API after the test
