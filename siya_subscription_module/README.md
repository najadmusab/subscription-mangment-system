# Siya's Module — Products, Variants, Plans & Subscriptions

This is the product/plan/subscription feature module for the shared Subscript Subscription Management System.

## Features
- Product catalogue: create, list, view, update and archive products.
- Product variants with optional additional price (example: Odoo add-on +560).
- Subscription plans with daily, weekly, monthly or yearly billing intervals.
- Plan minimum quantity, availability dates, auto-close and pause/renew settings.
- Subscription creation with customer, plan, dates, quantities and line items.
- Subscription lifecycle: DRAFT → QUOTATION → CONFIRMED → ACTIVE → CLOSED.
- Pause, resume, cancel/close and renew operations.
- Customer ownership enforced from the logged-in account; customers cannot choose another customer ID.
- ADMIN/STAFF manage catalogue and subscriptions; CUSTOMER can view only their own subscriptions.

## Shared project assumptions
- Java 17+, Spring Boot 3.x, Maven, MySQL 8+
- Shared package root: `com.subscript`
- Uses Nazmin's shared `com.subscript.auth.entity.User` and `UserRepository`.
- This ZIP is a feature module, not a separate Spring Boot application. It intentionally contains no `pom.xml`, main application class or replacement database configuration.

## Merge instructions
1. Copy `src/main/java/com/subscript/product`, `plan`, and `subscription` into the shared project's `backend/src/main/java/com/subscript/` (adjust root path to your team's actual backend).
2. Ensure the shared project already has Spring Web, Spring Data JPA, Spring Security and Validation dependencies.
3. Ensure the shared `User` entity exists at `com.subscript.auth.entity.User`.
4. Keep the team's one existing `pom.xml`, `application.properties`, `@SpringBootApplication` class and database.
5. If the shared project uses a different User package or class name, coordinate and update the imports/relationship before compiling. Do not create another User entity.

## API endpoints

### Products (`/api/products`)
- `GET /api/products` — ADMIN/STAFF list active products
- `GET /api/products/{id}` — ADMIN/STAFF view product
- `POST /api/products` — ADMIN/STAFF create product
- `PUT /api/products/{id}` — ADMIN/STAFF update product
- `PATCH /api/products/{id}/archive` — ADMIN archive product

Product example:
```json
{
  "name": "Odoo Software",
  "type": "SOFTWARE",
  "salesPrice": 1200,
  "costPrice": 450,
  "notes": "Business management software",
  "variants": [
    { "name": "Extra module", "additionalPrice": 560, "active": true }
  ]
}
```

### Plans (`/api/plans`)
- `GET /api/plans` — ADMIN/STAFF list plans
- `GET /api/plans/{id}` — ADMIN/STAFF view plan
- `POST /api/plans` — ADMIN/STAFF create plan
- `PUT /api/plans/{id}` — ADMIN/STAFF update plan
- `PATCH /api/plans/{id}/archive` — ADMIN archive plan

Plan example:
```json
{
  "name": "Annual Business",
  "billingInterval": "YEARLY",
  "intervalCount": 1,
  "minimumQuantity": 1,
  "availableFrom": "2026-01-01",
  "availableUntil": "2027-12-31",
  "autoClose": false,
  "allowPause": true,
  "allowRenew": true,
  "description": "Annual software subscription"
}
```

`billingInterval` allowed values: `DAILY`, `WEEKLY`, `MONTHLY`, `YEARLY`. `intervalCount` means every N of that interval (e.g. every 3 months).

### Subscriptions (`/api/subscriptions`)
- `GET /api/subscriptions` — ADMIN/STAFF list all; CUSTOMER sees own only
- `GET /api/subscriptions/{id}` — ADMIN/STAFF view any; CUSTOMER only own
- `POST /api/subscriptions` — ADMIN/STAFF create a subscription
- `POST /api/subscriptions/{id}/confirm` — ADMIN/STAFF confirm quotation
- `POST /api/subscriptions/{id}/activate` — ADMIN/STAFF activate confirmed subscription
- `POST /api/subscriptions/{id}/pause` — ADMIN/STAFF or owning CUSTOMER, if plan allows pause
- `POST /api/subscriptions/{id}/resume` — ADMIN/STAFF or owning CUSTOMER
- `POST /api/subscriptions/{id}/close` — ADMIN/STAFF or owning CUSTOMER
- `POST /api/subscriptions/{id}/renew` — ADMIN/STAFF or owning CUSTOMER, if plan allows renew

Subscription example:
```json
{
  "customerId": 3,
  "planId": 2,
  "startDate": "2026-11-01",
  "paymentTermsDays": 7,
  "notes": "Annual renewal",
  "lines": [
    { "productId": 1, "variantId": 1, "quantity": 2, "unitPrice": 1200 }
  ]
}
```

For CUSTOMER sessions, omit `customerId`; the server automatically assigns the signed-in user. For staff/admin, provide `customerId`.

## Important billing integration notes
- This module stores subscription line totals before tax. The billing/invoice module owned by Najad should calculate taxes, discounts, invoices and payment state.
- Subscription line `unitPrice` is snapshotted when the subscription is created so later product price edits do not silently change an existing subscription.
- This module does not create invoices or take payments.
- The billing scheduler should query ACTIVE subscriptions due for billing using `nextBillingDate`. Coordinate the exact schedule with Najad.
- `nextBillingDate` is initialized from start date and advanced by the plan interval on renewal; production billing should update it transactionally after a successful invoice creation to prevent duplicate invoices.

## Testing checklist
- [ ] Product creation and update validation works.
- [ ] Variant additional price is added to product base price in UI/business calculations.
- [ ] Plan rejects invalid date ranges and non-positive quantities.
- [ ] Invalid billing interval is rejected.
- [ ] Subscription create rejects archived products/plans and dates outside plan availability.
- [ ] Customer cannot create a subscription for another user.
- [ ] Customer cannot view another customer's subscription.
- [ ] Only allowed lifecycle transitions succeed.
- [ ] Pause is rejected when plan disallows pause.
- [ ] Renew is rejected when plan disallows renew.
- [ ] Archived products/plans cannot be used for new subscriptions.
- [ ] Invoice generation is integrated and tested with Najad's module.
