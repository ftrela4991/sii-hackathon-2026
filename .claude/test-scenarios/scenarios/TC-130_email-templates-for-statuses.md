# TC-130: Email Templates Configured for Order Statuses

**ID:** TC-130
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** low
**Tags:** @admin @orders @email

---

## Feature

Order Status Email Notification

## User Story

As a shop admin, I want order status changes to trigger the correct notification email so customers are kept informed automatically.

## Background

The "Processing" status is configured to send a notification email. An order exists in "Awaiting cheque payment" status.

## Scenario

**Given** the admin has an order in "Awaiting cheque payment" status
**When** the admin changes the status to "Processing"
**Then** the system sends a notification email to the customer's registered email address
**And** the email content corresponds to the configured "Processing" email template

---

## Acceptance Criteria

- [ ] Status change triggers an email send
- [ ] Email is sent to the customer's correct address
- [ ] Email content matches the "Processing" template (subject and body)
- [ ] Email is logged or visible in the order's message history
