# TC-118: Tax Exemption for Specific Customer Groups

**ID:** TC-118
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** low
**Tags:** @admin @tax @groups

---

## Feature

Group-Based Tax Exemption

## User Story

As a shop admin, I want Wholesale customers to be tax-exempt so they see net prices without VAT.

## Background

Wholesale group has tax exemption enabled. Product gross price: £120.00 (net £100.00, 20% VAT).

## Scenario

**Given** a product has a gross price of "£120.00" (inclusive of 20% VAT)
**When** a Wholesale customer (tax-exempt) views the product
**Then** the price displayed is "£100.00" (net, tax-free)
**When** a Regular customer views the same product
**Then** the price displayed is "£120.00" (gross, with tax)

---

## Acceptance Criteria

- [ ] Wholesale customer sees net price (£100.00)
- [ ] Regular customer sees gross price (£120.00)
- [ ] Cart tax line is zero or absent for Wholesale customers
