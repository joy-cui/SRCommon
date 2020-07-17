/*    */ package com.serenegiant.math;
/*    */ 
/*    */ 
/*    */ public class RingBounds
/*    */   extends CylinderBounds
/*    */ {
/*    */   private static final long serialVersionUID = -5157039256747626240L;
/*    */   protected float height;
/*    */   protected float inner_r;
/*    */   
/*    */   public RingBounds(float x, float y, float z, float height, float outer, float inner) {
/* 12 */     super(x, y, z, height, outer);
/* 13 */     this.inner_r = inner;
/*    */   }
/*    */ 
/*    */   
/* 17 */   public RingBounds(Vector center, float height, float outer, float inner) { this(center.x, center.y, center.z, height, outer, inner); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean ptInBounds(float x, float y, float z) {
/* 22 */     boolean f = super.ptInBounds(x, y, z);
/* 23 */     if (f) {
/* 24 */       f = !ptInCylinder(x, y, z, this.inner_r);
/*    */     }
/* 26 */     return f;
/*    */   }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\math\RingBounds.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */