# Integration contract — Najad billing module

## Required shared types
- Nazmin: `com.subscript.auth.entity.User`, `Role`, `com.subscript.auth.service.CurrentUserService`.
- Siya: `com.subscript.subscription.entity.Subscription`, `SubscriptionStatus`, `com.subscript.subscription.repository.SubscriptionRepository`.
- Siya's Subscription must expose `getCustomer()`, `getPlan()`, `getLines()`, `getStartDate()`, `getNextBillingDate()`, `setNextBillingDate()`, `getStatus()`, `getPaymentTermsDays()`, `getNotes()`.
- Subscription customer User must expose `getId()`, `getFullName()`, `getEmail()`.
- Plan must expose billing interval and interval count.

## Shared rules
1. Keep one shared Maven project, MySQL database, Spring Boot application and application.properties.
2. Invoice lines are snapshots. Do not recalculate old invoices from current product prices.
3. All monetary values use BigDecimal. UI should show the configured currency.
4. Taxes/discounts apply when the invoice is created; changing a tax or discount later does not rewrite old invoices.
5. Payment entry records a payment; it is not a gateway integration.
6. Invoice creation and nextBillingDate advancement run in one transaction.
7. The unique subscription/billing-period constraint protects against duplicate invoices. If two billing workers can run concurrently in production, add database locking on the subscription row or a dedicated distributed scheduler lock.
8. If the team wants an explicit customer-selected discount code, extend Discount with a unique code and pass it to invoice creation. The current hackathon default automatically chooses the highest eligible discount.
9. Current tax rule chooses the first active tax rate. For multiple tax jurisdictions or tax per product, implement a tax-selection rule before demo day.
