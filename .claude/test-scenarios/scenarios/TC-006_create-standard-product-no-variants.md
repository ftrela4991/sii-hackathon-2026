# TC-006: Create Standard Product (No Variants)

**ID:** TC-006
**Category:** BASIC
**Type:** positive
**Priority:** high
**Tags:** @admin @product

---

## Feature

Create Simple Product Without Combinations

## User Story

As a shop admin, I want to create a simple product with no combinations so that customers can view and purchase it.

## Background

Admin is logged into the admin panel at `/admin_hackathon`.

## Scenario

**Given** the admin is on the product creation page in the admin panel
**When** the admin sets the product name to "Test Simple Product <uuid>"
**And** the admin sets the retail price to "29.99"
**And** the admin sets the stock quantity to "50"
**And** the admin saves the product
**Then** the product appears in the admin product list
**And** the product is visible on the storefront
**And** the product detail page displays the price "£29.99"

---

## Acceptance Criteria

- [ ] Product is saved without errors
- [ ] Product appears in storefront with correct price
- [ ] No variants or combination selectors are shown on the product page
- [ ] Product is deleted via API after the test
