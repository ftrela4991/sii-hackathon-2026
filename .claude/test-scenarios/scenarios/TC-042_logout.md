# TC-042: Customer Logs Out Successfully

**ID:** TC-042
**Category:** ACCOUNT
**Type:** positive
**Priority:** high
**Tags:** @account @session

---

## Feature

Customer Session Termination

## User Story

As a logged-in customer, I want to log out so that my session is securely terminated.

## Background

The customer is logged in.

## Scenario

**Given** the customer is logged in and on any page
**When** the customer clicks "Sign out" in the header
**Then** the session is terminated
**And** the header shows "Sign in" instead of the customer's name
**And** the customer is redirected to the homepage or login page

---

## Acceptance Criteria

- [ ] "Sign out" is visible in the header when logged in
- [ ] Session is destroyed after logout
- [ ] "Sign in" link appears after logout
- [ ] Accessing `/my-account` after logout redirects to login
