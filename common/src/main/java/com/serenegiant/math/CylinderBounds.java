/*    */ package com.serenegiant.math;
/*    */ 
/*    */ 
/*    */ public class CylinderBounds
/*    */   extends BaseBounds
/*    */ {
/*    */   private static final long serialVersionUID = -2875851852923460432L;
/*    */   protected float height;
/*    */   protected float outer_r;
/* 10 */   private final Vector w1 = new Vector();
/* 11 */   private final Vector w2 = new Vector();
/*    */ 
/*    */   
/*    */   public CylinderBounds(float x, float y, float z, float height, float radius) {
/* 15 */     this.position.set(x, y, z);
/* 16 */     this.radius = (float)Math.sqrt((radius * radius + height * height / 4.0F));
/* 17 */     this.outer_r = radius;
/* 18 */     this.height = height / 2.0F;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 23 */   public CylinderBounds(Vector center, float height, float radius) { this(center.x, center.y, center.z, height, radius); }
/*    */ 
/*    */   
/*    */   protected boolean ptInCylinder(float x, float y, float z, float r) {
/* 27 */     boolean f = false;
/*    */ 
/*    */     
/* 30 */     this.w1.set(x, y, z).sub(this.position).rotate(this.angle, -1.0F);
/*    */     
/* 32 */     this.w2.set(this.w1); this.w2.y = 0.0F;
/* 33 */     if (this.w2.distSquared(this.position.x, 0.0F, this.position.z) < r * r) {
/* 34 */       float x1 = this.position.x - r;
/* 35 */       float x2 = this.position.x + r;
/* 36 */       float y1 = this.position.y - this.height;
/* 37 */       float y2 = this.position.y + this.height;
/* 38 */       f = (this.w1.x >= x1 && this.w1.x <= x2 && this.w1.y >= y1 && this.w1.y <= y2);
/*    */     } 
/* 40 */     return f;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean ptInBounds(float x, float y, float z) {
/* 45 */     boolean f = ptInBoundsSphere(x, y, z, this.radius);
/* 46 */     if (f) {
/* 47 */       f = ptInCylinder(x, y, z, this.outer_r);
/*    */     }
/* 49 */     return f;
/*    */   }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\math\CylinderBounds.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */