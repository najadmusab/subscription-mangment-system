# Integration contract for Siya + the rest of the team

1. Use one shared User entity: `com.subscript.auth.entity.User` (Nazmin's module).
2. Use these REST resources exactly: `/api/products`, `/api/plans`, `/api/subscriptions`.
3. Prices use `BigDecimal` and are expressed in the currency configured by the team. Agree the currency code globally; this module does not hard-code one.
4. Subscription `subtotal` is the pre-tax sum of quantity × unit price. Tax and discounts are calculated by Najad's invoice module, not here.
5. The invoice module should reference `Subscription.id` and `Subscription.subscriptionNumber`. Do not add invoice fields to this entity without coordinating.
6. Billing scheduler should select `status=ACTIVE` and `nextBillingDate <= today`, create one invoice idempotently, then advance nextBillingDate only after invoice creation succeeds. Use a unique invoice reference/period constraint in the invoice module to prevent duplicate billing.
7. Subscription `unitPrice` is a snapshot. Updating a Product's sales price does not alter existing subscription lines.
8. All modules must share the same Maven project, DB, main class and `application.properties`.
