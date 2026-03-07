# TC-117: Prices Displayed as Gross vs Net Based on Configuration

**ID:** TC-117
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** medium
**Tags:** @admin @tax @pricing

---

## Feature

Gross vs Net Price Display Configuration

## User Story

As a shop admin, I want to control whether prices include or exclude tax so customers see the correct pricing format.

## Background

Product net price: £100.00 with 20% tax.

## Scenario

**Given** the admin sets "Display prices with tax included" = Yes
**When** a customer views the product
**Then** the price shown is "£120.00" (gross, inclusive of VAT)
**When** the admin sets "Display prices with tax included" = No
**Then** the price shown is "£100.00" (net) with tax noted separately in the cart

---

## Acceptance Criteria

- [ ] Gross price mode shows £120.00 on the product page
- [ ] Net price mode shows £100.00 with tax itemised in the cart
- [ ] Switching the setting is reflected immediately on the storefront
