# TC-119: Activate Countries: Poland, Germany, UK, USA

**ID:** TC-119
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** low
**Tags:** @admin @geo

---

## Feature

Country Activation for Checkout

## User Story

As a shop admin, I want to enable specific countries so customers from those regions can enter their delivery address.

## Scenario

**Given** the admin activates Poland, Germany, United Kingdom, and United States in the Localisation > Countries section
**When** a customer fills in the address form at checkout
**Then** the Country dropdown includes "Poland", "Germany", "United Kingdom", and "United States"

---

## Acceptance Criteria

- [ ] All 4 countries appear in the country dropdown
- [ ] Countries are selectable and the form accepts them
- [ ] Non-activated countries are not shown in the dropdown
