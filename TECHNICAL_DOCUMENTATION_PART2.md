# TECHNICAL DOCUMENTATION - Part 2: Advanced Search Implementation

## 📋 PROJECT CONTEXT
**Project:** Tricol Stock Management API  
**Requirement:** Implement advanced search for stock movements with pagination  
**Technology Stack:** Spring Boot 3.5.7, JPA, MySQL, Maven  

---

## 🎯 TECHNICAL OBJECTIVES

### What We Built:
- **Dynamic Search API** for stock movements with multiple criteria
- **Pagination Support** for large datasets
- **JPA Specifications** for flexible query building

### Why These Technologies:
- **JPA Specifications:** Industry standard for dynamic queries
- **Pageable Interface:** Spring's built-in pagination mechanism
- **RESTful Design:** Standard API practices

---

## 🏗️ ARCHITECTURE OVERVIEW

```
Controller Layer (REST API)
    ↓
Service Layer (Business Logic)
    ↓
Repository Layer (JPA Specifications)
    ↓
Database (MySQL)
```

---

## 📁 FILES CREATED/MODIFIED

### 1. **StockMovementSpecification.java** (NEW)
**Purpose:** Build dynamic SQL queries based on search criteria

```java
public class StockMovementSpecification {
    public static Specification<StockMovement> withFilters(
            LocalDate dateDebut, LocalDate dateFin, Long produitId, 
            String reference, MovementType type, String numeroLot) {
        
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            // Date range filtering
            if (dateDebut != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                    root.get("movementDate"), dateDebut.atStartOfDay()));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
```

**Key Concepts Explained:**

- **Specification Interface:** Spring Data JPA interface for building dynamic queries
- **Predicate:** Represents a condition in SQL WHERE clause
- **CriteriaBuilder:** JPA API for building type-safe queries
- **Root:** Represents the entity being queried (StockMovement)

**Why This Approach:**
- ✅ **Type-safe:** Compile-time checking of field names
- ✅ **Dynamic:** Add/remove filters without changing code
- ✅ **Reusable:** Can combine specifications
- ✅ **Performance:** Generates optimized SQL

### 2. **StockMovementRepository.java** (MODIFIED)
**Before:**
```java
public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {
    List<StockMovement> findAllByOrderByMovementDateDesc();
}
```

**After:**
```java
public interface StockMovementRepository extends JpaRepository<StockMovement, Long>, 
                                                JpaSpecificationExecutor<StockMovement> {
    List<StockMovement> findAllByOrderByMovementDateDesc();
}
```

**Key Concepts:**
- **JpaSpecificationExecutor:** Adds methods like `findAll(Specification, Pageable)`
- **Multiple Inheritance:** Repository extends two interfaces
- **No Implementation Needed:** Spring Data generates implementation automatically

### 3. **StockService.java** (MODIFIED)
**New Method Added:**
```java
@Transactional(readOnly = true)
public Page<StockMovementResponseDTO> searchMovements(
        LocalDate dateDebut, LocalDate dateFin, Long produitId,
        String reference, MovementType type, String numeroLot,
        Pageable pageable) {
    
    return stockMovementRepository
            .findAll(StockMovementSpecification.withFilters(
                    dateDebut, dateFin, produitId, reference, type, numeroLot), pageable)
            .map(this::mapToMovementDTO);
}
```

**Key Concepts:**
- **@Transactional(readOnly = true):** Optimizes database performance for read operations
- **Page<T>:** Spring's pagination wrapper containing data + metadata
- **Pageable:** Interface defining page number, size, and sorting
- **map():** Converts entity objects to DTOs for API response

### 4. **StockController.java** (MODIFIED)
**Enhanced Endpoint:**
```java
@GetMapping("/mouvements")
public ResponseEntity<Page<StockMovementResponseDTO>> searchMovements(
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,
        @RequestParam(required = false) Long produitId,
        @RequestParam(required = false) String reference,
        @RequestParam(required = false) MovementType type,
        @RequestParam(required = false) String numeroLot,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size) {
    
    Pageable pageable = PageRequest.of(page, size, Sort.by("movementDate").descending());
    
    Page<StockMovementResponseDTO> movements = stockService.searchMovements(
            dateDebut, dateFin, produitId, reference, type, numeroLot, pageable);
    
    return ResponseEntity.ok(movements);
}
```

**Key Concepts:**
- **@RequestParam(required = false):** Makes parameters optional
- **@DateTimeFormat:** Converts string "2025-01-01" to LocalDate object
- **defaultValue:** Sets default values for pagination
- **PageRequest.of():** Creates Pageable object with sorting
- **Sort.by().descending():** Orders results by movement date (newest first)

---

## 🔍 DEEP DIVE: JPA SPECIFICATIONS

### What Happens Behind the Scenes:

**1. Request Processing:**
```
GET /api/v1/stock/mouvements?produitId=123&type=SORTIE&page=0&size=10
```

**2. Controller converts parameters:**
```java
produitId = 123L
type = MovementType.SORTIE
page = 0, size = 10
```

**3. Specification builds SQL:**
```sql
SELECT sm.* FROM stock_movements sm 
JOIN products p ON sm.product_id = p.id 
WHERE p.id = 123 
AND sm.movement_type = 'SORTIE'
ORDER BY sm.movement_date DESC 
LIMIT 10 OFFSET 0
```

**4. JPA executes query and returns Page object**

### Specification Building Process:

```java
// 1. Create empty predicate list
List<Predicate> predicates = new ArrayList<>();

// 2. Add conditions based on parameters
if (produitId != null) {
    predicates.add(criteriaBuilder.equal(root.get("product").get("id"), produitId));
}

// 3. Combine all predicates with AND
return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
```

---

## 📊 PAGINATION EXPLAINED

### Page Object Structure:
```json
{
  "content": [...],           // Actual data
  "pageable": {
    "pageNumber": 0,          // Current page
    "pageSize": 20,           // Items per page
    "sort": {...}             // Sorting info
  },
  "totalElements": 150,       // Total records in database
  "totalPages": 8,            // Total pages available
  "size": 20,                 // Page size
  "number": 0,                // Current page number
  "first": true,              // Is first page?
  "last": false,              // Is last page?
  "numberOfElements": 20      // Items in current page
}
```

### Benefits:
- **Performance:** Only loads needed data
- **User Experience:** Faster page loads
- **Memory Efficiency:** Prevents OutOfMemoryError
- **Scalability:** Handles millions of records

---

## 🎯 API USAGE EXAMPLES

### 1. Simple Product Search:
```bash
GET /api/v1/stock/mouvements?produitId=123
```
**Generated SQL:** `WHERE product_id = 123`

### 2. Date Range Search:
```bash
GET /api/v1/stock/mouvements?dateDebut=2025-01-01&dateFin=2025-03-31
```
**Generated SQL:** `WHERE movement_date >= '2025-01-01 00:00:00' AND movement_date <= '2025-03-31 23:59:59'`

### 3. Complex Multi-Criteria:
```bash
GET /api/v1/stock/mouvements?reference=PROD001&type=ENTREE&dateDebut=2025-01-01&page=1&size=5
```
**Generated SQL:**
```sql
SELECT * FROM stock_movements sm
JOIN products p ON sm.product_id = p.id
WHERE p.reference = 'PROD001'
AND sm.movement_type = 'ENTREE'
AND sm.movement_date >= '2025-01-01 00:00:00'
ORDER BY sm.movement_date DESC
LIMIT 5 OFFSET 5
```

---

## 🔧 TECHNICAL DECISIONS & JUSTIFICATIONS

### 1. **Why JPA Specifications over @Query?**
- ✅ **Dynamic:** Can build queries at runtime
- ✅ **Type-safe:** Compile-time field validation
- ✅ **Reusable:** Combine specifications
- ❌ **@Query:** Static, requires multiple methods for different combinations

### 2. **Why Page<T> instead of List<T>?**
- ✅ **Metadata:** Total count, page info
- ✅ **Performance:** Lazy loading
- ✅ **Frontend-friendly:** Easy pagination UI
- ❌ **List<T>:** No pagination info, memory issues

### 3. **Why @DateTimeFormat?**
- ✅ **Automatic conversion:** String → LocalDate
- ✅ **ISO standard:** Universal date format
- ✅ **Validation:** Invalid dates rejected automatically

---

## 🧪 TESTING APPROACH

### Test Structure:
```java
@WebMvcTest(StockController.class)
class StockControllerAdvancedSearchTest {
    
    @MockBean
    private StockService stockService;
    
    @Test
    void testAdvancedSearchEndpoint() throws Exception {
        // Mock service response
        when(stockService.searchMovements(...))
                .thenReturn(new PageImpl<>(Collections.emptyList()));
        
        // Test HTTP request
        mockMvc.perform(get("/api/v1/stock/mouvements")
                .param("produitId", "123")
                .param("type", "SORTIE"))
                .andExpect(status().isOk());
    }
}
```

**Testing Strategy:**
- **Unit Tests:** Controller layer with mocked service
- **Integration Tests:** Full stack with test database
- **Performance Tests:** Large datasets pagination

---

## 📈 PERFORMANCE CONSIDERATIONS

### Database Optimization:
```sql
-- Recommended indexes for performance
CREATE INDEX idx_stock_movements_product_id ON stock_movements(product_id);
CREATE INDEX idx_stock_movements_date ON stock_movements(movement_date);
CREATE INDEX idx_stock_movements_type ON stock_movements(movement_type);
CREATE INDEX idx_products_reference ON products(reference);
```

### Query Performance:
- **Pagination:** Prevents loading all records
- **Indexes:** Fast WHERE clause execution
- **Lazy Loading:** Related entities loaded on demand
- **Read-only Transactions:** Database optimization

---

## 🚀 DEPLOYMENT CONSIDERATIONS

### Configuration:
```properties
# application.properties
spring.data.web.pageable.default-page-size=20
spring.data.web.pageable.max-page-size=100
spring.jpa.show-sql=false  # Disable in production
```

### Monitoring:
- **Slow Query Log:** Monitor performance
- **Connection Pool:** Optimize database connections
- **Caching:** Consider Redis for frequent searches

---

## 📚 KEY LEARNING POINTS

### 1. **JPA Specifications Pattern:**
- Industry standard for dynamic queries
- Type-safe alternative to string-based queries
- Composable and reusable

### 2. **Pagination Best Practices:**
- Always paginate large datasets
- Provide total count for UI
- Default reasonable page sizes

### 3. **RESTful API Design:**
- Use query parameters for filtering
- Return consistent response format
- Handle optional parameters gracefully

### 4. **Spring Boot Integration:**
- Leverage auto-configuration
- Use standard interfaces (Pageable, Specification)
- Follow convention over configuration

---

## 🎯 PRESENTATION TALKING POINTS

### For Technical Audience:
1. **"We implemented JPA Specifications for dynamic query building..."**
2. **"The Specification pattern allows type-safe, composable queries..."**
3. **"Pagination prevents memory issues and improves performance..."**

### For Business Audience:
1. **"Users can now search stock movements by multiple criteria..."**
2. **"The system handles large datasets efficiently..."**
3. **"Search results load quickly with pagination..."**

### Demo Script:
1. Show simple search: `?produitId=123`
2. Show date range: `?dateDebut=2025-01-01&dateFin=2025-03-31`
3. Show pagination: `?page=0&size=5`
4. Show complex search with multiple criteria
5. Explain the JSON response structure

---

## 🔍 TROUBLESHOOTING GUIDE

### Common Issues:
1. **Date Format Errors:** Use ISO format (YYYY-MM-DD)
2. **Invalid Enum Values:** Use exact enum names (ENTREE, SORTIE)
3. **Page Size Too Large:** Respect max-page-size limit
4. **Performance Issues:** Check database indexes

### Debug Tips:
- Enable SQL logging: `spring.jpa.show-sql=true`
- Use browser dev tools for API testing
- Check application logs for errors

This documentation provides complete technical coverage for your debriefing presentation.