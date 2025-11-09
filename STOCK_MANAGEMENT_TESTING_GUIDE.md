# Stock Management with FIFO - Testing Guide

## 📋 **What We've Implemented**

### ✅ **Components Created:**
1. **StockBatchRepository** - Manages FIFO batch queries
2. **StockMovementRepository** - Tracks all stock movements
3. **StockService** - Core FIFO business logic
4. **StockController** - REST API endpoints
5. **Integration** - Order reception automatically creates stock batches

---

## 🚀 **Available Endpoints**

### 1. Get Product Stock Detail with FIFO Batches
**Endpoint:** `GET /api/v1/stock/produit/{productId}`

**Description:** Shows detailed stock information including all batches ordered by FIFO

**Example:** `GET http://localhost:9005/api/v1/stock/produit/1`

**Response:**
```json
{
  "productId": 1,
  "productName": "Tissu Coton Blanc",
  "totalQuantity": 500.0,
  "totalValue": 21500.00,
  "batches": [
    {
      "id": 1,
      "batchNumber": "LOT-TIS--20251108-0001",
      "productId": 1,
      "productName": "Tissu Coton Blanc",
      "quantityRemaining": 500.0,
      "unitPurchasePrice": 43.00,
      "entryDate": "2025-11-08",
      "supplierOrderId": 1,
      "supplierOrderNumber": "CMD-20251108-0001"
    }
  ]
}
```

---

### 2. Get All Stock Movements
**Endpoint:** `GET /api/v1/stock/mouvements`

**Description:** Returns history of all stock movements (entries and exits)

**Example:** `GET http://localhost:9005/api/v1/stock/mouvements`

**Response:**
```json
[
  {
    "id": 1,
    "productId": 1,
    "productName": "Tissu Coton Blanc",
    "movementType": "ENTREE",
    "quantity": 500.0,
    "unitPrice": 43.00,
    "batchNumber": "LOT-TIS--20251108-0001",
    "reference": "SUPPLIER_ORDER-1",
    "movementDate": "2025-11-08T14:30:00",
    "notes": "Réception commande fournisseur: CMD-20251108-0001"
  }
]
```

---

### 3. Get Stock Movements for Specific Product
**Endpoint:** `GET /api/v1/stock/mouvements/produit/{productId}`

**Description:** Returns movement history for a single product

**Example:** `GET http://localhost:9005/api/v1/stock/mouvements/produit/1`

---

### 4. Get Stock Alerts
**Endpoint:** `GET /api/v1/stock/alertes`

**Description:** Returns list of products below their reorder point

**Example:** `GET http://localhost:9005/api/v1/stock/alertes`

**Response:**
```json
[
  {
    "productId": 2,
    "productName": "Boutons Blancs 15mm",
    "currentStock": 50.0,
    "reorderPoint": 5000.0,
    "deficit": 4950.0
  }
]
```

---

### 5. Get Stock Valuation
**Endpoint:** `GET /api/v1/stock/valorisation`

**Description:** Calculates total stock value using FIFO method

**Example:** `GET http://localhost:9005/api/v1/stock/valorisation`

**Response:**
```json
{
  "totalStockValue": 45650.00,
  "totalProducts": 3,
  "totalBatches": 5
}
```

---

## 🧪 **Complete Testing Workflow**

### **Step 1: Create Suppliers and Products**
Use your existing supplier and product endpoints to create test data.

### **Step 2: Create and Validate Order**

1. **Create Order:**
```
POST http://localhost:9005/api/v1/commandes
Content-Type: application/json

{
  "supplierId": 1,
  "notes": "Test order for FIFO stock",
  "orderLines": [
    {
      "productId": 1,
      "quantityOrdered": 500.0,
      "unitPurchasePrice": 43.00
    },
    {
      "productId": 2,
      "quantityOrdered": 10000.0,
      "unitPurchasePrice": 0.22
    }
  ]
}
```

2. **Validate Order:**
```
PUT http://localhost:9005/api/v1/commandes/{orderId}/valider
```

3. **Receive Order (Creates Stock Batches Automatically!):**
```
PUT http://localhost:9005/api/v1/commandes/{orderId}/reception
```

### **Step 3: Verify Stock Creation**

**Check Product Stock:**
```
GET http://localhost:9005/api/v1/stock/produit/1
```

**Expected:** You should see a new batch created with the quantities from the order.

**Check Stock Movements:**
```
GET http://localhost:9005/api/v1/stock/mouvements
```

**Expected:** You should see ENTREE movements for each product in the received order.

### **Step 4: Check Product Stock via Product Endpoint**
```
GET http://localhost:9005/api/v1/produits/1/stock
```

**Expected:** The `currentStock` should be updated with the received quantities.

### **Step 5: Test Stock Alerts**

If any products are below their reorder point:
```
GET http://localhost:9005/api/v1/stock/alertes
```

### **Step 6: Check Stock Valuation**
```
GET http://localhost:9005/api/v1/stock/valorisation
```

---

## 🔄 **How FIFO Works in Your System**

### **Stock Entry (Automatic on Order Reception):**
1. Order is created with status `EN_ATTENTE`
2. Order is validated → status becomes `VALIDEE`
3. Order is received → status becomes `LIVREE`
4. **Automatically:**
   - Creates `StockBatch` for each order line
   - Generates unique batch number (e.g., `LOT-TIS--20251108-0001`)
   - Creates `StockMovement` with type `ENTREE`
   - Updates product `currentStock`

### **Stock Exit (FIFO Consumption):**
When you implement delivery notes, the `processStockExit()` method will:
1. Find oldest batches first (by entry date)
2. Consume quantities from oldest batches
3. If a batch is depleted, move to next oldest
4. Create `StockMovement` with type `SORTIE`
5. Update product `currentStock`

### **Example FIFO Scenario:**
```
Product: Tissu Coton

Entry 1: 100 units @ 40€ on Nov 1st → Batch LOT-TIS--20251101-0001
Entry 2: 50 units @ 45€ on Nov 5th → Batch LOT-TIS--20251105-0002

Exit: 120 units needed
→ Take 100 from Batch-0001 (oldest)
→ Take 20 from Batch-0002 (next oldest)
→ Remaining: 30 units @ 45€ in Batch-0002
```

---

## 📊 **Testing Checklist**

- [ ] Create supplier and products
- [ ] Create order with multiple products
- [ ] Validate order
- [ ] Receive order
- [ ] Check product stock detail (verify batches created)
- [ ] Check stock movements (verify ENTREE movements)
- [ ] Check product current stock updated
- [ ] Check stock alerts (if applicable)
- [ ] Check stock valuation
- [ ] Create second order from same supplier
- [ ] Receive second order
- [ ] Verify multiple batches for same product

---

## 🎯 **Expected Behavior**

### ✅ **After Receiving First Order:**
- Stock batches created automatically
- Product stock updated
- Movement history recorded
- Batch numbers generated uniquely

### ✅ **After Receiving Multiple Orders:**
- Multiple batches for same product
- Batches ordered by entry date (FIFO)
- Stock valuation reflects all batches

### ✅ **Stock Alerts:**
- Shows products below reorder point
- Calculates deficit

### ✅ **Stock Valuation:**
- Sums value of all remaining quantities
- Uses purchase price from each batch

---

## 🐛 **Troubleshooting**

### Issue: "No batches found"
**Solution:** Make sure you've received an order first. Stock batches are only created when orders are received (status = LIVREE).

### Issue: "Product stock not updated"
**Solution:** Check that the order was properly received via `/api/v1/commandes/{id}/reception`

### Issue: "Empty stock movements"
**Solution:** Stock movements are created when orders are received. Ensure order status progression: EN_ATTENTE → VALIDEE → LIVREE

---

## 📝 **What's Next?**

After testing Stock Management, you'll implement:
1. **Delivery Notes Module** (Bons de Sortie)
   - Will use `processStockExit()` method
   - Implements FIFO consumption
   - Links to stock movements

---

**Date:** November 8, 2025  
**Application Port:** 9005  
**Base URL:** http://localhost:9005/api/v1

