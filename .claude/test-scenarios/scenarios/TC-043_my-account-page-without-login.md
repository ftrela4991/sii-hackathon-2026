# TC-043: My Account Page Requires Authentication

**ID:** TC-043
**Category:** ACCOUNT
**Type:** negative
**Priority:** high
**Tags:** @account @security

---

## Feature

My Account Access Control

## User Story

As a visitor, accessing the My Account page without being logged in must redirect me to the login page.

## Scenario

**Given** the user is not logged in
**When** the user navigates to `/my-account`
**Then** the user is redirected to the login page `/login`
**And** the My Account content is not displayed

---

## Acceptance Criteria

- [ ] Unauthenticated access to `/my-account` redirects to `/login`
- [ ] Account details are not visible to unauthenticated users
- [ ] No 5xx error occurs
