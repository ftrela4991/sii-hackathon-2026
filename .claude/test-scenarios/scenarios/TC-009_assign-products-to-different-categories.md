# TC-009: Assign Products to Different Categories

**ID:** TC-009
**Category:** BASIC
**Type:** positive
**Priority:** medium
**Tags:** @admin @product @category

---

## Feature

Product Category Assignment

## User Story

As a shop admin, I want to assign products to specific categories so that customers can browse them correctly.

## Background

Categories "Clothes > Men" and "Accessories > Stationery" exist in the store.

## Scenario

**Given** the admin creates product "Category Test A <uuid>" assigned to category "Clothes > Men"
**And** the admin creates product "Category Test B <uuid>" assigned to category "Accessories > Stationery"
**When** a customer navigates to the Men category page `/4-men`
**Then** "Category Test A" is listed on the page
**When** a customer navigates to the Stationery category page `/7-stationery`
**Then** "Category Test B" is listed on the page

---

## Acceptance Criteria

- [ ] Product A appears only in the Men category
- [ ] Product B appears only in the Stationery category
- [ ] Products do not appear in each other's categories
- [ ] Both products are deleted via API after the test
