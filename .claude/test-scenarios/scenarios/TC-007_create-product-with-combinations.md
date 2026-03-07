# TC-007: Create Product with Attribute Combinations

**ID:** TC-007
**Category:** BASIC
**Type:** positive
**Priority:** high
**Tags:** @admin @product @combinations

---

## Feature

Create Product with Size and Colour Combinations

## User Story

As a shop admin, I want to create a product with size and colour combinations so that customers can choose the exact variant they want.

## Background

Admin is logged into the admin panel.

## Scenario

**Given** the admin is on the product creation page
**When** the admin sets the product name to "Test Combo Product <uuid>"
**And** the admin adds attribute "Size" with values "S", "M", "L"
**And** the admin adds attribute "Colour" with values "Red", "Blue"
**And** the admin generates all combinations
**And** the admin sets a default price of "49.99"
**And** the admin saves the product
**Then** 6 combinations are listed (S/Red, S/Blue, M/Red, M/Blue, L/Red, L/Blue)
**And** the product detail page shows a Size selector and a Colour selector

---

## Acceptance Criteria

- [ ] All 6 combinations are generated
- [ ] Size and Colour dropdowns are present on the product page
- [ ] Selecting a combination does not cause a page error
- [ ] Product is deleted via API after the test
