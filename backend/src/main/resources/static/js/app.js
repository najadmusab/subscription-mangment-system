/* Subscript frontend — Shafiya's UI module.
   Demo data is local to this browser. Replace API calls in apiRequest() when backend is ready. */
const state = {
  page: "overview",
  customers: [
    {name:"Olivia Rhye",email:"olivia@acme.com",company:"Acme Studio",initials:"OR",plan:"Growth Annual",amount:"$2,400.00",status:"Active"},
    {name:"Phoenix Baker",email:"phoenix@acme.com",company:"Phoenix Labs",initials:"PB",plan:"Starter Monthly",amount:"$49.00",status:"Active"},
    {name:"Lana Steiner",email:"lana@cloudline.io",company:"Cloudline",initials:"LS",plan:"Business Annual",amount:"$5,880.00",status:"Active"},
    {name:"Demi Wilkinson",email:"demi@layers.io",company:"Layers",initials:"DW",plan:"Growth Monthly",amount:"$199.00",status:"Trial"},
    {name:"Candice Wu",email:"candice@bright.io",company:"Bright Co.",initials:"CW",plan:"Starter Monthly",amount:"$49.00",status:"Paused"}
  ],
  subscriptions: [
    {id:"SUB-1048",customer:"Olivia Rhye",plan:"Growth Annual",start:"Jan 12, 2026",renewal:"Jan 12, 2027",amount:"$2,400.00",status:"Active"},
    {id:"SUB-1047",customer:"Phoenix Baker",plan:"Starter Monthly",start:"Oct 02, 2026",renewal:"Nov 02, 2026",amount:"$49.00",status:"Active"},
    {id:"SUB-1046",customer:"Lana Steiner",plan:"Business Annual",start:"Sep 18, 2026",renewal:"Sep 18, 2027",amount:"$5,880.00",status:"Active"},
    {id:"SUB-1045",customer:"Demi Wilkinson",plan:"Growth Monthly",start:"Oct 20, 2026",renewal:"Nov 20, 2026",amount:"$199.00",status:"Trial"},
    {id:"SUB-1044",customer:"Candice Wu",plan:"Starter Monthly",start:"Aug 11, 2026",renewal:"—",amount:"$49.00",status:"Paused"}
  ],
  invoices: [
    {id:"INV-2084",customer:"Olivia Rhye",date:"Oct 28, 2026",due:"Nov 04, 2026",amount:"$200.00",status:"Paid"},
    {id:"INV-2083",customer:"Phoenix Baker",date:"Oct 27, 2026",due:"Nov 03, 2026",amount:"$49.00",status:"Pending"},
    {id:"INV-2082",customer:"Lana Steiner",date:"Oct 24, 2026",due:"Oct 30, 2026",amount:"$490.00",status:"Overdue"},
    {id:"INV-2081",customer:"Demi Wilkinson",date:"Oct 22, 2026",due:"Oct 29, 2026",amount:"$199.00",status:"Paid"},
    {id:"INV-2080",customer:"Candice Wu",date:"Oct 19, 2026",due:"Oct 26, 2026",amount:"$49.00",status:"Draft"}
  ],
  payments: [
    {id:"PAY-3092",customer:"Olivia Rhye",date:"Oct 28, 2026",method:"Visa •••• 4242",amount:"$200.00",status:"Paid"},
    {id:"PAY-3091",customer:"Demi Wilkinson",date:"Oct 22, 2026",method:"Bank transfer",amount:"$199.00",status:"Paid"},
    {id:"PAY-3090",customer:"Phoenix Baker",date:"Oct 20, 2026",method:"Mastercard •••• 8210",amount:"$49.00",status:"Failed"},
    {id:"PAY-3089",customer:"Lana Steiner",date:"Oct 18, 2026",method:"Bank transfer",amount:"$490.00",status:"Paid"}
  ],
  products: [
    {name:"Subscript Core",type:"Software",price:"$49.00",billing:"Monthly",status:"Published"},
    {name:"Advanced Analytics",type:"Add-on",price:"$19.00",billing:"Monthly",status:"Published"},
    {name:"Priority Support",type:"Add-on",price:"$99.00",billing:"Yearly",status:"Published"}
  ],
  plans: [
    {name:"Starter Monthly",period:"Monthly",price:"$49.00",subscribers:"128",status:"Active"},
    {name:"Growth Annual",period:"Yearly",price:"$2,400.00",subscribers:"64",status:"Active"},
    {name:"Business Annual",period:"Yearly",price:"$5,880.00",subscribers:"22",status:"Active"}
  ]
};

const pageMeta = {
  overview:["Overview","A clear view of how your subscription business is performing."],
  customers:["Customers","Manage customer records and understand account activity."],
  products:["Products","Manage the products and add-ons available to customers."],
  plans:["Plans","Configure recurring plans, billing cycles and pricing."],
  subscriptions:["Subscriptions","Track subscription lifecycle, renewals and customer plans."],
  invoices:["Invoices","Review invoices and keep billing activity on track."],
  payments:["Payments","Monitor collected payments and payment attempts."],
  reports:["Reports & analytics","Understand recurring revenue, customer growth and billing performance."],
  settings:["Settings","Manage workspace preferences and account configuration."]
};

function money(n){return n}
function statusPill(value){return `<span class="status ${value.toLowerCase()}">${value}</span>`}
function heading(page, actionLabel, actionType){
  const [title,desc]=pageMeta[page];
  return `<div class="page-heading"><div><p class="eyebrow">BRIGHTLOOP SOFTWARE / WORKSPACE</p><h1>${title}</h1><p>${desc}</p></div><div class="heading-actions"><button class="btn" data-action="export">↓ Export</button>${actionLabel?`<button class="btn btn-primary" data-action="${actionType||'create'}">＋ ${actionLabel}</button>`:""}</div></div>`;
}
function stat(label,value,change,icon,positive=true){return `<article class="stat-card"><div class="stat-top"><span>${label}</span><span class="stat-icon">${icon}</span></div><div class="stat-value">${value}</div><div class="stat-foot"><span class="${positive?'trend-up':'trend-down'}">${change}</span><span>vs. last month</span></div></article>`}
function chartPanel(){
 return `<section class="panel"><div class="panel-head"><div><h2>Revenue overview</h2><p>Monthly recurring revenue performance</p></div><select class="select-lite" id="revenueRange"><option>Last 6 months</option><option>Last 12 months</option><option>This year</option></select></div><div class="chart-wrap"><svg viewBox="0 0 650 205" preserveAspectRatio="none" role="img" aria-label="Revenue trend chart"><defs><linearGradient id="areaFill" x1="0" x2="0" y1="0" y2="1"><stop offset="0%" stop-color="#315ee8" stop-opacity=".16"/><stop offset="100%" stop-color="#315ee8" stop-opacity="0"/></linearGradient></defs><line class="chart-grid" x1="35" y1="22" x2="640" y2="22"/><line class="chart-grid" x1="35" y1="65" x2="640" y2="65"/><line class="chart-grid" x1="35" y1="108" x2="640" y2="108"/><line class="chart-grid" x1="35" y1="151" x2="640" y2="151"/><text class="chart-label" x="0" y="25">$30k</text><text class="chart-label" x="0" y="68">$20k</text><text class="chart-label" x="0" y="111">$10k</text><text class="chart-label" x="13" y="154">$0</text><path class="chart-area" d="M35 132 C75 124 83 111 135 117 S195 92 235 99 S300 78 335 87 S395 55 435 65 S500 42 535 51 S600 24 640 30 L640 151 L35 151Z"/><path class="chart-line" d="M35 132 C75 124 83 111 135 117 S195 92 235 99 S300 78 335 87 S395 55 435 65 S500 42 535 51 S600 24 640 30"/><circle class="chart-dot" cx="135" cy="117" r="4"/><circle class="chart-dot" cx="235" cy="99" r="4"/><circle class="chart-dot" cx="335" cy="87" r="4"/><circle class="chart-dot" cx="435" cy="65" r="4"/><circle class="chart-dot" cx="535" cy="51" r="4"/><circle class="chart-dot" cx="640" cy="30" r="4"/><text class="chart-label" x="35" y="177">May</text><text class="chart-label" x="150" y="177">Jun</text><text class="chart-label" x="260" y="177">Jul</text><text class="chart-label" x="370" y="177">Aug</text><text class="chart-label" x="480" y="177">Sep</text><text class="chart-label" x="610" y="177">Oct</text></svg></div><div class="chart-legend"><span><i class="legend-dot"></i>Recurring revenue</span><span><i class="legend-dot gray"></i>One-time revenue</span></div></section>`;
}
function recentInvoices(){
 return `<section class="panel table-panel"><div class="panel-head"><div><h2>Recent invoices</h2><p>Latest billing activity across your workspace</p></div><button class="btn btn-sm" data-page="invoices">View all →</button></div>${table(["INVOICE","CUSTOMER","ISSUE DATE","AMOUNT","STATUS"],state.invoices.slice(0,4).map(x=>[`<strong>${x.id}</strong>`,x.customer,x.date,x.amount,statusPill(x.status)]))}</section>`;
}
function table(headers,rows){
 return `<table class="data-table"><thead><tr>${headers.map(h=>`<th>${h}</th>`).join("")}</tr></thead><tbody>${rows.length?rows.map(r=>`<tr>${r.map(c=>`<td>${c}</td>`).join("")}</tr>`).join(""):`<tr><td colspan="${headers.length}" class="empty-note">No records found.</td></tr>`}</tbody></table>`;
}
function overview(){
 return `${heading("overview","Create subscription","createSubscription")}<div class="stats-grid">${stat("Monthly recurring revenue","$28,490","+12.8%","↗")}${stat("Active subscriptions","1,284","+8.2%","⟳")}${stat("Total customers","946","+6.4%","♙")}${stat("Outstanding invoices","$4,280","3.1%","▧",false)}</div><div class="dashboard-grid">${chartPanel()}<section class="panel"><div class="panel-head"><div><h2>Subscription health</h2><p>Current subscription status</p></div><button class="btn btn-sm" data-page="subscriptions">Details →</button></div><div class="mini-bars"><div class="mini-bar-col"><div class="mini-bar" style="height:78%"></div><small>Active</small></div><div class="mini-bar-col"><div class="mini-bar highlight" style="height:17%"></div><small>Trial</small></div><div class="mini-bar-col"><div class="mini-bar" style="height:9%"></div><small>Paused</small></div><div class="mini-bar-col"><div class="mini-bar" style="height:5%"></div><small>Closed</small></div></div><div class="source-legend"><span>Active subscriptions</span><strong>1,284</strong></div><div class="source-legend"><span>Renewal success rate</span><strong>96.8%</strong></div></section></div><div class="content-two">${recentInvoices()}<section class="panel"><div class="panel-head"><div><h2>Quick actions</h2><p>Common workspace tasks</p></div></div><div class="quick-actions"><button class="quick-action" data-action="createCustomer"><span>＋</span><div><strong>Add a customer</strong><small>Create a customer profile</small></div><b>→</b></button><button class="quick-action" data-action="createPlan"><span>▤</span><div><strong>Create a plan</strong><small>Set up recurring pricing</small></div><b>→</b></button><button class="quick-action" data-action="createInvoice"><span>▧</span><div><strong>New invoice</strong><small>Prepare a customer invoice</small></div><b>→</b></button></div></section></div>`;
}
function quickStyles(){return `<style>.quick-actions{display:flex;flex-direction:column;gap:7px}.quick-action{display:flex;align-items:center;gap:10px;border:1px solid #edf0f4;border-radius:8px;background:#fff;padding:11px;text-align:left}.quick-action>span{width:29px;height:29px;border-radius:8px;background:#edf2ff;color:#315ee8;display:grid;place-items:center}.quick-action div{display:flex;flex-direction:column;gap:3px;flex:1}.quick-action strong{font-size:10px;color:#354158}.quick-action small{font-size:9px;color:#929baa}.quick-action b{color:#9aa3b0}</style>`}
function customersPage(){
 const rows=state.customers.map(x=>[`<div class="customer-cell"><span class="customer-initial">${x.initials}</span><div><div class="customer-name">${x.name}</div><div class="customer-email">${x.email}</div></div></div>`,x.company,x.plan,x.amount,statusPill(x.status),`<button class="btn btn-sm" data-action="viewCustomer" data-name="${x.name}">View</button>`]);
 return `${heading("customers","Add customer","createCustomer")}<div class="panel table-panel"><div class="panel-head"><div><h2>All customers <span class="muted-count">(${state.customers.length})</span></h2><p>A directory of customers in your workspace</p></div><div class="table-tools"><input class="table-search" data-filter="customers" placeholder="Search customers..."><button class="btn btn-sm" data-action="export">↓ Export</button></div></div>${table(["CUSTOMER","COMPANY","PLAN","LIFETIME VALUE","STATUS",""],rows)}<div class="table-footer"><span>Showing 1–${rows.length} of ${rows.length} customers</span><div class="pagination"><button>‹</button><button class="current">1</button><button>›</button></div></div></div>`;
}
function subscriptionsPage(){
 const rows=state.subscriptions.map(x=>[`<strong>${x.id}</strong>`,x.customer,x.plan,x.start,x.renewal,x.amount,statusPill(x.status),`<button class="btn btn-sm" data-action="viewSubscription" data-id="${x.id}">View</button>`]);
 return `${heading("subscriptions","New subscription","createSubscription")}<div class="filter-row"><input data-filter="subscriptions" placeholder="Search subscriptions..."><select id="subscriptionStatus"><option>All statuses</option><option>Active</option><option>Trial</option><option>Paused</option></select><select><option>All plans</option><option>Starter Monthly</option><option>Growth Annual</option></select></div><div class="panel table-panel">${table(["REFERENCE","CUSTOMER","PLAN","START DATE","NEXT RENEWAL","AMOUNT","STATUS",""],rows)}<div class="table-footer"><span>Showing ${rows.length} subscriptions</span><div class="pagination"><button>‹</button><button class="current">1</button><button>›</button></div></div></div>`;
}
function invoicesPage(){
 const rows=state.invoices.map(x=>[`<strong>${x.id}</strong>`,x.customer,x.date,x.due,x.amount,statusPill(x.status),`<button class="btn btn-sm" data-action="viewInvoice" data-id="${x.id}">Open</button>`]);
 return `${heading("invoices","Create invoice","createInvoice")}<div class="stats-grid">${stat("Total invoiced","$18,420","+9.2%","▧")}${stat("Paid this month","$14,140","+11.4%","✓")}${stat("Awaiting payment","$2,980","5 invoices","◷")}${stat("Overdue","$1,300","2 invoices", "!",false)}</div><div class="filter-row"><input data-filter="invoices" placeholder="Search invoice or customer..."><select><option>All statuses</option><option>Paid</option><option>Pending</option><option>Overdue</option><option>Draft</option></select></div><div class="panel table-panel">${table(["INVOICE","CUSTOMER","ISSUE DATE","DUE DATE","AMOUNT","STATUS",""],rows)}<div class="table-footer"><span>Showing ${rows.length} invoices</span><div class="pagination"><button>‹</button><button class="current">1</button><button>›</button></div></div></div>`;
}
function paymentsPage(){
 const rows=state.payments.map(x=>[`<strong>${x.id}</strong>`,x.customer,x.date,x.method,x.amount,statusPill(x.status)]);
 return `${heading("payments","Record payment","createPayment")}<div class="stats-grid">${stat("Payments received","$16,840","+10.6%","⇄")}${stat("Successful payments","392","+5.8%","✓")}${stat("Failed payments","8","2.0%","!")}${stat("Average payment","$184.20","+2.4%","$")}</div><div class="filter-row"><input data-filter="payments" placeholder="Search payment or customer..."><select><option>All methods</option><option>Bank transfer</option><option>Card</option></select></div><div class="panel table-panel">${table(["PAYMENT ID","CUSTOMER","DATE","METHOD","AMOUNT","STATUS"],rows)}</div>`;
}
function productsPage(){
 const rows=state.products.map(x=>[`<strong>${x.name}</strong>`,x.type,x.price,x.billing,statusPill(x.status),`<button class="btn btn-sm" data-action="editProduct" data-name="${x.name}">Edit</button>`]);
 return `${heading("products","Add product","createProduct")}<div class="panel table-panel">${table(["PRODUCT","TYPE","SALES PRICE","BILLING","STATUS",""],rows)}<div class="table-footer"><span>${rows.length} products</span><span>Product catalogue</span></div></div>`;
}
function plansPage(){
 const rows=state.plans.map(x=>[`<strong>${x.name}</strong>`,x.period,x.price,x.subscribers,statusPill(x.status),`<button class="btn btn-sm" data-action="editPlan" data-name="${x.name}">Edit</button>`]);
 return `${heading("plans","Create plan","createPlan")}<div class="panel table-panel">${table(["PLAN NAME","BILLING PERIOD","PRICE","SUBSCRIBERS","STATUS",""],rows)}<div class="table-footer"><span>${rows.length} plans</span><span>Plan catalogue</span></div></div>`;
}
function reportsPage(){
 return `${heading("reports","Export report","export")}<div class="report-row"><section class="panel"><p class="eyebrow">MONTHLY RECURRING REVENUE</p><div class="report-metric">$28,490</div><p>Recurring subscription revenue for October 2026.</p><div class="progress-track"><div class="progress-fill" style="width:76%"></div></div></section><section class="panel"><p class="eyebrow">CUSTOMER RETENTION</p><div class="report-metric">96.8%</div><p>Customers retained over the selected period.</p><div class="progress-track"><div class="progress-fill" style="width:96.8%"></div></div></section><section class="panel"><p class="eyebrow">COLLECTION RATE</p><div class="report-metric">94.2%</div><p>Invoices paid by their due date.</p><div class="progress-track"><div class="progress-fill" style="width:94.2%"></div></div></section></div><div class="dashboard-grid">${chartPanel()}<section class="panel"><div class="panel-head"><div><h2>Revenue breakdown</h2><p>By subscription category</p></div></div><div class="source-legend"><span>Annual plans</span><strong>$17,200</strong></div><div class="progress-track"><div class="progress-fill" style="width:72%"></div></div><div class="source-legend"><span>Monthly plans</span><strong>$8,490</strong></div><div class="progress-track"><div class="progress-fill" style="width:45%"></div></div><div class="source-legend"><span>Add-ons</span><strong>$2,800</strong></div><div class="progress-track"><div class="progress-fill" style="width:20%"></div></div></section></div>`;
}
function settingsPage(){
 return `${heading("settings")}<div class="content-two"><section class="panel"><div class="panel-head"><div><h2>Workspace profile</h2><p>Basic information about your business</p></div></div><div class="form-grid"><div class="form-field full"><label>Business name</label><input value="Brightloop Software"></div><div class="form-field"><label>Contact email</label><input value="billing@brightloop.example"></div><div class="form-field"><label>Default currency</label><select><option>USD — US Dollar</option><option>INR — Indian Rupee</option></select></div><div class="form-field"><label>Timezone</label><select><option>Asia/Kolkata</option><option>UTC</option></select></div><div class="form-field"><label>Date format</label><select><option>DD MMM YYYY</option><option>MM/DD/YYYY</option></select></div></div><div class="form-actions"><button class="btn btn-primary" data-action="saveSettings">Save changes</button></div></section><section class="panel"><div class="panel-head"><div><h2>Workspace preferences</h2><p>Defaults used across your subscription system</p></div></div><div class="setting-row"><div><strong>Invoice reminders</strong><small>Send reminders before an invoice is due.</small></div><input type="checkbox" checked></div><div class="setting-row"><div><strong>Automatic renewals</strong><small>Allow subscriptions to renew at the end of each term.</small></div><input type="checkbox" checked></div><div class="setting-row"><div><strong>Payment notifications</strong><small>Notify administrators when a payment fails.</small></div><input type="checkbox" checked></div></section></div>`;
}
function render(){
 const pages={overview,customers:customersPage,products:productsPage,plans:plansPage,subscriptions:subscriptionsPage,invoices:invoicesPage,payments:paymentsPage,reports:reportsPage,settings:settingsPage};
 document.getElementById("pageContent").innerHTML=(pages[state.page]||overview)()+quickStyles();
 document.getElementById("crumbCurrent").textContent=pageMeta[state.page][0];
 document.querySelectorAll(".nav-item[data-page]").forEach(b=>b.classList.toggle("active",b.dataset.page===state.page));
}
function toast(message,success=true){
 const el=document.createElement("div");el.className="toast"+(success?" success":"");el.textContent=message;
 document.getElementById("toastRegion").appendChild(el);setTimeout(()=>el.remove(),3200);
}
function openModal(title,fields,submitLabel="Save"){
 document.getElementById("modalTitle").textContent=title;
 document.getElementById("modalBody").innerHTML=`<form id="recordForm"><div class="form-grid">${fields.map(f=>`<div class="form-field ${f.full?'full':''}"><label>${f.label}</label>${f.options?`<select name="${f.name}" ${f.required?'required':''}>${f.options.map(o=>`<option>${o}</option>`).join("")}</select>`:`<input name="${f.name}" type="${f.type||'text'}" placeholder="${f.placeholder||''}" ${f.required?'required':''}>`}</div>`).join("")}</div><div class="form-actions"><button type="button" class="btn" id="cancelForm">Cancel</button><button class="btn btn-primary" type="submit">${submitLabel}</button></div></form>`;
 document.getElementById("modalBackdrop").hidden=false;
 document.getElementById("cancelForm").onclick=closeModal;
 document.getElementById("recordForm").onsubmit=e=>{e.preventDefault();const data=Object.fromEntries(new FormData(e.target));handleSave(title,data);closeModal();};
}
function closeModal(){document.getElementById("modalBackdrop").hidden=true}
function handleSave(title,d){
 if(title.toLowerCase().includes("customer")){
  state.customers.unshift({name:d.name,email:d.email,company:d.company||"—",initials:d.name.split(" ").map(v=>v[0]).join("").slice(0,2).toUpperCase(),plan:"Not assigned",amount:"$0.00",status:"Active"});
 }else if(title.toLowerCase().includes("subscription")){
  state.subscriptions.unshift({id:"SUB-"+(1050+state.subscriptions.length),customer:d.customer||"New customer",plan:d.plan||"Starter Monthly",start:d.start||"Today",renewal:d.renewal||"To be scheduled",amount:d.amount||"$49.00",status:"Draft"});
 }else if(title.toLowerCase().includes("product")){
  state.products.unshift({name:d.name,type:d.type||"Software",price:d.price||"$0.00",billing:d.billing||"Monthly",status:"Published"});
 }else if(title.toLowerCase().includes("plan")){
  state.plans.unshift({name:d.name,period:d.period||"Monthly",price:d.price||"$0.00",subscribers:"0",status:"Active"});
 }else if(title.toLowerCase().includes("invoice")){
  state.invoices.unshift({id:"INV-"+(2085+state.invoices.length),customer:d.customer||"New customer",date:"Oct 31, 2026",due:d.due||"Nov 07, 2026",amount:d.amount||"$0.00",status:"Draft"});
 }else if(title.toLowerCase().includes("payment")){
  state.payments.unshift({id:"PAY-"+(3093+state.payments.length),customer:d.customer||"Customer",date:"Oct 31, 2026",method:d.method||"Bank transfer",amount:d.amount||"$0.00",status:"Paid"});
 }
 render();toast(`${title} saved successfully. Demo data updated.`);
}
function handleAction(action,el){
 const forms={
  createCustomer:["Add customer",[{label:"Full name",name:"name",required:true},{label:"Email address",name:"email",type:"email",required:true},{label:"Company",name:"company"}]],
  createSubscription:["Create subscription",[{label:"Customer",name:"customer",required:true},{label:"Plan",name:"plan",options:state.plans.map(p=>p.name)},{label:"Start date",name:"start",type:"date"},{label:"Next renewal",name:"renewal",type:"date"},{label:"Amount",name:"amount",placeholder:"$49.00"}]],
  createProduct:["Add product",[{label:"Product name",name:"name",required:true},{label:"Product type",name:"type",options:["Software","Add-on","Service"]},{label:"Sales price",name:"price",required:true},{label:"Billing period",name:"billing",options:["Monthly","Yearly","One-time"]}]],
  createPlan:["Create plan",[{label:"Plan name",name:"name",required:true},{label:"Billing period",name:"period",options:["Daily","Weekly","Monthly","Yearly"]},{label:"Price",name:"price",required:true}]],
  createInvoice:["Create invoice",[{label:"Customer",name:"customer",required:true},{label:"Due date",name:"due",type:"date"},{label:"Total amount",name:"amount",required:true}]],
  createPayment:["Record payment",[{label:"Customer",name:"customer",required:true},{label:"Payment method",name:"method",options:["Bank transfer","Card","Cash","Other"]},{label:"Amount",name:"amount",required:true}]]
 };
 if(forms[action]){openModal(...forms[action]);return}
 if(action==="export"){const csv="Reference,Customer,Status,Amount\n"+state.invoices.map(x=>`${x.id},${x.customer},${x.status},${x.amount}`).join("\n");const a=document.createElement("a");a.href=URL.createObjectURL(new Blob([csv],{type:"text/csv"}));a.download="subscript-export.csv";a.click();URL.revokeObjectURL(a.href);toast("Export downloaded.");return}
 if(action==="saveSettings"){toast("Workspace preferences saved (demo).");return}
 if(action==="viewCustomer"){const x=state.customers.find(c=>c.name===el.dataset.name);toast(`${x.name} · ${x.email} · ${x.company}`);return}
 if(action==="viewSubscription"){const x=state.subscriptions.find(c=>c.id===el.dataset.id);toast(`${x.id} · ${x.customer} · ${x.status}`);return}
 if(action==="viewInvoice"){const x=state.invoices.find(c=>c.id===el.dataset.id);toast(`${x.id} · ${x.customer} · ${x.amount} · ${x.status}`);return}
 if(action==="editProduct"||action==="editPlan"){toast("Edit form will connect to the backend module.");return}
}
document.addEventListener("click",e=>{
 const pageBtn=e.target.closest("[data-page]");if(pageBtn){state.page=pageBtn.dataset.page;render();document.getElementById("sidebar").classList.remove("open");document.querySelector(".mobile-overlay")?.classList.remove("show");return}
 const actionBtn=e.target.closest("[data-action]");if(actionBtn){handleAction(actionBtn.dataset.action,actionBtn);return}
});
document.addEventListener("input",e=>{
 if(!e.target.dataset.filter)return;
 const type=e.target.dataset.filter,term=e.target.value.toLowerCase();
 const rows=[...document.querySelectorAll(".data-table tbody tr")];
 rows.forEach(row=>row.style.display=row.textContent.toLowerCase().includes(term)?"":"none");
});
document.getElementById("closeModal").onclick=closeModal;
document.getElementById("modalBackdrop").addEventListener("click",e=>{if(e.target.id==="modalBackdrop")closeModal()});
document.getElementById("mobileMenu").onclick=()=>{document.getElementById("sidebar").classList.toggle("open")};
document.getElementById("notificationBtn").onclick=()=>toast("You are all caught up.");
document.getElementById("helpBtn").onclick=()=>toast("Help centre link can be connected by the team.");
document.getElementById("globalSearch").addEventListener("keydown",e=>{if(e.key==="Enter"){const q=e.target.value.toLowerCase();const found=Object.keys(pageMeta).find(p=>pageMeta[p][0].toLowerCase().includes(q));if(found){state.page=found;render()}else toast("No matching workspace page found.",false)}});
document.addEventListener("keydown",e=>{if(e.key==="Escape")closeModal();if((e.metaKey||e.ctrlKey)&&e.key.toLowerCase()==="k"){e.preventDefault();document.getElementById("globalSearch").focus()}});
render();
