// Use these examples in the shared frontend. All requests are same-origin.
// If the shared app uses Spring Security CSRF, use the CSRF helper supplied
// by the authentication module before POST/PUT/PATCH requests.
async function loadProducts() {
  const r = await fetch("/api/products", { credentials: "same-origin" });
  if (!r.ok) throw new Error("Could not load products");
  return r.json();
}
async function loadPlans() {
  const r = await fetch("/api/plans", { credentials: "same-origin" });
  if (!r.ok) throw new Error("Could not load plans");
  return r.json();
}
async function loadSubscriptions() {
  const r = await fetch("/api/subscriptions", { credentials: "same-origin" });
  if (!r.ok) throw new Error("Could not load subscriptions");
  return r.json();
}
// Example create subscription body:
// {
//   customerId: 3, // omit for a CUSTOMER session
//   planId: 2,
//   startDate: "2026-11-01",
//   paymentTermsDays: 7,
//   notes: "New annual plan",
//   lines: [{ productId: 1, variantId: null, quantity: 1, unitPrice: 1200 }]
// }
