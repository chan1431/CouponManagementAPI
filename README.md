Coupons Management API

This project aims to design and implement a Coupons Management API for an e-commerce platform. The goal is to handle three types of discount coupons: Cart-wise, Product-wise, and Buy X, Get Y (BxGy). The system must also allow for easy extension to add future types of coupons.

Key Considerations:
- Coupon Types: Cart-wise, Product-wise, and BxGy (Buy X, Get Y).
- Scalability: The system should support easily adding new coupon types in the future.
- Extensibility: The API should be designed to accommodate future coupon rules and conditions.

Coupon Types:

1. Cart-wise Coupons: Discount applied to the entire cart if the total exceeds a threshold.
   - Example: "10% off on carts over Rs. 100" if the cart total > Rs. 100.
   
2. Product-wise Coupons: Discount applied to specific products in the cart.
   - Example: "20% off on Product A" if Product A is in the cart.

3. Buy X, Get Y Coupons (BxGy): Buy a certain number of products from one list and get free items from another list.
   - Example: "Buy 2 of products from [X, Y, Z] and get 1 from [A, B, C] free."
   - Includes repetition limits: Apply the offer only up to a specified repetition limit.



API Design:

1.POST /coupons
- Create a new coupon. The request body should include:
  - **type**: Cart-wise, Product-wise, or BxGy
  - **discount_details**: The amount or percentage off.
  - **conditions**: Conditions for applicability, such as product list, cart total, etc.
  - **applicable_items** (for BxGy): Arrays defining the "buy" and "get" products.
  - **validity**: Start and end date (if applicable).
  
2.GET /coupons
- Retrieve all coupons.

3.GET /coupons/{id}
- Retrieve a specific coupon by its ID.

4.PUT /coupons/{id}
- Update a specific coupon by its ID.

5.DELETE /coupons/{id}
- Delete a specific coupon by its ID.

6.POST /applicable-coupons
- Given a cart, return a list of applicable coupons and the discount amount that would be applied by each coupon.

7.POST /apply-coupon/{id}
- Apply a specific coupon to the cart and return the updated cart with discounted prices.



Coupon Structure (Schema):

The general structure for coupons will include:
- id: Unique identifier for the coupon.
- type: Enum (Cart-wise, Product-wise, BxGy).
- discount_details: Could be a percentage or flat amount (e.g., "10% off" or "Rs. 50 off").
- conditions: Conditions for the coupon (e.g., minimum cart value, specific products, etc.).
- applicable_items: List of applicable items (if BxGy or product-wise).
- validity: Start and end dates to determine if the coupon is active.


Scenarios and Cases:

1.Cart-wise Coupon Scenarios:
- Apply a 10% discount if the cart value exceeds Rs. 100.
  - Condition: Cart total > Rs. 100
  - Discount: 10% off on the entire cart.

Edge Cases:
- Cart value is exactly Rs. 100 (should not apply).
- Cart value is just above Rs. 100 (should apply discount).

2.Product-wise Coupon Scenarios:
- Apply a 20% discount on Product A if it's in the cart.
  - Condition: Cart contains Product A.
  - Discount: 20% off Product A.

Edge Cases:
- Product A is present multiple times (should apply discount to each occurrence).
- Cart contains Product B (should not apply discount).

3.BxGy Coupon Scenarios:
- Buy 2 items from [X, Y, Z] and get 1 free from [A, B, C].
  - Conditions:
    - Buy 2 from ["X", "Y", "Z"] and get 1 from ["A", "B", "C"] free.
    - Repetition limit: Can only apply 3 times.
    
Edge Cases:
- Cart contains fewer than 2 items from the "buy" list (should not apply).
- Cart has products from both lists but fewer than the required "buy" items (should not apply).
- Exceeding repetition limit (should apply only up to 3 times).


Error Handling:

- Invalid Coupon ID: Return a 404 error if the coupon is not found.
- couponNotFound : returns CouponNotFound Exception if it doesnt exist.


Assumptions:

1. Coupon Validity: Coupons are only valid within the specified validity dates.
2. Product Availability: Products in the cart are assumed to be valid and available for applying product-wise or BxGy discounts.
3. Data Integrity: The cart and coupon data are assumed to be clean, with no missing fields (e.g., product IDs, prices).
4. Multiple Coupons: Multiple coupons can be applied to the same cart, but cart-wise coupons would apply to the total cart, while product-wise coupons apply to individual products.


Limitations:

1. No Support for Stacked Coupons: Cart-wise and product-wise coupons cannot be stacked or combined. The system applies only the highest applicable coupon.
2. Edge Case Handling**: Some edge cases, like partial coupon application (e.g., only applying part of the "buy" requirement in BxGy), have not been fully implemented due to time constraints.


Future Improvements:

1. Coupon Expiry Logic: Implement expiration dates for coupons.
2. Coupon Stacking: Allow for stacked coupons, where multiple coupons can be applied together with different levels of discount.
3. Advanced BxGy Logic: Improve the "Buy X, Get Y" logic to handle more complex scenarios, like partial application of the offer.
4. Database Optimization: Implement more advanced database features (e.g., indexing, caching) to speed up coupon retrieval and application in larger carts.

