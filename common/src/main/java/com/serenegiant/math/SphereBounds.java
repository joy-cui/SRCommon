/*    */ package com.serenegiant.math;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SphereBounds
/*    */   extends CircleBounds
/*    */ {
/*    */   private static final long serialVersionUID = 5374122610666117206L;
/*    */   
/* 12 */   public SphereBounds(float x, float y, float z, float radius) { super(x, y, z, radius); }
/*    */ 
/*    */ 
/*    */   
/* 16 */   public SphereBounds(float x, float y, float radius) { super(x, y, radius); }
/*    */ 
/*    */ 
/*    */   
/* 20 */   public SphereBounds(Vector v, float radius) { super(v, radius); }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\math\SphereBounds.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */