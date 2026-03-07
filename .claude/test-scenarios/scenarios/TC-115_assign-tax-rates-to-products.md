# TC-115: Assign Tax Rate to Products

**ID:** TC-115
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** high
**Tags:** @admin @tax

---

## Feature

Product Tax Rate Assignment

## User Story

As a shop admin, I want to assign a tax rule to a product so the correct VAT amount is displayed and charged.

## Background

A 20% tax rate rule is configured in the admin panel.

## Scenario

**Given** the admin assigns a 20% tax rule to a product with net price "£100.00"
**When** a customer views the product (prices shown with tax)
**Then** the displayed price is "£120.00" (including 20% VAT)
**And** the cart summary shows the tax amount "£20.00"

---

## Acceptance Criteria

- [ ] Gross price includes the correct VAT amount
- [ ] Tax amount is broken out in the cart summary
- [ ] Product is deleted via API after the test
