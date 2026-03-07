# TC-108: Carriers Available Only for Specific Countries/Zones

**ID:** TC-108
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** low
**Tags:** @admin @shipping @geo

---

## Feature

Zone-Restricted Carrier Availability

## User Story

As a shop admin, I want Express Delivery to be available only within the Europe zone so it only appears for relevant customers.

## Background

Express Delivery is restricted to Zone "Europe". Germany is in Zone "Europe". USA is not.

## Scenario

**Given** a customer has a German delivery address (Europe zone)
**Then** Express Delivery is available at the shipping step
**When** a customer has a US delivery address (outside Europe zone)
**Then** Express Delivery is not listed at the shipping step

---

## Acceptance Criteria

- [ ] Express Delivery appears for European delivery addresses
- [ ] Express Delivery does not appear for US delivery addresses
- [ ] Standard carriers available for all zones remain unaffected
