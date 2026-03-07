# TC-016: Products with Different VAT Rates (23%, 8%, 0%)

**ID:** TC-016
**Category:** BASIC
**Type:** positive
**Priority:** high
**Tags:** @admin @tax @pricing

---

## Feature

Multiple VAT Rate Verification

## User Story

As a shop admin, I want different tax rates applied to different products so that the cart shows the correct tax breakdown.

## Background

Tax rates 23%, 8%, and 0% are configured. Three products exist with net price £100.00 each, each assigned a different rate.

## Scenario

**Given** the admin assigns 23% VAT to Product A (net price £100.00)
**And** the admin assigns 8% VAT to Product B (net price £100.00)
**And** the admin assigns 0% VAT to Product C (net price £100.00)
**When** a customer adds all three products to the cart
**Then** the cart shows Product A with gross price "£123.00"
**And** the cart shows Product B with gross price "£108.00"
**And** the cart shows Product C with gross price "£100.00"
**And** the tax summary section lists the correct tax amounts per rate

---

## Acceptance Criteria

- [ ] Each product price includes the correct VAT amount
- [ ] Tax breakdown section is present in the cart
- [ ] Total tax amount equals the sum of individual product taxes
- [ ] All three test products are deleted via API after the test
