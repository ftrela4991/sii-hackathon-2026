# TC-120: Assign Countries to Shipping Zones

**ID:** TC-120
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** low
**Tags:** @admin @geo

---

## Feature

Country-to-Zone Assignment

## User Story

As a shop admin, I want to assign Germany and France to the "Europe" shipping zone so zone-based carriers apply correctly.

## Scenario

**Given** the admin assigns Germany and France to zone "Europe"
**And** a carrier configured for zone "Europe" is active
**When** a German customer reaches the shipping step at checkout
**Then** the Europe-zone carrier is listed as an option

---

## Acceptance Criteria

- [ ] Germany and France are assigned to zone "Europe"
- [ ] Europe-zone carrier appears for German customers
- [ ] Non-European customers do not see the Europe-zone carrier
