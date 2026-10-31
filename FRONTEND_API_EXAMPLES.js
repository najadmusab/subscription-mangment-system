// All API calls are same-origin when the frontend is served by Spring Boot.
// Use the CSRF cookie/header helper from Nazmin's authentication integration for POST requests.
async function api(url, options={}) {
  const response = await fetch(url, { credentials: "same-origin", ...options,
    headers: { "Content-Type": "application/json", ...(options.headers || {}) } });
  if (!response.ok) {
    const body = await response.json().catch(() => ({}));
    throw new Error(body.message || `Request failed (${response.status})`);
  }
  return response.status === 204 ? null : response.json();
}
const invoices = () => api("/api/invoices");
const payments = () => api("/api/payments");
const dashboardReport = () => api("/api/reports/dashboard");
const revenueReport = (from,to) => api(`/api/reports/revenue?from=${from}&to=${to}`);
const createInvoice = subscriptionId => api(`/api/invoices/from-subscription/${subscriptionId}`, {method:"POST"});
const confirmInvoice = invoiceId => api(`/api/invoices/${invoiceId}/confirm`, {method:"POST"});
const recordPayment = payload => api("/api/payments", {method:"POST",body:JSON.stringify(payload)});
// Payment payload example:
// {invoiceId: 12, method:"UPI", amount:1416, paidAt:"2026-10-31T10:30:00", reference:"UPI-REF-1", note:"Paid"}
