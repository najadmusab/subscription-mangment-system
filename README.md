# Najad's Module — Billing, Invoices, Taxes, Discounts, Payments & Reports

This module completes the billing side of the shared Brightloop / Subscript Subscription Management System.

## Included features
- Invoice creation from subscriptions and recurring billing periods.
- Invoice line snapshots, subtotal, tax, discount, total and balance due.
- Tax rates and tax calculation (percentage-based).
- Fixed-amount and percentage discounts with date windows, minimum purchase, minimum quantity and usage limits.
- Automatic discount application when an invoice is created, when eligible.
- Payment recording with method, amount, date, note and invoice linkage.
- Partial payments; prevents overpayment; marks invoice PAID when fully paid.
- Invoice lifecycle: DRAFT → CONFIRMED → PAID; overdue is derived from due date and unpaid balance.
- Invoice PDF endpoint.
- Dashboard/report summaries and date-range revenue reports.
- Idempotent recurring invoice generation: unique subscription + billing period start prevents duplicate invoices.
- Role-aware access: ADMIN/STAFF manage billing; CUSTOMER sees only their own invoices and payments.

## Shared project assumptions
- Java 17+, Spring Boot 3.x, Maven, MySQL 8+
- Shared package root: `com.subscript`
- Uses Siya's entities:
  - `com.subscript.subscription.entity.Subscription`
  - `com.subscript.subscription.entity.SubscriptionLine`
  - `com.subscript.subscription.entity.SubscriptionStatus`
- Uses Nazmin's shared authentication:
  - `com.subscript.auth.entity.User`
  - `com.subscript.auth.entity.Role`
  - `com.subscript.auth.service.CurrentUserService`
- This is a feature module, not a standalone Spring Boot app. No separate `pom.xml`, main application class, user entity, or database config is included.

## Merge instructions
1. Copy `src/main/java/com/subscript/billing` into the shared backend's `src/main/java/com/subscript/`.
2. Copy `src/main/java/com/subscript/report` into the same shared Java source root.
3. Copy `src/main/resources/db/migration/V1__billing_module.sql` only if your team uses Flyway. Otherwise use the JPA schema update during development and create a proper migration before final deployment.
4. Add the dependencies listed below to the ONE shared `pom.xml`.
5. Merge the sample properties into the ONE shared `application.properties`.
6. Do not create another `@SpringBootApplication`, `pom.xml`, User entity, or database.
7. Compile and test after Siya's and Nazmin's modules are merged.

## Extra Maven dependency for PDF
This module uses OpenPDF for invoice PDF generation. Add to the shared `pom.xml`:
```xml
<dependency>
  <groupId>com.github.librepdf</groupId>
  <artifactId>openpdf</artifactId>
  <version>2.0.3</version>
</dependency>
```
The shared project also needs Spring Web, Spring Data JPA, Spring Security, Validation and the MySQL driver. Do not duplicate dependencies if they already exist.

## Sample properties
```properties
# Keep your team's existing datasource settings.
spring.jpa.hibernate.ddl-auto=update
spring.jpa.open-in-view=false

# Set the currency used by the entire application.
app.billing.currency=INR
app.billing.invoice-prefix=INV
app.billing.default-due-days=7
```

## API endpoints

### Invoices (`/api/invoices`)
- `GET /api/invoices` — ADMIN/STAFF all invoices; CUSTOMER own invoices only
- `GET /api/invoices/{id}` — invoice detail, ownership enforced
- `POST /api/invoices/from-subscription/{subscriptionId}` — create invoice for the subscription's current `nextBillingDate` (ADMIN/STAFF)
- `POST /api/invoices/{id}/confirm` — confirm draft invoice (ADMIN/STAFF)
- `GET /api/invoices/{id}/pdf` — download invoice PDF, ownership enforced

### Recurring billing (`/api/billing`)
- `POST /api/billing/run-due` — generate invoices for due active subscriptions (ADMIN only)
- `GET /api/billing/due-count` — count due subscriptions (ADMIN/STAFF)

The recurring job can be called manually from an admin UI or scheduled with Spring `@Scheduled` after enabling scheduling in the shared main application. This module does not add a second scheduler or main class.

### Taxes (`/api/taxes`)
- `GET /api/taxes` — ADMIN/STAFF list active tax rates
- `POST /api/taxes` — ADMIN create tax rate
- `PUT /api/taxes/{id}` — ADMIN update tax rate
- `PATCH /api/taxes/{id}/archive` — ADMIN archive tax rate

Tax example:
```json
{ "name": "GST", "ratePercent": 18.00, "active": true }
```

### Discounts (`/api/discounts`)
- `GET /api/discounts` — ADMIN/STAFF list discounts
- `POST /api/discounts` — ADMIN create discount
- `PUT /api/discounts/{id}` — ADMIN update discount
- `PATCH /api/discounts/{id}/archive` — ADMIN archive discount

Discount example:
```json
{
  "name": "WELCOME10",
  "type": "PERCENTAGE",
  "value": 10,
  "minimumPurchase": 500,
  "minimumQuantity": 1,
  "startsAt": "2026-01-01T00:00:00",
  "endsAt": "2027-01-01T00:00:00",
  "usageLimit": 100,
  "active": true
}
```

`type` is `FIXED` or `PERCENTAGE`. The discount is selected automatically by the billing service from active, currently eligible discounts; the largest eligible discount is applied. A production UI should allow the admin to choose a specific discount code if the business requires that workflow.

### Payments (`/api/payments`)
- `GET /api/payments` — ADMIN/STAFF all; CUSTOMER own only
- `GET /api/payments/{id}` — ownership enforced
- `POST /api/payments` — ADMIN/STAFF record a payment
- `GET /api/invoices/{invoiceId}/payments` — list payments for invoice

Payment example:
```json
{
  "invoiceId": 12,
  "method": "BANK_TRANSFER",
  "amount": 1416.00,
  "paidAt": "2026-10-31T10:30:00",
  "reference": "BANK-REF-0081",
  "note": "October subscription payment"
}
```

Payment methods: `CASH`, `CARD`, `BANK_TRANSFER`, `UPI`, `OTHER`.

### Reports (`/api/reports`)
- `GET /api/reports/dashboard` — active subscription count, invoice totals, overdue count, paid revenue and outstanding balance
- `GET /api/reports/revenue?from=2026-01-01&to=2026-12-31` — paid revenue grouped by payment date

## Billing rules and calculation order
1. Subscription lines are copied as invoice line snapshots; product edits do not change existing invoices.
2. Subtotal = sum of quantity × unit price.
3. Discount is calculated on subtotal, capped so it cannot make the invoice subtotal negative.
4. Tax is calculated on the discounted taxable amount.
5. Total = subtotal − discount + tax.
6. Due date = invoice date + configured default due days (or subscription payment terms if provided).
7. Payments reduce the balance due. When balance reaches zero, invoice status becomes PAID.
8. A payment cannot be zero, negative or greater than the remaining balance.
9. Recurring invoice creation uses a unique `(subscription_id, billing_period_start)` key. A second attempt for the same billing period returns the existing invoice rather than duplicating it.
10. The subscription's next billing date advances only after invoice creation succeeds.

## Important integration notes
- Siya's subscription subtotal is pre-tax and pre-discount. This module owns invoice calculations.
- The current Siya subscription entity has `nextBillingDate`, plan interval and `paymentTermsDays`; this module uses those fields.
- The recurring billing endpoint is ADMIN-only. For production, call it from a scheduled process or protected admin action.
- Tax is applied using the first active tax rate returned by the repository. If your business needs tax by product/customer/location, extend the rule and store the applied tax snapshot on invoice lines.
- Discounts are automatically selected from eligible discounts by highest computed discount amount. If your hackathon judges expect discount code entry, add a code field and explicit selection UI.
- This module does not connect to a real payment gateway. It records payments entered by staff/admin.
- The PDF uses OpenPDF. If your team cannot add that dependency, remove `InvoicePdfService` and the PDF controller endpoint or replace it with another approved PDF library.

## Testing checklist
- [ ] Invoice creation copies subscription line descriptions, quantity and unit price.
- [ ] Tax and discount amounts calculate correctly to 2 decimal places.
- [ ] Discount is rejected when date, minimum purchase, quantity or usage limit is not met.
- [ ] Discount usage increments only when invoice creation succeeds.
- [ ] Duplicate recurring billing call does not create a second invoice.
- [ ] Next billing date advances only after successful invoice creation.
- [ ] Draft invoice cannot accept a payment until confirmed.
- [ ] Partial payment reduces balance.
- [ ] Exact final payment changes status to PAID.
- [ ] Overpayment and zero/negative payment are rejected.
- [ ] Customers cannot view another customer's invoices or payments.
- [ ] PDF is downloadable for an authorized invoice.
- [ ] Dashboard totals match invoice/payment records.
