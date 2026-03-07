# TC-096: Assign Existing Customers to Customer Groups

**ID:** TC-096
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** medium
**Tags:** @admin @groups

---

## Feature

Customer-to-Group Assignment

## User Story

As a shop admin, I want to assign individual customers to groups so they receive the appropriate pricing and discounts.

## Background

VIP and Wholesale groups exist. Two customer accounts exist.

## Scenario

**Given** the admin opens Customer A's profile in the admin panel
**When** the admin changes the group to "VIP" and saves
**Then** Customer A's profile shows group "VIP"
**When** the admin assigns Customer B to "Wholesale" and saves
**Then** Customer B's profile shows group "Wholesale"

---

## Acceptance Criteria

- [ ] Group assignment is saved for Customer A
- [ ] Group assignment is saved for Customer B
- [ ] Each customer's group is reflected in the admin panel
- [ ] Group discounts are applied when those customers log in
