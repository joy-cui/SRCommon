/*    */ package com.serenegiant.graphics;
/*    */ 
/*    */ import android.graphics.Path;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TriangleShape
/*    */   extends PathShape
/*    */ {
/* 14 */   public TriangleShape() { this(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 25 */   public TriangleShape(float[] pointes) { this(pointes[0], pointes[1], pointes[2], pointes[3], pointes[4], pointes[5]); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public TriangleShape(float x0, float y0, float x1, float y1, float x2, float y2) {
/* 41 */     super(null, delta(x0, x1, x2), delta(y0, y1, y2));
/* 42 */     float minx = min(x0, x1, x2);
/* 43 */     float miny = min(y0, y1, y2);
/* 44 */     Path path = new Path();
/* 45 */     path.moveTo(x0 - minx, y0 - miny);
/* 46 */     path.lineTo(x1 - minx, y1 - miny);
/* 47 */     path.lineTo(x2 - minx, y2 - miny);
/* 48 */     path.close();
/* 49 */     setPath(path);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 60 */   private static final float min(float v0, float v1, float v2) { return Math.min(Math.min(v0, v1), v2); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 71 */   private static final float max(float v0, float v1, float v2) { return Math.max(Math.max(v0, v1), v2); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 82 */   private static final float delta(float v0, float v1, float v2) { return max(v0, v1, v2) - min(v0, v1, v2); }
/*    */ }


/* Location:              E:\tools\工具\classes.jar!\com\serenegiant\graphics\TriangleShape.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.2
 */