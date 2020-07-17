/*    */ package com.serenegiant.math;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CircleBounds
/*    */   extends BaseBounds
/*    */ {
/*    */   private static final long serialVersionUID = -6571630061846420508L;
/*    */   
/*    */   public CircleBounds(float x, float y, float z, float radius) {
/* 12 */     this.position.set(x, y, z);
/* 13 */     this.radius = radius;
/*    */   }
/*    */ 
/*    */   
/* 17 */   public CircleBounds(float x, float y, float radius) { this(x, y, 0.0F, radius); }
/*    */ 
/*    */ 
/*    */   
/* 21 */   public CircleBounds(Vector v, float radius) { this(v.x, v.y, 0.0F, radius); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   public boolean ptInBounds(float x, float y, float z) { return ptInBoundsSphere(x, y, z, this.radius); }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\math\CircleBounds.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */